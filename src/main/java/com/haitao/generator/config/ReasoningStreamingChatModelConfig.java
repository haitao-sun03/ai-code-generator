package com.haitao.generator.config;

import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "langchain4j.open-ai.chat-model")
@Data
public class ReasoningStreamingChatModelConfig {

    private String baseUrl;
    private String apiKey;


    @Bean
    public OpenAiStreamingChatModel reasoningStreamingChatModel() {
//        String modelName = "deepseek-reasoner";
//        int maxTokens = 32768;

        String modelName = "deepseek-chat";
        int maxTokens = 8192;

        return OpenAiStreamingChatModel
                .builder()
                .baseUrl(baseUrl)
                .apiKey(apiKey)
                .modelName(modelName)
                .maxTokens(maxTokens)
                .logRequests(true)
                .logResponses(true)
                .build();
    }
}
