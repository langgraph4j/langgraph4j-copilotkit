package org.bsc.langgraph4j.agui.sdk;

import com.agui.community.core.agent.RunAgentInput;
import com.agui.community.core.event.*;
import com.agui.community.core.interrupt.SuccessOutcome;
import org.bsc.langgraph4j.CompileConfig;
import org.bsc.langgraph4j.GraphInput;
import org.bsc.langgraph4j.GraphRepresentation;
import org.bsc.langgraph4j.GraphStateException;
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

import static org.bsc.langgraph4j.utils.CollectionsUtils.lastOf;

public class AGUIAgentExecutor extends AGUIAbstractLangGraphAgent {

    private final MemorySaver saver = new MemorySaver();

    @Override
    protected GraphData buildStateGraph() throws GraphStateException {

//        var model = ofNullable(System.getenv("OPENAI_API_KEY"))
//                .map( key -> AiModel.OPENAI.chatModel("gpt-4o-mini"))
//                .orElseGet( () -> AiModel.OLLAMA.chatModel("qwen3.5") );

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

        return new GraphData(agent.compile(compileConfig));
    }

    @Override
    protected GraphInput buildGraphInput(RunAgentInput input, boolean resume) {

        var lastUserMessage = lastOf(input.messages())
                .map(com.agui.community.core.message.Message::content)
                .orElseThrow(() -> new IllegalStateException("last user message not found"));

        log.debug("LAST USER MESSAGE: {}", lastUserMessage);

        return (resume) ?
                GraphInput.resume(Map.of(AgentEx.APPROVAL_RESULT, lastUserMessage)) :
                GraphInput.args(Map.of("messages", new UserMessage(lastUserMessage)));

    }

    @Override
    protected <S extends AgentState> AGUINodeOutput<S> onInterruption(RunAgentInput input, InterruptionMetadata<S> interruptionMetadata) {

        final var messages = interruptionMetadata.state().<List<Message>>value("messages")
                .orElseThrow(() -> new IllegalStateException("messages not found into given state"));

        final var outputBuilder = AGUINodeOutput.<S>builder();

        lastOf(messages)
                .flatMap(MessageUtil::asAssistantMessage)
                .filter(AssistantMessage::hasToolCalls)
                .map(AssistantMessage::getToolCalls)
                .ifPresent(toolCalls ->
                        toolCalls.forEach(toolCall -> {
                            var toolCallId = toolCall.id().isBlank() ?
                                    UUID.randomUUID().toString() :
                                    toolCall.id();

                            outputBuilder.addEvent(new ToolCallStartEvent(
                                    toolCallId,
                                    toolCall.name(),
                                    null,
                                    System.currentTimeMillis(),
                                    null
                            ));
                            outputBuilder.addEvent(new ToolCallArgsEvent(
                                    toolCallId,
                                    toolCall.arguments(),
                                    System.currentTimeMillis(),
                                    null
                            ));
                            outputBuilder.addEvent(new ToolCallEndEvent(
                                    toolCallId,
                                    System.currentTimeMillis(),
                                    null
                            ));
                        }));
        //StateDeltaEvent
        return outputBuilder
                .addEvent(new RunFinishedEvent(
                        input.threadId(),
                        input.runId(),
                        new SuccessOutcome(),
                        Map.of("resume", true),
                        System.currentTimeMillis(),
                        null))
                .build(interruptionMetadata.nodeId(), interruptionMetadata.state());

    }
}
