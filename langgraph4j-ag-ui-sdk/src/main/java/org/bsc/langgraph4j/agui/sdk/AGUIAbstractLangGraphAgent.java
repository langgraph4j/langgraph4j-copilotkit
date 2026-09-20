package org.bsc.langgraph4j.agui.sdk;

import com.agui.community.core.agent.RunAgentInput;
import com.agui.community.core.event.*;
import com.agui.community.core.message.Role;
import org.bsc.langgraph4j.*;
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

import static java.util.Optional.ofNullable;

public abstract class AGUIAbstractLangGraphAgent implements LG4JLoggable {

    private final Map<String, CompiledGraph<? extends AgentState>> graphByThread = new ConcurrentHashMap<>();
    private final AtomicReference<String> streamingId = new AtomicReference<>();

    protected abstract CompiledGraph<? extends AgentState> buildStateGraph() throws GraphStateException;

    protected abstract GraphInput buildGraphInput(RunAgentInput input);

    protected abstract <S extends AgentState> AGUINodeOutput<S> onCompletion(RunAgentInput input, GraphResult state);

    protected String newMessageId() {
        return String.valueOf(System.currentTimeMillis());
    }

    protected RunnableConfig buildRunnableConfig(RunAgentInput input) {
        return RunnableConfig.builder()
                .threadId(input.threadId())
                .build();
    }

    protected Collection<? extends Event> nodeOutputToEvents(RunAgentInput input, NodeOutput<? extends AgentState> output) {

        if (output instanceof AGUINodeOutput<? extends AgentState> aguiNodeOutput) {
            return aguiNodeOutput.events();
        }
        return List.of();
    }

    protected Collection<? extends Event> initEvents(RunAgentInput input) {
        return List.of();
    }

    public final RunErrorEvent toErrorEvent(Throwable error) {
        return new RunErrorEvent(ofNullable(error.getMessage()).orElseGet(error::toString));
    }

    @SuppressWarnings("unchecked")
    public final <S extends AgentState> Optional<CompiledGraph<S>> currentGraph(RunAgentInput input) {
        return ofNullable( graphByThread.get(input.threadId()))
                .map( g -> (CompiledGraph<S>)g);
    }

    public final Flux<? extends Event> run(RunAgentInput input) {

        final var agent = graphByThread.computeIfAbsent(input.threadId(),
                TryFunction.Try(k -> buildStateGraph()));

        try {


            final var runnableConfig = buildRunnableConfig(input);

            final GraphInput graphInput = buildGraphInput(input);

            final var outputFlux = Flux.<Event>create(emitter -> {

                agent.stream(graphInput, runnableConfig)
                        .forEachAsync(event -> {

                            if (event instanceof StreamingOutput<? extends AgentState> output) {
                                var messageId = streamingId.get();
                                if (messageId == null) {
                                    log.trace("STREAMING START");
                                    messageId = streamingId.updateAndGet(v -> newMessageId());
                                    emitter.next(new TextMessageStartEvent(
                                            messageId,
                                            Role.ASSISTANT,
                                            System.currentTimeMillis(),
                                            null));
                                    //continue;
                                    return;
                                }
                                if (output.isStreamingEnd()) { // is streaming out ended
                                    log.trace("STREAMING END");
                                    streamingId.set(null);
                                    emitter.next(new TextMessageEndEvent(
                                            messageId,
                                            System.currentTimeMillis(),
                                            null));
                                    //continue;
                                    return;
                                }

                                if (output.chunk() == null || output.chunk().isEmpty()) {
                                    log.trace("STREAMING CHUNK IS EMPTY");
                                } else {
                                    log.trace("{}", output.chunk());
                                    emitter.next(new TextMessageContentEvent(
                                            messageId,
                                            output.chunk(),
                                            System.currentTimeMillis(),
                                            null));
                                }
                            } else {

                                log.trace("NEXT:\n{}", event);
                                nodeOutputToEvents(input, event).forEach(emitter::next);
                            }

                        })
                        .thenApply(GraphResult::from)
                        .thenAccept(result -> {

                            log.trace("COMPLETE:\n{}", result);

                            final var output = onCompletion(input, result);
                            nodeOutputToEvents(input, output).forEach(emitter::next);

                        }).whenComplete((ignored, throwable) -> {
                            if (throwable != null) {
                                log.error("Error during graph execution", throwable);
                                if (agent.compileConfig.checkpointSaver().isPresent()) {
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
                            new RunStartedEvent(
                                    input.threadId(),
                                    input.runId(),
                                    null,
                                    null,
                                    System.currentTimeMillis(),
                                    null)
                    )
                    .concatWith(outputFlux.subscribeOn(Schedulers.immediate()));


        } catch (Exception e) {
            return Flux.error(e);
        }
    }


}
