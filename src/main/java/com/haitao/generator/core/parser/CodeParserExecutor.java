package com.haitao.generator.core.parser;

import com.haitao.generator.ai.model.HtmlCodeResult;
import com.haitao.generator.enums.CodeGenTypeEnum;
import com.haitao.generator.exception.BusinessException;
import com.haitao.generator.exception.ErrorCode;

public class CodeParserExecutor {

    private static final HtmlCodeParser htmlCodeParser = new HtmlCodeParser();
    private static final MultiCodeParser multiCodeParser = new MultiCodeParser();

    public static Object executeCodeParser(String content, CodeGenTypeEnum codeGenTypeEnum) {
        return switch (codeGenTypeEnum) {
            case HTML -> htmlCodeParser.parseCode(content);
            case MULTI_FILE -> multiCodeParser.parseCode(content);
            default ->
                    throw new BusinessException(ErrorCode.SYSTEM_ERROR, "CodeParserExecutor:不支持的解析类型" + codeGenTypeEnum);
        };
    }
}
