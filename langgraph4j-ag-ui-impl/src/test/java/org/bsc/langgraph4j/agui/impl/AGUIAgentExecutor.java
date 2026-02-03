package org.bsc.langgraph4j.agui.impl;

import org.bsc.langgraph4j.CompileConfig;
import org.bsc.langgraph4j.GraphRepresentation;
import org.bsc.langgraph4j.GraphStateException;
import org.bsc.langgraph4j.NodeOutput;
import org.bsc.langgraph4j.action.InterruptionMetadata;
import org.bsc.langgraph4j.checkpoint.MemorySaver;
import org.bsc.langgraph4j.spring.ai.agentexecutor.AgentExecutorEx;
import org.bsc.langgraph4j.spring.ai.util.MessageUtil;
import org.bsc.langgraph4j.state.AgentState;

import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;

import java.util.*;

import static java.util.Optional.ofNullable;
import static org.bsc.langgraph4j.utils.CollectionsUtils.lastOf;

//@org.springframework.stereotype.Component("AGUIAgent")
public class AGUIAgentExecutor extends AGUIAbstractLangGraphAgent {

    private final MemorySaver saver = new MemorySaver();

    public AGUIAgentExecutor() {}

    @Override
    protected GraphData buildStateGraph() throws GraphStateException {

        var model = ofNullable(System.getenv("OPENAI_API_KEY"))
                .map( key -> AIModel.OPENAI_GPT_4O_MINI.model.get())
                .orElseGet( () ->
                    ofNullable( System.getenv("GITHUB_MODELS_TOKEN") )
                            .map( key -> AIModel.GITHUB_MODELS_GPT_4O_MINI.model.get() )
                            .orElseGet( AIModel.OLLAMA_QWEN2_5_7B.model ));

        var agent =  AgentExecutorEx.builder()
                .chatModel(model, true)
                .toolsFromObject(new Tools())
                .approvalOn( "sendEmail",
                         (nodeId, state ) ->
                                        InterruptionMetadata.builder( nodeId, state )
                                        .build()
                )
                .build();

        log.info( "REPRESENTATION:\n{}",
                agent.getGraph(GraphRepresentation.Type.PLANTUML, "Agent Executor", false).content()
        );

        var compileConfig = CompileConfig.builder().checkpointSaver(saver).build();

        return new GraphData( agent.compile(compileConfig) ) ;
    }

    @Override
    protected Map<String, Object> buildGraphInput(AGUIType.RunAgentInput input) {

        var lastUserMessage = input.lastUserMessage()
                .map(AGUIMessage.TextMessage::content)
                .orElseThrow( () -> new IllegalStateException("last user message not found"));

        log.debug( "LAST USER MESSAGE: {}", lastUserMessage );

        return Map.of("messages", new UserMessage(lastUserMessage));
    }

    @Override
    protected <State extends AgentState> List<Approval> onInterruption(AGUIType.RunAgentInput input, InterruptionMetadata<State> state ) {

        var messages = state.state().<List<Message>>value("messages")
                .orElseThrow( () -> new IllegalStateException("messages not found into given state"));

        return lastOf(messages)
                .flatMap(MessageUtil::asAssistantMessage)
                .filter(AssistantMessage::hasToolCalls)
                .map(AssistantMessage::getToolCalls)
                .map( toolCalls ->
                    toolCalls.stream().map( toolCall -> {
                        var id = toolCall.id().isBlank() ?
                                UUID.randomUUID().toString() :
                                toolCall.id();
                        return new Approval( id, toolCall.name(), toolCall.arguments() );
                    }).toList()
                )
                .orElseGet(List::of);

    }

    @Override
    protected Optional<String> nodeOutputToText(NodeOutput<? extends AgentState> output) {
        if( output.isEND() | output.isSTART() ) {
            return Optional.empty();
        }

        throw new UnsupportedOperationException("not implemented yet");
    }


    public static void main( String[] argv ) throws Exception {

        var agent = new AGUIAgentExecutor();

        var input = new AGUIType.RunAgentInput(
                "thread1",
                "run1",
                null,
                List.of( AGUIMessage.userMessage( "msg1", """
                    Send a mail to bartolomeo.sorrentino@gmail.com with subjet AG-UI test and an empty body
                """ )),
                null,
                null,
                null );

        agent.run(input).subscribe( event -> log.trace( "{}", event) );

    }
}
