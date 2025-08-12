package com.haitao.generator.core.saver;

import com.haitao.generator.ai.model.HtmlCodeResult;
import com.haitao.generator.ai.model.MultiFileCodeResult;
import com.haitao.generator.enums.CodeGenTypeEnum;
import com.haitao.generator.exception.BusinessException;
import com.haitao.generator.exception.ErrorCode;

import java.io.File;

public class CodeFileSaverExecutor {
    private static final HtmlFileSaver HTML_FILE_SAVER = new HtmlFileSaver();
    private static final MultiFileSaver MULTI_FILE_SAVER = new MultiFileSaver();

    public static File executeCodeFileSaver(Object content, CodeGenTypeEnum codeGenTypeEnum) {
        return switch (codeGenTypeEnum) {
            case HTML -> HTML_FILE_SAVER.saveFile((HtmlCodeResult) content);
            case MULTI_FILE -> MULTI_FILE_SAVER.saveFile((MultiFileCodeResult) content);
            default -> {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "CodeFileSaverExecutor:保存文件格式不支持" + codeGenTypeEnum);
            }
        };
    }
}
