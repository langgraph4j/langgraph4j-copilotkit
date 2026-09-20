package org.bsc.langgraph4j.agui.sdk;

import com.agui.community.core.agent.RunAgentInput;
import com.agui.community.core.event.JsonPatchOperation;
import com.agui.community.core.event.RunFinishedEvent;
import com.agui.community.core.event.StateDeltaEvent;
import com.agui.community.core.event.ToolCallChunkEvent;
import com.agui.community.core.interrupt.Interrupt;
import com.agui.community.core.interrupt.InterruptOutcome;
import com.agui.community.core.interrupt.SuccessOutcome;
import org.bsc.langgraph4j.*;
import org.bsc.langgraph4j.action.InterruptionMetadata;
import org.bsc.langgraph4j.agent.AgentEx;
import org.bsc.langgraph4j.checkpoint.MemorySaver;
import org.bsc.langgraph4j.spring.ai.agentexecutor.AgentExecutorEx;
import org.bsc.langgraph4j.spring.ai.util.MessageUtil;
import org.bsc.langgraph4j.state.AgentState;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.bsc.langgraph4j.GraphDefinition.END;
import static org.bsc.langgraph4j.utils.CollectionsUtils.lastOf;

public class AGUIAgentExecutorINTERRUPT extends AGUIAbstractLangGraphAgent {

    private final MemorySaver saver = new MemorySaver();

    @Override
    protected CompiledGraph<? extends AgentState> buildStateGraph() throws GraphStateException {

        var agent = AgentExecutorEx.builder()
                .chatModel(AiModel.OLLAMA.chatModel("qwen3.5"))
                .emitStreamingEnd(true)
                .streaming(true)
                .toolsFromObject(new Tools())
                .approvalOn("sendEmail",
                        (nodeId, state) ->
                                InterruptionMetadata.builder(nodeId, state)
                                        .build()
                )
                .build();

        log.info("REPRESENTATION:\n{}",
                agent.getGraph(GraphRepresentation.Type.PLANTUML, "Agent Executor", false).content()
        );

        var compileConfig = CompileConfig.builder().checkpointSaver(saver).build();

        return agent.compile(compileConfig);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected GraphInput buildGraphInput(RunAgentInput input) {

        if( !input.resume().isEmpty() ) {

            if( input.resume().size() > 1 ) {
                throw new IllegalStateException("resume more than one message");
            }
            final var resume = input.resume().get(0);

            final var payload = (Map<String,Object>)resume.payload();

            final var approvalResult = payload.get(AgentEx.APPROVAL_RESULT);

            return GraphInput.resume(Map.of(AgentEx.APPROVAL_RESULT, approvalResult));

        }

        var lastUserMessage = lastOf(input.messages())
                .map(com.agui.community.core.message.Message::content)
                .orElseThrow(() -> new IllegalStateException("last user message not found"));

        log.debug("LAST USER MESSAGE: {}", lastUserMessage);

        return GraphInput.args(Map.of("messages", new UserMessage(lastUserMessage)));
    }

    @Override
    protected <S extends AgentState> AGUINodeOutput<S> onCompletion(RunAgentInput input, GraphResult result) {

        final var outputBuilder = AGUINodeOutput.<S>builder();

        final var agent = this.<S>currentGraph(input).orElseThrow(() -> new IllegalStateException("current graph not found"));

        if (result.isInterruptionMetadata()) {

            final var interruptionMetadata = result.<S>asInterruptionMetadata();

            log.trace("INTERRUPTION DETECTED: {}", interruptionMetadata);

            final var messages = interruptionMetadata.state().<List<Message>>value("messages")
                    .orElseThrow(() -> new IllegalStateException("messages not found into given state"));


            var interrupts = lastOf(messages)
                    .flatMap(MessageUtil::asAssistantMessage)
                    .filter(AssistantMessage::hasToolCalls)
                    .map(AssistantMessage::getToolCalls)
                    .map(toolCalls ->
                            toolCalls.stream().map(toolCall -> {
                                var toolCallId = toolCall.id().isBlank() ?
                                        UUID.randomUUID().toString() :
                                        toolCall.id();

                                outputBuilder.addEvent(new ToolCallChunkEvent(
                                        toolCallId,
                                        toolCall.name(),
                                        null,
                                        toolCall.arguments(),
                                        System.currentTimeMillis(),
                                        null
                                ));
                                return new Interrupt("int-1",
                                        interruptionMetadata.reason().orElse(""),
                                        "",
                                        toolCallId,
                                        null,
                                        null,
                                        Map.of("to", "", "subject", "", "body", ""));
                            }).toList());
            return outputBuilder
                    .addEvent(new RunFinishedEvent(
                            input.threadId(),
                            input.runId(),
                            new InterruptOutcome(interrupts.orElseGet(List::of)),
                            null,
                            System.currentTimeMillis(),
                            null))
                    .build(interruptionMetadata.nodeId(), interruptionMetadata.state());
        }
        else {
            return outputBuilder
                    .addEvent(new RunFinishedEvent(
                            input.threadId(),
                            input.runId(),
                            new SuccessOutcome(),
                            null,
                            System.currentTimeMillis(),
                            null))
                    .build(END, agent.stateGraph.getStateFactory().apply( result.asStateDataOrLastCheckpointStateData() ));
        }

    }
}
