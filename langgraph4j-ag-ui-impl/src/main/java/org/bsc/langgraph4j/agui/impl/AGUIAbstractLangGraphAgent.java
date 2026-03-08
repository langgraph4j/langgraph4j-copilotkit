package org.bsc.langgraph4j.agui.impl;
import org.bsc.langgraph4j.*;
import org.bsc.langgraph4j.action.InterruptionMetadata;
import org.bsc.langgraph4j.agent.AgentEx;
import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.streaming.StreamingOutput;
import org.bsc.langgraph4j.utils.TryFunction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

public abstract class AGUIAbstractLangGraphAgent implements AGUIAgent, LG4JLoggable {

    private final Map<String, GraphData> graphByThread = new ConcurrentHashMap<>();

    protected abstract GraphData buildStateGraph() throws GraphStateException;

    protected abstract Map<String,Object> buildGraphInput( AGUIType.RunAgentInput input );

    protected abstract <State extends AgentState> List<Approval> onInterruption(AGUIType.RunAgentInput input, InterruptionMetadata<State> state );

    protected Optional<String> nodeOutputToText( NodeOutput<? extends AgentState> output ) {
        return Optional.empty();
    }

    protected Collection<?extends AGUIEvent> nodeOutputToEvents( AGUIType.RunAgentInput input, NodeOutput<? extends AgentState> output ) {
        var text = nodeOutputToText(output);
        if( text.isEmpty() ) {
            return List.of();
        }

        var messageId = newMessageId();
        return List.of(
                new AGUIEvent.TextMessageStartEvent(messageId),
                new AGUIEvent.TextMessageContentEvent(messageId,text.get()),
                new AGUIEvent.TextMessageEndEvent(messageId)
        );
    }

    private final AtomicReference<String> streamingId = new AtomicReference<>();

    protected String newMessageId() {
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
                            continue;
                        }

                        if(output.isStreamingEnd()) {
                            log.trace( "STREAMING END");
                            streamingId.set(null);
                            emitter.next(new AGUIEvent.TextMessageEndEvent(messageId));
                            continue;
                        }

                        if( output.chunk() == null || output.chunk().isEmpty()) {
                            log.trace( "STREAMING CHUNK IS EMPTY");
                        }
                        else {
                            log.trace( "{}", output.chunk());
                            emitter.next(new AGUIEvent.TextMessageContentEvent(messageId, output.chunk()));
                        }
                    } else {

                            log.trace( "NEXT:\n{}", event);
                            nodeOutputToEvents(input, event).forEach( emitter::next );
                   }

                }

                final var result = GraphResult.from(outputGenerator);

                log.trace( "COMPLETE:\n{}", result);

                if( result.isInterruptionMetadata() ) {

                    final var interruptionMetadata = result.asInterruptionMetadata();

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
