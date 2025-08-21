package com.haitao.generator.config;

import com.haitao.generator.ai.AiCodeGenTypeRouteService;
import com.haitao.generator.ai.AiCodeGeneratorService;
import com.haitao.generator.ai.tools.WriteFileTool;
import com.haitao.generator.enums.CodeGenTypeEnum;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class AiCodeGenTypeRouteServiceFactory {

    @Autowired
    private ChatModel chatModel;

    /**
     * 初始化智能路由的AI service
     */
    @Bean
    public AiCodeGenTypeRouteService aiCodeGenTypeRouteService() {
        return AiServices.builder(AiCodeGenTypeRouteService.class)
                .chatModel(chatModel)
                .build();
    }
}
