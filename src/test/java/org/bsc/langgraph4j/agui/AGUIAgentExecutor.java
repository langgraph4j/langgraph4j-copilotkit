package org.bsc.langgraph4j.agui;

import org.bsc.langgraph4j.GraphStateException;
import org.bsc.langgraph4j.StateGraph;
import org.bsc.langgraph4j.checkpoint.MemorySaver;
import org.bsc.langgraph4j.spring.ai.agentexecutor.AgentExecutor;
import org.bsc.langgraph4j.state.AgentState;
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

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static java.util.Optional.ofNullable;

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
        OLLAMA_QWEN2_5_7B( () ->
                OllamaChatModel.builder()
                        .ollamaApi( OllamaApi.builder().baseUrl("http://localhost:11434").build() )
                        .defaultOptions(OllamaOptions.builder()
                                .model("qwen2.5:7b")
                                .temperature(0.1)
                                .build())
                        .build());
        ;

        public final Supplier<ChatModel> model;

        AiModel(  Supplier<ChatModel> model ) {
            this.model = model;
        }
    }

    public static class WeatherTool {

        @Tool( description = "Get the weather in location")
        public String execQuery(@ToolParam( description = "The query to use in your search.") String query) {
            // This is a placeholder for the actual implementation
            return "Cold, with a low of 13 degrees";
        }
    }

    public AGUIAgentExecutor() {
        super(new MemorySaver());
    }

    @Override
    StateGraph<? extends AgentState> buildStateGraph() throws GraphStateException {

        var model = ofNullable(System.getenv("OPENAI_API_KEY"))
                .map( key -> AiModel.OPENAI_GPT_4O_MINI.model.get())
                .orElseGet(AiModel.OLLAMA_QWEN2_5_7B.model);

        return AgentExecutor.builder()
                .streamingChatModel(model)
                .toolsFromObject(new WeatherTool())
                .build();
    }

    @Override
    Map<String, Object> buildGraphInput(AGUIType.RunAgentInput input) {
        var lastMessage = input.messages().stream()
                .reduce((first, second) -> second)
                .map(AGUIMessage::content)
                .orElseThrow();

        return Map.of("messages", new UserMessage(lastMessage));
    }


    public static void main( String[] argv ) throws Exception {

        var agent = new AGUIAgentExecutor();

        var input = new AGUIType.RunAgentInput(
                "thread1",
                "run1",
                null,
                List.of( new AGUIMessage.UserMessage( "msg1", "tell me a joke!" )),
                null,
                null,
                null );

        agent.run(input).subscribe();

    }
}
