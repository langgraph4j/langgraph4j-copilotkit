package org.bsc.langgraph4j.agui.sdk;

import com.agui.community.core.agent.RunAgentInput;
import com.agui.community.core.event.*;
import com.agui.community.core.message.Role;
import org.bsc.langgraph4j.*;
import org.bsc.langgraph4j.action.InterruptionMetadata;
import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.streaming.StreamingOutput;
import org.bsc.langgraph4j.utils.TryFunction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.Optional.ofNullable;

public abstract class AGUIAbstractLangGraphAgent implements LG4JLoggable {


    private final Map<String, GraphData> graphByThread = new ConcurrentHashMap<>();
    private final AtomicReference<String> streamingId = new AtomicReference<>();

    protected abstract GraphData buildStateGraph() throws GraphStateException;

    protected abstract GraphInput buildGraphInput(RunAgentInput input, boolean resume);

    protected abstract <S extends AgentState> List<Approval> onInterruption(RunAgentInput input, InterruptionMetadata<S> state);

    protected String newMessageId() {
        return String.valueOf(System.currentTimeMillis());
    }

    protected RunnableConfig buildRunnableConfig(RunAgentInput input) {
        return RunnableConfig.builder()
                .threadId(input.threadId())
                .build();

    }

    protected Collection<? extends Event> nodeOutputToEvents(RunAgentInput input, NodeOutput<? extends AgentState> output) {        return List.of();
    }

    public final RunErrorEvent toErrorEvent(Throwable error) {
        return new RunErrorEvent(ofNullable(error.getMessage()).orElseGet(error::toString));
    }

    public final Flux<? extends Event> run(RunAgentInput input) {

        final var graphData = graphByThread.computeIfAbsent(input.threadId(),
                TryFunction.Try(k -> buildStateGraph()));

        try {

            final var agent = graphData.compiledGraph();

            final var runnableConfig = buildRunnableConfig(input);

            final GraphInput graphInput = buildGraphInput(input, graphData.interruption());

            final var outputFlux = Flux.<Event>create(emitter -> {

                agent.stream(graphInput, runnableConfig)
                        .forEachAsync(event -> {

                            if (event instanceof StreamingOutput<? extends AgentState> output) {
                                var messageId = streamingId.get();
                                if (messageId == null) {
                                    log.trace("STREAMING START");
                                    messageId = streamingId.updateAndGet(v -> newMessageId());
                                    emitter.next( new TextMessageStartEvent(messageId, Role.ASSISTANT));
                                    //continue;
                                    return;
                                }
                                if (output.isStreamingEnd()) { // is streaming out ended
                                    log.trace("STREAMING END");
                                    streamingId.set(null);
                                    emitter.next(new TextMessageEndEvent(messageId));
                                    //continue;
                                    return;
                                }

                                if (output.chunk() == null || output.chunk().isEmpty()) {
                                    log.trace("STREAMING CHUNK IS EMPTY");
                                } else {
                                    log.trace("{}", output.chunk());
                                    emitter.next(new TextMessageContentEvent(messageId, output.chunk()));
                                }
                            } else {

                                log.trace("NEXT:\n{}", event);
                                nodeOutputToEvents(input, event).forEach(emitter::next);
                            }

                        })
                        .thenApply(GraphResult::from)
                        .thenAccept(result -> {

                            log.trace("COMPLETE:\n{}", result);

                            if (result.isInterruptionMetadata()) {

                                final var interruptionMetadata = result.asInterruptionMetadata();

                                log.trace("INTERRUPTION DETECTED: {}", interruptionMetadata);

                                graphByThread.put(input.threadId(), graphData.withInterruption(true));

                                onInterruption(input, interruptionMetadata).forEach(approval -> {
                                    final var messageId = newMessageId();

                                    emitter.next(new ToolCallStartEvent(
                                            approval.toolId(),
                                            approval.toolName(),
                                            messageId,
                                            null,
                                            null
                                    ));

                                    emitter.next(new ToolCallArgsEvent(
                                            approval.toolArgs(),
                                            approval.toolId()
                                    ));

                                    emitter.next(new ToolCallEndEvent(approval.toolId()));

                                });

                            } else {
                                graphByThread.put(input.threadId(), graphData.withInterruption(false));

                                // Thread CleanUp
                                //graphByThread.remove(input.threadId());
                                //var tag = saver.release( runnableConfig );
                                //log.debug( "thread '{}' released", tag.threadId() );

                            }
                        }).whenComplete((ignored, throwable) -> {
                            if (throwable != null) {
                                log.error("Error during graph execution", throwable);
                                if( agent.compileConfig.checkpointSaver().isPresent() ) {
                                    try {
                                        agent.compileConfig.checkpointSaver().get().releaseOnError(runnableConfig, new Exception(throwable));
                                    } catch (Exception e) {
                                        log.error("Error releasing graph execution on error", e);
                                    }
                                }
                                // The SSE controller must receive the error so it can
                                // serialize a terminal RUN_ERROR event for the client.
                                emitter.next(toErrorEvent(throwable));
                            }
                            emitter.complete();
                        });


            });
            return Mono.<Event>just(
                            new RunStartedEvent(input.threadId(), input.runId())
                    )
                    .concatWith(outputFlux.subscribeOn(Schedulers.immediate()))
                    .concatWith(
                            Mono.<Event>just(
                                    new RunFinishedEvent(input.threadId(), input.runId())));

        } catch (Exception e) {
            return Flux.error(e);
        }
    }


}
