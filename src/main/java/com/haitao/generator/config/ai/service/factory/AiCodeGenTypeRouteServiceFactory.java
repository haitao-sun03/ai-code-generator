package com.haitao.generator.config.ai.service.factory;

import com.haitao.generator.ai.AiCodeGenTypeRouteService;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@Slf4j
public class AiCodeGenTypeRouteServiceFactory {

    @Autowired
    @Qualifier("routeChatModel")
    private ObjectProvider<ChatModel> routeChatModelProvider;

    /**
     * 初始化智能路由的AI service
     */
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public AiCodeGenTypeRouteService aiCodeGenTypeRouteService() {
        return AiServices.builder(AiCodeGenTypeRouteService.class)
                .chatModel(routeChatModelProvider.getObject())
                .build();
    }
}
