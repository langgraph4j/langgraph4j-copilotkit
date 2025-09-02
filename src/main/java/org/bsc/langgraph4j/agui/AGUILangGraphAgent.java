package org.bsc.langgraph4j.agui;
import org.bsc.async.AsyncGenerator;
import org.bsc.langgraph4j.*;
import org.bsc.langgraph4j.action.InterruptionMetadata;
import org.bsc.langgraph4j.agent.AgentEx;
import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.streaming.StreamingOutput;
import org.bsc.langgraph4j.utils.TryFunction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.Objects.requireNonNull;

public abstract class AGUILangGraphAgent implements AGUIAgent {

    public record GraphData( CompiledGraph<? extends AgentState> compiledGraph,
                             boolean interruption)
    {
        public GraphData {
            requireNonNull( compiledGraph, "compiledGraph cannot ne bull");
        }

        public GraphData( CompiledGraph<? extends AgentState> compiledGraph) {
            this(compiledGraph, false);
        }

        public GraphData withInterruption( boolean interrupt ) {
            if( this.interruption == interrupt ) {
                return this;
            }
            return new GraphData(compiledGraph, interrupt);
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

    private final Map<String, GraphData> graphByThread = new ConcurrentHashMap<>();

    protected abstract GraphData buildStateGraph() throws GraphStateException;

    protected abstract Map<String,Object> buildGraphInput( AGUIType.RunAgentInput input );

    protected abstract <State extends AgentState> List<Approval> onInterruption(AGUIType.RunAgentInput input, InterruptionMetadata<State> state );

    protected abstract  Optional<String> nodeOutputToText( NodeOutput<? extends AgentState> output );

    private final AtomicReference<String> streamingId = new AtomicReference<>();

    private String newMessageId() {
        return String.valueOf(System.currentTimeMillis());
    }

    @Override
    public final Flux<? extends AGUIEvent> run(AGUIType.RunAgentInput input) {

        final var graphData = graphByThread.computeIfAbsent(input.threadId(),
                TryFunction.Try(k -> buildStateGraph() ));

        try {

            var agent = graphData.compiledGraph();

            var runnableConfig = RunnableConfig.builder()
                    .threadId(input.threadId())
                    .build();


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

            final var outputGenerator = agent.stream( graphInput, runnableConfig );

            var outputFlux = Flux.<AGUIEvent>create( emitter -> {

                for( var event : outputGenerator ) {

                    if (event instanceof StreamingOutput<? extends AgentState> output) {
                        var messageId = streamingId.get();
                        if(messageId==null) {
                            log.trace( "STREAMING START");
                            messageId = streamingId.updateAndGet( v -> newMessageId() );
                            emitter.next(new AGUIEvent.TextMessageStartEvent(messageId));
                        }

                        if( output.chunk() == null || output.chunk().isEmpty()) {
                            log.trace( "STREAMING CHUNK IS EMPTY");
                        }
                        else {
                            log.trace( "{}", output.chunk());
                            emitter.next(new AGUIEvent.TextMessageContentEvent(messageId, output.chunk()));
                        }
                    } else {

                        var messageId = streamingId.get();

                        if( messageId == null ) {
                            log.trace( "NEXT:\n{}", event);

                            messageId = newMessageId();
                            emitter.next(new AGUIEvent.TextMessageStartEvent(messageId));
                            var text = nodeOutputToText(event);
                            if( text.isPresent() ) {
                                emitter.next(new AGUIEvent.TextMessageContentEvent(messageId,text.get()));
                            }
                        }
                        else {
                            log.trace("STREAMING END");
                            streamingId.set(null);
                        }
                        emitter.next(new AGUIEvent.TextMessageEndEvent(messageId));
                    }

                }

                final var result = AsyncGenerator.resultValue(outputGenerator).orElse(null);

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

                    // Thread CleanUp
                    //graphByThread.remove(input.threadId());
                    //var tag = saver.release( runnableConfig );
                    //log.debug( "thread '{}' released", tag.threadId() );

                }

                emitter.complete();

            });
            return Mono.<AGUIEvent>just(
                            new AGUIEvent.RunStartedEvent( input.threadId(), input.runId())
                    )
                    .concatWith( outputFlux.subscribeOn(Schedulers.single()) )
                    .concatWith(
                            Mono.<AGUIEvent>just(
                                    new AGUIEvent.RunFinishedEvent(input.threadId(), input.runId() )) );



        }
        catch( Exception e ) {
            return Flux.error(e);
        }

    }

}
