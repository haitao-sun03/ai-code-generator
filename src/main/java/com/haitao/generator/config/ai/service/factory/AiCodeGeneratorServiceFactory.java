package com.haitao.generator.config.ai.service.factory;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.haitao.generator.ai.AiCodeGeneratorService;
import com.haitao.generator.ai.guardrails.PromptSafetyInputGuardrail;
import com.haitao.generator.ai.tools.ToolsManager;
import com.haitao.generator.enums.CodeGenTypeEnum;
import com.haitao.generator.service.ChatHistoryService;
import dev.langchain4j.community.store.memory.chat.redis.RedisChatMemoryStore;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@Slf4j
public class AiCodeGeneratorServiceFactory {

    @Resource
    private ChatModel openAiChatModel;

    @Autowired
    @Qualifier("streamingChatModel")
    private ObjectProvider<OpenAiStreamingChatModel> streamingChatModelObjectProvider;

    @Autowired
    @Qualifier("reasoningStreamingChatModel")
    private ObjectProvider<OpenAiStreamingChatModel> reasoningStreamingChatModelObjectProvider;

    @Autowired
    private RedisChatMemoryStore redisChatMemoryStore;

    @Autowired
    private ChatHistoryService chatHistoryService;

    @Resource
    private ToolsManager toolsManager;

    /**
     * AI 服务实例缓存
     * 缓存策略：
     * - 最大缓存 1000 个实例
     * - 写入后 30 分钟过期
     * - 访问后 10 分钟过期
     */
    private final Cache<Long, AiCodeGeneratorService> aiServiceCache = Caffeine.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(Duration.ofMinutes(30))
            .expireAfterAccess(Duration.ofMinutes(10))
            .removalListener((key, value, cause) -> {
                log.info("AI 服务实例被移除，appId: {}, 原因: {}", key, cause);
            })
            .build();


    /**
     * 根据 appId 从Caffeine中获取AiCodeGeneratorService
     * 若没有重新创建一个放入
     */
    public AiCodeGeneratorService getAiCodeGeneratorService(long appId, CodeGenTypeEnum codeGenTypeEnum) {
        return aiServiceCache.get(appId, appIdKey -> createAiCodeGeneratorService(appId, codeGenTypeEnum));
    }

    /**
     * 创建新的 AI 服务实例
     */
    private AiCodeGeneratorService createAiCodeGeneratorService(long appId, CodeGenTypeEnum codeGenTypeEnum) {
        log.info("为 appId: {} 创建新的 AI 服务实例", appId);
        // 根据 appId 构建独立的对话记忆
        MessageWindowChatMemory chatMemory = MessageWindowChatMemory
                .builder()
                .id(appId)
                .chatMemoryStore(redisChatMemoryStore)
                .maxMessages(20)
                .build();
        //每次AiCodeGeneratorService从Caffeine中过期后，需要重新生成新的AiCodeGeneratorService，需要重新同步对话记忆到redis
        chatHistoryService.loadChatHistoryToMemory(appId, chatMemory, 20);

        return switch (codeGenTypeEnum) {
            case VUE_PROJECT -> AiServices.builder(AiCodeGeneratorService.class)
                    .chatModel(openAiChatModel)
                    //使用多例的reasoningStreamingChatModel解决并发访问问题
                    .streamingChatModel(reasoningStreamingChatModelObjectProvider.getObject())
                    .chatMemoryProvider(memoryId -> chatMemory)
                    .tools(toolsManager.getTools())
                    .hallucinatedToolNameStrategy(toolExecutionRequest ->
                            ToolExecutionResultMessage.from(toolExecutionRequest, "not exists the tool named: " + toolExecutionRequest.name()))
                    .inputGuardrails(new PromptSafetyInputGuardrail())
                    //为了避免AI无限调用工具，可以设置最多连续调用工具20次
                    //经过测试：一般的网页生成不超过20次调用工具即可完成，而超过20次的情况基本就是无限调用的情况
                    .maxSequentialToolsInvocations(20)
                    .build();


            case HTML, MULTI_FILE -> AiServices.builder(AiCodeGeneratorService.class)
                    .chatModel(openAiChatModel)
                    //使用多例的streamingChatModel解决并发访问问题
                    .streamingChatModel(streamingChatModelObjectProvider.getObject())
                    .chatMemory(chatMemory)
                    .inputGuardrails(new PromptSafetyInputGuardrail())
                    .build();

        };

    }

}
