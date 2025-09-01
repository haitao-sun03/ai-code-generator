package com.haitao.generator.config.ai.models;

import com.haitao.generator.monitor.AiModelMonitorListener;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import jakarta.annotation.Resource;
import lombok.Data;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "langchain4j.open-ai.streaming-chat-model")
@Data
public class StreamingChatModelConfig {

    @Resource
    private AiModelMonitorListener aiModelMonitorListener;

    private String baseUrl;
    private String apiKey;
    private String modelName;
    private int maxTokens;
    private boolean logRequests;
    private boolean logResponses;

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public OpenAiStreamingChatModel streamingChatModel() {

        return OpenAiStreamingChatModel
                .builder()
                .baseUrl(baseUrl)
                .apiKey(apiKey)
                .modelName(modelName)
                .maxTokens(maxTokens)
                .logRequests(logRequests)
                .logResponses(logResponses)
                .listeners(List.of(aiModelMonitorListener))
                .build();
    }
}
