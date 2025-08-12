package com.haitao.generator.ai;

import com.haitao.generator.ai.model.HtmlCodeResult;
import com.haitao.generator.ai.model.MultiFileCodeResult;
import dev.langchain4j.service.SystemMessage;
import reactor.core.publisher.Flux;

/**
 * AI交互接口配置
 * langchain4j会在运行过程中创建代理与LLM交互
 */
public interface AiCodeGeneratorService {

    /**
     * 非流式，结构化输出
     * @param userMessage
     * @return
     */
    @SystemMessage(fromResource = "prompt/single-html-generate-prompt.txt")
    HtmlCodeResult generateHtmlCode(String userMessage);

    @SystemMessage(fromResource = "prompt/multi-generate-prompt.txt")
    MultiFileCodeResult generateMultiCode(String userMessage);


    /**
     * 流式输出，需要手动处理代码拼接
     * @param userMessage
     * @return
     */
    @SystemMessage(fromResource = "prompt/single-html-generate-prompt.txt")
    Flux<String> generateHtmlCodeStream(String userMessage);

    @SystemMessage(fromResource = "prompt/multi-generate-prompt.txt")
    Flux<String> generateMultiCodeStream(String userMessage);

}
