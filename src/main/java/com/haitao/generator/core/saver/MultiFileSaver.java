package com.haitao.generator.core.saver;

import com.haitao.generator.ai.model.MultiFileCodeResult;
import com.haitao.generator.enums.CodeGenTypeEnum;

/**
 * 多文件生成保存
 */
public class MultiFileSaver extends CodeFileSaverTemplate<MultiFileCodeResult> {
    @Override
    CodeGenTypeEnum getGenType() {
        return CodeGenTypeEnum.MULTI_FILE;
    }

    @Override
    void saveFiles(MultiFileCodeResult codeResult, String baseDir) {
        writeToFile(baseDir, "index.html", codeResult.getHtmlCode());
        writeToFile(baseDir, "style.css", codeResult.getCssCode());
        writeToFile(baseDir, "script.js", codeResult.getJsCode());
    }
}
