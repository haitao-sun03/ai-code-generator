package com.haitao.generator.core.stream.handler;

import cn.hutool.core.util.StrUtil;
import com.haitao.generator.enums.ChatMessageTypeEnum;
import com.haitao.generator.enums.CodeGenTypeEnum;
import com.haitao.generator.service.ChatHistoryService;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class BasicTextStreamHandler implements StreamHandler {
    @Override
    public boolean support(CodeGenTypeEnum codeGenTypeEnum) {
        switch (codeGenTypeEnum) {
            case HTML, MULTI_FILE -> {
                return true;
            }
            case VUE_PROJECT -> {
                return false;
            }
            default -> {
                return false;
            }
        }
    }

    @Override
    public Flux<String> handle(Flux<String> originStream, ChatHistoryService chatHistoryService, long appId, long userId) {
        StringBuilder aiResponseBuilder = new StringBuilder();
        return originStream.doOnNext(chunk -> {
            aiResponseBuilder.append(chunk);
        }).doOnComplete(() -> {
//            保存AI回复消息
            String aiResponseContent = aiResponseBuilder.toString();
            chatHistoryService.addChatMessage(appId, aiResponseContent, ChatMessageTypeEnum.AI.getValue(), userId);
        }).doOnError(err -> {
//            保存AI回复消息错误
            String errMessage = StrUtil.format("AI回复异常：{}", err.getMessage());
            chatHistoryService.addChatMessage(appId, errMessage, ChatMessageTypeEnum.AI.getValue(), userId);
        });
    }
}
