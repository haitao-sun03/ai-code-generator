package com.haitao.generator.core;

import cn.hutool.json.JSONUtil;
import com.haitao.generator.ai.AiCodeGeneratorService;
import com.haitao.generator.ai.model.HtmlCodeResult;
import com.haitao.generator.ai.model.MultiFileCodeResult;
import com.haitao.generator.ai.model.messages.AiResponseTextMessage;
import com.haitao.generator.ai.model.messages.ToolExecutedMessage;
import com.haitao.generator.ai.model.messages.ToolRequestMessage;
import com.haitao.generator.config.AiCodeGeneratorServiceFactory;
import com.haitao.generator.core.parser.CodeParserExecutor;
import com.haitao.generator.core.saver.CodeFileSaverExecutor;
import com.haitao.generator.core.stream.handler.StreamHandler;
import com.haitao.generator.enums.CodeGenTypeEnum;
import com.haitao.generator.exception.BusinessException;
import com.haitao.generator.exception.ErrorCode;
import dev.langchain4j.service.TokenStream;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.File;
import java.util.List;

/**
 * AI 代码生成外观类，组合生成和保存功能
 */
@Service
@Slf4j
public class AiCodeGeneratorFacade {

    @Resource
    @Lazy
    private AiCodeGeneratorServiceFactory aiCodeGeneratorServiceFactory;

    /**
     * 统一入口：根据类型生成并保存代码
     *
     * @param userMessage     用户提示词
     * @param codeGenTypeEnum 生成类型
     * @return 保存的目录
     */
    public File generateAndSaveCode(String userMessage, CodeGenTypeEnum codeGenTypeEnum, Long appId) {
        if (codeGenTypeEnum == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "生成类型为空");
        }
        AiCodeGeneratorService aiCodeGeneratorService = aiCodeGeneratorServiceFactory.getAiCodeGeneratorService(appId, codeGenTypeEnum);
        return switch (codeGenTypeEnum) {
            case HTML -> {
                HtmlCodeResult result = aiCodeGeneratorService.generateHtmlCode(userMessage);
                yield CodeFileSaverExecutor.executeCodeFileSaver(result, codeGenTypeEnum, appId);
            }
            case MULTI_FILE -> {
                MultiFileCodeResult result = aiCodeGeneratorService.generateMultiCode(userMessage);
                yield CodeFileSaverExecutor.executeCodeFileSaver(result, codeGenTypeEnum, appId);
            }
            default -> {
                String errorMessage = "不支持的生成类型：" + codeGenTypeEnum.getValue();
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, errorMessage);
            }
        };
    }


    /**
     * 统一入口：根据类型生成并保存代码（流式）
     *
     * @param userMessage     用户提示词
     * @param codeGenTypeEnum 生成类型
     * @return 保存的目录
     */
    public Flux<String> generateAndSaveCodeStream(String userMessage, CodeGenTypeEnum codeGenTypeEnum, Long appId) {
        if (codeGenTypeEnum == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "生成类型为空");
        }
        AiCodeGeneratorService aiCodeGeneratorService = aiCodeGeneratorServiceFactory.getAiCodeGeneratorService(appId, codeGenTypeEnum);
        return switch (codeGenTypeEnum) {
            case HTML -> {
                Flux<String> htmlFlux = aiCodeGeneratorService.generateHtmlCodeStream(userMessage);
                yield proccessCode(htmlFlux, codeGenTypeEnum, appId);
            }
            case MULTI_FILE -> {
                Flux<String> multiFlux = aiCodeGeneratorService.generateMultiCodeStream(userMessage);
                yield proccessCode(multiFlux, codeGenTypeEnum, appId);
            }
            case VUE_PROJECT -> {
                TokenStream tokenStream = aiCodeGeneratorService.generateVueProjectStream(appId, userMessage);
//                将tokenStream转换为统一的返回值类型Flux<String>，其中对不同类型的响应流进行包装，包括：AI基础回复文本，tool calling请求消息，tool calling完成消息
                yield proceesTokenStream(tokenStream);
            }

            default -> {
                String errorMessage = "不支持的生成类型：" + codeGenTypeEnum.getValue();
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, errorMessage);
            }
        };
    }

    /**
     *
     * @param codeStream      与AI交互后生成的内容流
     * @param codeGenTypeEnum 解析与保存的类型枚举
     * @return
     */
    private Flux<String> proccessCode(Flux<String> codeStream, CodeGenTypeEnum codeGenTypeEnum, Long appId) {
        StringBuilder resultBuilder = new StringBuilder();
        return codeStream
                .doOnNext(chunk -> resultBuilder.append(chunk))
                .doOnComplete(() -> {
                    Object codeRes = CodeParserExecutor.executeCodeParser(resultBuilder.toString(), codeGenTypeEnum);
                    CodeFileSaverExecutor.executeCodeFileSaver(codeRes, codeGenTypeEnum, appId);
                    log.info("保存文件成功");
                });

    }

    private Flux<String> proceesTokenStream(TokenStream tokenStream) {
        return Flux.create(sink -> {
            tokenStream.onPartialResponse(partialResponse -> {
                        AiResponseTextMessage aiResponseTextMessage = new AiResponseTextMessage(partialResponse);
                        sink.next(JSONUtil.toJsonStr(aiResponseTextMessage));
                    })
                    .beforeToolExecution((beforeToolExecution) -> {
                        ToolRequestMessage toolRequestMessage = new ToolRequestMessage(beforeToolExecution.request());
                        sink.next(JSONUtil.toJsonStr(toolRequestMessage));
                    })
                    .onToolExecuted(toolExecution -> {
                        ToolExecutedMessage toolExecutedMessage = new ToolExecutedMessage(toolExecution);
                        sink.next(JSONUtil.toJsonStr(toolExecutedMessage));
                    })
                    .onCompleteResponse(response -> {
                        sink.complete();
                    })
                    .onError(error -> {
                        sink.error(error);
                    })
                    .start();
        });
    }
}
