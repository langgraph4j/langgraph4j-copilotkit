package org.bsc.langgraph4j.agui;
import org.bsc.langgraph4j.CompileConfig;
import org.bsc.langgraph4j.GraphStateException;
import org.bsc.langgraph4j.RunnableConfig;
import org.bsc.langgraph4j.StateGraph;
import org.bsc.langgraph4j.checkpoint.BaseCheckpointSaver;
import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.streaming.StreamingOutput;
import reactor.core.publisher.Flux;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AGUILangGraphAgent implements AGUIAgent {
    static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AGUILangGraphAgent.class);

    final BaseCheckpointSaver saver;

    protected AGUILangGraphAgent(BaseCheckpointSaver saver ) {
        this.saver = saver;
    }

    abstract StateGraph<? extends AgentState> buildStateGraph() throws GraphStateException;

    abstract Map<String,Object> buildGraphInput( AGUIType.RunAgentInput input );

    @Override
    public final Flux<? extends AGUIEvent> run(AGUIType.RunAgentInput input) {

        return Flux.create( emitter -> {
            try {
                var compileConfig = CompileConfig.builder()
                        .checkpointSaver(saver)
                        .build();

                var agent = buildStateGraph()
                        .compile(compileConfig);

                emitter.next(new AGUIEvent.RunStartedEvent(
                        input.threadId(),
                        input.runId()));

                var messageId = String.valueOf(System.currentTimeMillis());

                var runnableConfig = RunnableConfig.builder()
                        .threadId(input.threadId())
                        .build();

                var isEmittingChunk = new AtomicBoolean(false);

                agent.stream( buildGraphInput(input), runnableConfig )
                        .async()
                        .forEachAsync(event -> {

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
                                    /*
                                if( event.isEND() && !isEndEmittingChunk ) {
                                    var newMessageId = String.valueOf(System.currentTimeMillis());
                                    emitter.next(new AGUIEvent.TextMessageStartEvent(newMessageId));

                                    var text = event.state().lastMessage()
                                            .map(Objects::toString)
                                            .orElse("NONE");
                                    emitter.next(new AGUIEvent.TextMessageContentEvent(
                                            newMessageId,
                                            "END"));

                                    emitter.next(new AGUIEvent.TextMessageEndEvent(newMessageId));
                                }
                                */
                                }
                            }

                        }).thenAccept( result -> {
                            log.trace( "COMPLETE:\n{}", result);

                            emitter.next(new AGUIEvent.RunFinishedEvent(
                                    input.threadId(),
                                    input.runId()));

                        }).join();

            }
            catch( Exception e ) {
                emitter.error(e);
            }

        });
    }

}
