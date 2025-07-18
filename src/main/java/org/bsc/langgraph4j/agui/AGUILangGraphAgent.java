package org.bsc.langgraph4j.agui;
import org.bsc.langgraph4j.CompileConfig;
import org.bsc.langgraph4j.GraphStateException;
import org.bsc.langgraph4j.RunnableConfig;
import org.bsc.langgraph4j.StateGraph;
import org.bsc.langgraph4j.action.InterruptionMetadata;
import org.bsc.langgraph4j.agent.AgentEx;
import org.bsc.langgraph4j.checkpoint.BaseCheckpointSaver;
import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.streaming.StreamingOutput;
import org.bsc.langgraph4j.utils.TryConsumer;
import org.bsc.langgraph4j.utils.TryFunction;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.Objects.requireNonNull;

public abstract class AGUILangGraphAgent implements AGUIAgent {
    public record GraphData( StateGraph<? extends AgentState> stateGraph,
                             boolean interruption)
    {
        public GraphData {
            requireNonNull( stateGraph, "stateGraph cannot ne bull");
        }

        public GraphData( StateGraph<? extends AgentState> stateGraph) {
            this(stateGraph, false);
        }

        public GraphData withInterruption( boolean interrupt ) {
            if( this.interruption == interrupt ) {
                return this;
            }
            return new GraphData(stateGraph, interrupt);
        }
     }

     public record Approval( String toolId, String toolName, String toolArgs) {
        public Approval {
            requireNonNull( toolId, "toolId cannot ne bull");
            requireNonNull( toolName, "toolName cannot ne bull");
            requireNonNull( toolArgs, "toolArgs cannot ne bull");
        }
     }

    static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AGUILangGraphAgent.class);

    final BaseCheckpointSaver saver;

    private final Map<String, GraphData> graphByThread = new ConcurrentHashMap<>();

    protected AGUILangGraphAgent(BaseCheckpointSaver saver ) {
        this.saver = saver;
    }

    abstract GraphData buildStateGraph() throws GraphStateException;

    abstract Map<String,Object> buildGraphInput( AGUIType.RunAgentInput input );

    abstract <State extends AgentState> List<Approval> onInterruption(AGUIType.RunAgentInput input, InterruptionMetadata<State> state );

    @Override
    public final Flux<? extends AGUIEvent> run(AGUIType.RunAgentInput input) {

        final var graphData = graphByThread.computeIfAbsent(input.threadId(), TryFunction.Try(k -> buildStateGraph() ));

        return Flux.create( emitter -> {
            try {

                var compileConfig = CompileConfig.builder()
                        .checkpointSaver(saver)
                        .build();

                var agent = graphData.stateGraph()
                        .compile(compileConfig);

                emitter.next(new AGUIEvent.RunStartedEvent(
                        input.threadId(),
                        input.runId()));

                var messageId = String.valueOf(System.currentTimeMillis());

                var runnableConfig = RunnableConfig.builder()
                        .threadId(input.threadId())
                        .build();

                var isEmittingChunk = new AtomicBoolean(false);

                final Map<String,Object> graphInput ;

                if( graphData.interruption() ) {

                    var lastResultMessage = input.lastResultMessage()
                                    .map(AGUIMessage.ResultMessage::result)
                                    .orElseThrow( () -> new IllegalStateException( "last result message not found after interruption") );

                    runnableConfig = agent.updateState( runnableConfig, Map.of(AgentEx.APPROVAL_RESULT_PROPERTY, lastResultMessage ));

                    graphInput = null; // resume graph
                }
                else {
                    graphInput = buildGraphInput(input);
                }

                agent.stream( graphInput, runnableConfig )
                        .async()
                        .forEachAsync( event -> {

                            if (event instanceof StreamingOutput<? extends AgentState> output) {

                                if(isEmittingChunk.compareAndSet(false, true)) {
                                    log.trace( "STREAMING START");
                                    emitter.next(new AGUIEvent.TextMessageStartEvent(messageId));
                                }
                                if( output.chunk().isEmpty()) {
                                    log.trace( "STREAMING CHUNK IS EMPTY");
                                }
                                else {
                                    log.trace( "{}", output.chunk());
                                        emitter.next(new AGUIEvent.TextMessageContentEvent(messageId, output.chunk()));
                                }

                            } else {

                                var isEndEmittingChunk = isEmittingChunk.compareAndSet(true, false);

                                if(isEndEmittingChunk) {
                                    log.trace( "STREAMING END");
                                    emitter.next(new AGUIEvent.TextMessageEndEvent(messageId));
                                }
                                else {
                                    log.trace( "NEXT:\n{}", event);
                                }
                            }

                        }).thenAccept( TryConsumer.Try(result -> {
                            log.trace( "COMPLETE:\n{}", result);


                            if( result instanceof InterruptionMetadata<?> interruptionMetadata ) {

                                log.trace( "INTERRUPTION DETECTED: {}",interruptionMetadata );

                                graphByThread.put(input.threadId(), graphData.withInterruption(true));

                                onInterruption(input, interruptionMetadata).forEach( approval -> {
                                    emitter.next( new AGUIEvent.ToolCallStartEvent(
                                            approval.toolId(),
                                            approval.toolName(),
                                            null));

                                    emitter.next( new AGUIEvent.ToolCallArgsEvent(
                                            approval.toolId(),
                                            approval.toolArgs()));

                                    emitter.next( new AGUIEvent.ToolCallEndEvent(
                                            approval.toolId() ));

                                });

                            }
                            else {
                                graphByThread.put(input.threadId(), graphData.withInterruption(false));

                                emitter.next(new AGUIEvent.RunFinishedEvent(
                                        input.threadId(),
                                        input.runId()));
                                // Thread CleanUp
                                //graphByThread.remove(input.threadId());
                                //var tag = saver.release( runnableConfig );
                                //log.debug( "thread '{}' released", tag.threadId() );
                            }

                        })).join();

            }
            catch( Exception e ) {
                emitter.error(e);
            }

        });
    }

}
