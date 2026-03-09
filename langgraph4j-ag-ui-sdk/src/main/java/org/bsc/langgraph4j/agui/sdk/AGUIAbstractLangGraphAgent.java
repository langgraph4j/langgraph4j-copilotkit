package org.bsc.langgraph4j.agui.sdk;

import com.agui.core.agent.RunAgentParameters;
import com.agui.core.event.BaseEvent;
import com.agui.core.message.BaseMessage;
import com.agui.core.message.Role;
import com.agui.server.EventFactory;
import org.bsc.langgraph4j.*;
import org.bsc.langgraph4j.action.InterruptionMetadata;
import org.bsc.langgraph4j.agent.AgentEx;
import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.streaming.StreamingOutput;
import org.bsc.langgraph4j.utils.TryFunction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import static org.bsc.langgraph4j.utils.CollectionsUtils.lastOf;

public abstract class AGUIAbstractLangGraphAgent implements LG4JLoggable {


    private final Map<String, GraphData> graphByThread = new ConcurrentHashMap<>();
    private final AtomicReference<String> streamingId = new AtomicReference<>();

    protected abstract GraphData buildStateGraph() throws GraphStateException;

    protected abstract GraphInput buildGraphInput(RunAgentParameters input);

    protected abstract <S extends AgentState> List<Approval> onInterruption(RunAgentParameters input, InterruptionMetadata<S> state);

    protected String newMessageId() {
        return String.valueOf(System.currentTimeMillis());
    }

    protected Optional<String> nodeOutputToText(NodeOutput<? extends AgentState> output) {
        return Optional.empty();
    }


    protected Collection<? extends BaseEvent> nodeOutputToEvents(RunAgentParameters input, NodeOutput<? extends AgentState> output) {
        return nodeOutputToText(output)
                .map(text -> {
                    var messageId = newMessageId();

                    return List.of(
                            EventFactory.textMessageStartEvent(messageId, Role.assistant.name()),
                            EventFactory.textMessageContentEvent(messageId, text),
                            EventFactory.textMessageEndEvent(messageId));
                })
                .orElseGet(List::of);
    }



    public final Flux<? extends BaseEvent> run(RunAgentParameters input) {

        final var graphData = graphByThread.computeIfAbsent(input.getThreadId(),
                TryFunction.Try(k -> buildStateGraph()));

        try {

            var agent = graphData.compiledGraph();

            var runnableConfig = RunnableConfig.builder()
                    .threadId(input.getThreadId())
                    .build();

            final GraphInput graphInput ;

            if( graphData.interruption() ) {

                var lastResultMessage = lastOf(input.getMessages())
                        .map(BaseMessage::getContent)
                        .orElseThrow( () -> new IllegalStateException( "last result message not found after interruption") );

                //runnableConfig = agent.updateState( runnableConfig, Map.of(AgentEx.APPROVAL_RESULT_PROPERTY, lastResultMessage ));

                graphInput = GraphInput.resume(Map.of(AgentEx.APPROVAL_RESULT_PROPERTY, lastResultMessage )); // resume graph
            }
            else {
                graphInput = buildGraphInput(input);
            }


            final var outputGenerator = agent.stream(graphInput, runnableConfig);

            var outputFlux = Flux.<BaseEvent>create(emitter -> {

                for (var event : outputGenerator) {

                    if (event instanceof StreamingOutput<? extends AgentState> output) {
                        var messageId = streamingId.get();
                        if(messageId==null) {
                            log.trace( "STREAMING START");
                            messageId = streamingId.updateAndGet( v -> newMessageId() );
                            emitter.next(EventFactory.textMessageStartEvent(messageId, Role.assistant.name()));
                            continue;
                        }
                        if( output.isEnd() ) { // is streaming out ended
                            log.trace("STREAMING END");
                            streamingId.set(null);
                            emitter.next(EventFactory.textMessageEndEvent(messageId));
                            continue;
                        }

                        if( output.chunk() == null || output.chunk().isEmpty()) {
                            log.trace( "STREAMING CHUNK IS EMPTY");
                        }
                        else {
                            log.trace( "{}", output.chunk());
                            emitter.next(EventFactory.textMessageContentEvent(messageId, output.chunk()));
                        }
                    } else {

                        log.trace( "NEXT:\n{}", event);
                        nodeOutputToEvents(input, event).forEach( emitter::next );
                    }

                }

                final var result = GraphResult.from(outputGenerator);

                log.trace("COMPLETE:\n{}", result);

                if (result.isInterruptionMetadata()) {

                    final var interruptionMetadata = result.asInterruptionMetadata();

                    log.trace("INTERRUPTION DETECTED: {}", interruptionMetadata);

                    graphByThread.put(input.getThreadId(), graphData.withInterruption(true));

                    onInterruption(input, interruptionMetadata).forEach(approval -> {
                        final var messageId = newMessageId();

                        emitter.next(EventFactory.toolCallStartEvent(
                                messageId,
                                approval.toolName(),
                                approval.toolId()
                                ));

                        emitter.next(EventFactory.toolCallArgsEvent(
                                approval.toolArgs(),
                                approval.toolId()
                                ));

                        emitter.next(EventFactory.toolCallEndEvent(
                                approval.toolId()));

                    });

                } else {
                    graphByThread.put(input.getThreadId(), graphData.withInterruption(false));

                    // Thread CleanUp
                    //graphByThread.remove(input.threadId());
                    //var tag = saver.release( runnableConfig );
                    //log.debug( "thread '{}' released", tag.threadId() );

                }

                emitter.complete();

            });
            return Mono.<BaseEvent>just(
                            EventFactory.runStartedEvent(input.getThreadId(), input.getRunId())
                    )
                    .concatWith(outputFlux.subscribeOn(Schedulers.immediate()))
                    .concatWith(
                            Mono.<BaseEvent>just(
                                    EventFactory.runFinishedEvent(input.getThreadId(), input.getRunId())));

        } catch (Exception e) {
            return Flux.error(e);
        }
    }


}
