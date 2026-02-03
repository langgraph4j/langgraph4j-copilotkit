package org.bsc.langgraph4j.agui.sdk;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaChatOptions;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;

import java.util.function.Supplier;

public enum AIModel {

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
                    .defaultOptions(OllamaChatOptions.builder()
                            .model("qwen2.5:7b")
                            .temperature(0.1)
                            .build())
                    .build()),
    OLLAMA_QWEN3_14B( () ->
            OllamaChatModel.builder()
                    .ollamaApi( OllamaApi.builder().baseUrl("http://localhost:11434").build() )
                    .defaultOptions(OllamaChatOptions.builder()
                            .model("qwen3:14b")
                            .temperature(0.1)
                            .build())
                    .build());
    ;

    public final Supplier<ChatModel> model;

    AIModel(  Supplier<ChatModel> model ) {
        this.model = model;
    }
}

