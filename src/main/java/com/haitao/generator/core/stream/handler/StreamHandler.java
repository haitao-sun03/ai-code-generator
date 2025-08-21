package com.haitao.generator.core.stream.handler;

import com.haitao.generator.enums.CodeGenTypeEnum;
import com.haitao.generator.service.ChatHistoryService;
import reactor.core.publisher.Flux;

/**
 * 流处理器接口
 * 实现类有自己的逻辑，对不同流进行不同处理
 *
 */
public interface StreamHandler {

    boolean support(CodeGenTypeEnum codeGenTypeEnum);

    /**
     * 解析每个原始留originStream，进行解析，拼接，保存等操作
     * @param originStream
     * @param chatHistoryService
     * @param appId
     * @param userId
     * @return
     */
    Flux<String> handle(Flux<String> originStream, ChatHistoryService chatHistoryService, long appId, long userId);
}
