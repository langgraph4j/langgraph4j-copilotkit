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
import reactor.core.publisher.FluxSink;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.Objects.requireNonNull;
import static org.bsc.langgraph4j.utils.CollectionsUtils.lastOf;

public abstract class AGUIAbstractLangGraphAgent implements LG4JLoggable {


    private final Map<String, GraphData> graphByThread = new ConcurrentHashMap<>();

    protected abstract GraphData buildStateGraph() throws GraphStateException;

    protected abstract GraphInput buildGraphInput(RunAgentParameters input);

    protected abstract <S extends AgentState> List<Approval> onInterruption(RunAgentParameters input, InterruptionMetadata<S> state);

    protected abstract Optional<ToolCallRequestData> isToolCallRequest( NodeOutput<? extends AgentState> output );
    protected abstract Optional<ToolCallResultData> isToolCallResult( NodeOutput<? extends AgentState> output );

    protected String newMessageId() {
        return String.valueOf(System.currentTimeMillis());
    }


    class StreamingTracking {
        final AtomicReference<String> currentMessageId = new AtomicReference<>();

        boolean process(RunAgentParameters input, NodeOutput<? extends AgentState> event, FluxSink<BaseEvent> emitter ) {
            if (event instanceof StreamingOutput<? extends AgentState> output) {
                var messageId = currentMessageId.get();
                if (messageId == null) {
                    log.trace("STREAMING START");
                    messageId = currentMessageId.updateAndGet(v -> newMessageId());
                    emitter.next(EventFactory.runStartedEvent(input.getThreadId(), input.getRunId()));
                    emitter.next(EventFactory.textMessageStartEvent(messageId, Role.assistant.name()));
                }

                if (output.chunk() == null || output.chunk().isEmpty()) {
                    log.trace("STREAMING CHUNK IS EMPTY");
                } else {
                    log.trace("{}", output.chunk());
                    emitter.next(EventFactory.textMessageContentEvent(messageId, output.chunk()));
                }
                return true;
            }

            var messageId = currentMessageId.get();
            if (messageId != null) {
                log.trace("STREAMING END");
                emitter.next(EventFactory.textMessageEndEvent(messageId));
                emitter.next(EventFactory.runFinishedEvent(input.getThreadId(), input.getRunId()));
                currentMessageId.set(null);
                return true;
            }

            return false;
        }

    }

    class ToolTracking {
        final AtomicReference<String> currentMessageId = new AtomicReference<>();

        private Collection<? extends BaseEvent> toolRequestEvents( RunAgentParameters input, String messageId, ToolCallRequestData tollCallData ) {
            return List.of(
                    EventFactory.runStartedEvent(input.getThreadId(), input.getRunId()),
                    EventFactory.toolCallStartEvent(
                            requireNonNull(messageId, "messageId cannot be null"),
                            tollCallData.toolName(),
                            tollCallData.toolId()),
                    EventFactory.toolCallArgsEvent(
                            tollCallData.toolArgs(),
                            tollCallData.toolId()),
                    EventFactory.toolCallEndEvent(tollCallData.toolId()));

        }
        protected final Collection<? extends BaseEvent> toolResponseEvents( RunAgentParameters input, String messageId, ToolCallResultData data ) {
            return List.of(
                    EventFactory.toolCallResultEvent(
                            data.toolCallId(),
                            data.content(),
                            requireNonNull(messageId, "messageId cannot be null"),
                            data.role()),
                    EventFactory.runFinishedEvent(input.getThreadId(), input.getRunId()));


        }

        void process(RunAgentParameters input, NodeOutput<? extends AgentState> event, FluxSink<BaseEvent> emitter ) {
            final var toolCallRequestOptional = isToolCallRequest( event);
            if( toolCallRequestOptional.isPresent() ) {
                toolRequestEvents( input,
                        currentMessageId.updateAndGet(v -> newMessageId()),
                        toolCallRequestOptional.get() ).forEach(emitter::next);
                return;
            }

            final var toolCallResultOptional = isToolCallResult( event);
            if( toolCallResultOptional.isPresent() ) {
                toolResponseEvents( input,
                        currentMessageId.get(),
                        toolCallResultOptional.get() ).forEach(emitter::next);
                currentMessageId.set(null);
            }

        }

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

                graphInput = GraphInput.resume(Map.of(AgentEx.APPROVAL_RESULT_PROPERTY, lastResultMessage )); // resume graph
            }
            else {
                graphInput = buildGraphInput(input);
            }

            final var streamingTracking = new StreamingTracking();
            final var toolTracking = new ToolTracking();

            final AtomicReference<String> toolMessageId = new AtomicReference<>();

            final var outputGenerator = agent.stream(graphInput, runnableConfig);

            return Flux.<BaseEvent>create(emitter -> {

                for (var event : outputGenerator) {

                    if( event.isSTART() ) {
                        continue;
                    }

                    if( streamingTracking.process( input, event, emitter ) || event.isEND() )  {
                        continue;
                    }

                    toolTracking.process( input, event, emitter );

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

            })
            //.subscribeOn(Schedulers.immediate());
            ;

            /*
            return Mono.<BaseEvent>just(
                            EventFactory.runStartedEvent(input.getThreadId(), input.getRunId())
                    )
                    .concatWith(outputFlux.subscribeOn(Schedulers.immediate()))
                    .concatWith(
                            Mono.<BaseEvent>just(
                                    EventFactory.runFinishedEvent(input.getThreadId(), input.getRunId())));
            */
        } catch (Exception e) {
            return Flux.error(e);
        }
    }


}
