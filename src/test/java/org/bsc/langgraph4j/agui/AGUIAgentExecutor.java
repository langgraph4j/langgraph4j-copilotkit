package org.bsc.langgraph4j.agui;

import org.bsc.langgraph4j.GraphRepresentation;
import org.bsc.langgraph4j.GraphStateException;
import org.bsc.langgraph4j.action.InterruptionMetadata;
import org.bsc.langgraph4j.checkpoint.MemorySaver;
import org.bsc.langgraph4j.spring.ai.agentexecutor.AgentExecutorEx;
import org.bsc.langgraph4j.spring.ai.util.MessageUtil;
import org.bsc.langgraph4j.state.AgentState;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.util.*;
import java.util.function.Supplier;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static org.bsc.langgraph4j.utils.CollectionsUtils.lastOf;

public class AGUIAgentExecutor extends AGUILangGraphAgent {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AGUIAgentExecutor.class);

    enum AiModel {

        OPENAI_GPT_4O_MINI( () ->
                OpenAiChatModel.builder()
                        .openAiApi(OpenAiApi.builder()
                                .baseUrl("https://api.openai.com")
                                .apiKey( System.getenv("OPENAI_API_KEY"))
                                .build())
                        .defaultOptions(OpenAiChatOptions.builder()
                                .model("gpt-4o-mini")
                                .logprobs(false)
                                .temperature(0.1)
                                .build())
                        .build()),
        GITHUB_MODELS_GPT_4O_MINI( () ->
                OpenAiChatModel.builder()
                        .openAiApi(OpenAiApi.builder()
                                .baseUrl("https://models.github.ai/inference") // GITHUB MODELS
                                .apiKey(System.getenv("GITHUB_MODELS_TOKEN"))
                                .build())
                        .defaultOptions(OpenAiChatOptions.builder()
                                .model("gpt-4o-mini")
                                .logprobs(false)
                                .temperature(0.1)
                                .build())
                        .build()),
        OLLAMA_QWEN2_5_7B( () ->
                OllamaChatModel.builder()
                        .ollamaApi( OllamaApi.builder().baseUrl("http://localhost:11434").build() )
                        .defaultOptions(OllamaOptions.builder()
                                .model("qwen2.5:7b")
                                .temperature(0.1)
                                .build())
                        .build()),
        OLLAMA_QWEN3_14B( () ->
                OllamaChatModel.builder()
                .ollamaApi( OllamaApi.builder().baseUrl("http://localhost:11434").build() )
                .defaultOptions(OllamaOptions.builder()
                                .model("qwen3:14b")
                                .temperature(0.1)
                                .build())
                .build());
        ;

        public final Supplier<ChatModel> model;

        AiModel(  Supplier<ChatModel> model ) {
            this.model = model;
        }
    }

    public static class Tools {

        @Tool( description = "Send an email to someone")
        public String sendEmail(
                @ToolParam( description = "destination address") String to,
                @ToolParam( description = "subject of the email") String subject,
                @ToolParam( description = "body of the email") String body
        ) {
            // This is a placeholder for the actual implementation
            return format("mail sent to %s with subject %s", to, subject);
        }

        @Tool( description = "Get the weather in location")
        public String queryWeather(@ToolParam( description = "The query to use in your search.") String query) {
            // This is a placeholder for the actual implementation
            return "Cold, with a low of 13 degrees";
        }
    }

    public AGUIAgentExecutor() {
        super(new MemorySaver());
    }

    @Override
    GraphData buildStateGraph() throws GraphStateException {

        var model = ofNullable(System.getenv("OPENAI_API_KEY"))
                .map( key -> AiModel.OPENAI_GPT_4O_MINI.model.get())
                .orElseGet( () ->
                    ofNullable( System.getenv("GITHUB_MODELS_TOKEN") )
                            .map( key -> AiModel.GITHUB_MODELS_GPT_4O_MINI.model.get() )
                            .orElseGet( AiModel.OLLAMA_QWEN2_5_7B.model ));

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

        return new GraphData( agent ) ;
    }

    @Override
    Map<String, Object> buildGraphInput(AGUIType.RunAgentInput input) {

        var lastUserMessage = input.lastUserMessage()
                .map(AGUIMessage.TextMessage::content)
                .orElseThrow( () -> new IllegalStateException("last user message not found"));

        log.debug( "LAST USER MESSAGE: {}", lastUserMessage );

        return Map.of("messages", new UserMessage(lastUserMessage));
    }

    @Override
    <State extends AgentState> List<Approval> onInterruption(AGUIType.RunAgentInput input, InterruptionMetadata<State> state ) {

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
