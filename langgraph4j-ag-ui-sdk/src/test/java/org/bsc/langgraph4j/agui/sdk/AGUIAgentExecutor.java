package org.bsc.langgraph4j.agui.sdk;

import com.agui.core.agent.RunAgentParameters;
import com.agui.core.message.BaseMessage;
import io.modelcontextprotocol.client.McpSyncClient;
import org.bsc.langgraph4j.*;
import org.bsc.langgraph4j.action.InterruptionMetadata;
import org.bsc.langgraph4j.checkpoint.MemorySaver;
import org.bsc.langgraph4j.spring.ai.agentexecutor.AgentExecutorEx;
import org.bsc.langgraph4j.state.AgentState;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.ToolResponseMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.Optional.ofNullable;
import static org.bsc.langgraph4j.utils.CollectionsUtils.lastOf;

@Service
public class AGUIAgentExecutor extends  AGUIAbstractLangGraphAgent {
    private final List<McpSyncClient> mcpSyncClient;
    private final MemorySaver saver = new MemorySaver();

    public AGUIAgentExecutor( List<McpSyncClient> mcpSyncClient ) {
        this.mcpSyncClient = mcpSyncClient;
    }


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
                .tools(SyncMcpToolCallbackProvider.builder()
                        .mcpClients(mcpSyncClient)
                        .build())
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

        var compileConfig = CompileConfig.builder()
                //.interruptAfter("get_time")
                .checkpointSaver(saver)
                .build();

        return new GraphData( agent.compile(compileConfig) ) ;
    }

    @Override
    protected GraphInput buildGraphInput(RunAgentParameters input) {

        var lastUserMessage = lastOf(input.getMessages())
                .map(BaseMessage::getContent)
                .orElseThrow( () -> new IllegalStateException("last user message not found"));

        log.debug( "LAST USER MESSAGE: {}", lastUserMessage );

        return  GraphInput.args(Map.of("messages", new UserMessage(lastUserMessage)));

    }

    @Override
    protected Optional<ToolCallData> isToolCall(InterruptionMetadata<? extends AgentState> output) {
        if( output.state() instanceof AgentExecutorEx.State state ) {
            final var lastMessageOptional = state.lastMessage();

            if( lastMessageOptional.isPresent() ) {

                final var lastMessage = lastMessageOptional.get();

                final var response = isToolCallResponse(lastMessage);
                if( response.isPresent() ) {

                    var previousMessage = state.lastMinus(1);
                    if( previousMessage.isPresent() ) {
                        final var request = isToolCallRequest(previousMessage.get());

                        if( request.isPresent() ) {
                            return Optional.of(new ToolCallData( request.get(), response.get() ));
                        }
                    }
                }
                else {
                    final var request = isToolCallRequest(lastMessage);
                    if( request.isPresent() ) {
                        return Optional.of(new ToolCallData( request.get(), null ));
                    }
                }
            }
        }
        return Optional.empty();
    }

    @Override
    protected Optional<ToolCallData> isToolCall(NodeOutput<? extends AgentState> output) {
        if( output.state() instanceof AgentExecutorEx.State state ) {

            final var lastMessageOptional = state.lastMessage();

            if( lastMessageOptional.isPresent() ) {

                final var lastMessage = lastMessageOptional.get();
                final var request = isToolCallRequest(lastMessage);
                if (request.isPresent()) {
                    return Optional.of(new ToolCallData(request.get(), null));
                }
                final var response = isToolCallResponse(lastMessage);
                if (response.isPresent()) {
                    return Optional.of(new ToolCallData(null, response.get()));
                }
            }
        }
        return Optional.empty();
    }

    private Optional<ToolCallData.Response> isToolCallResponse( Message lastMessage ) {

        if( lastMessage instanceof ToolResponseMessage toolResponse) {

            if( toolResponse.getResponses().size() != 1 ) {
                throw new IllegalStateException( "MULTIPLE TOOL RESPONSES '%d' ARE NOT SUPPORTED".formatted( toolResponse.getResponses().size() ) );
            }

            final var res = toolResponse.getResponses().get(0);

            return Optional.of( new ToolCallData.Response(
                res.id(),
                res.responseData()));


        }
        return Optional.empty();

    }

    private Optional<ToolCallData.Request> isToolCallRequest( Message lastMessage ) {

        if (lastMessage instanceof AssistantMessage message) {

            if (message.hasToolCalls()) {

                final var toolCalls = message.getToolCalls();

                if( toolCalls.size() != 1 ) {
                    throw new IllegalStateException( "MULTIPLE TOOL REQUESTS '%d' ARE NOT SUPPORTED".formatted( toolCalls.size() ) );

                }

                final var toolCall = toolCalls.get(0);

                return Optional.of(new ToolCallData.Request(
                        toolCall.id(),
                        toolCall.name(),
                        toolCall.arguments()));

            }
        }
        return Optional.empty();

    }

}
