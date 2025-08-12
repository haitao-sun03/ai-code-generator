package com.haitao.generator.core.saver;

import com.haitao.generator.ai.model.HtmlCodeResult;
import com.haitao.generator.enums.CodeGenTypeEnum;

/**
 * html单文件生成保存
 */
public class HtmlFileSaver extends CodeFileSaverTemplate<HtmlCodeResult> {
    @Override
    CodeGenTypeEnum getGenType() {
        return CodeGenTypeEnum.HTML;
    }

    @Override
    void saveFiles(HtmlCodeResult codeResult, String baseDir) {
        writeToFile(baseDir, "index.html", codeResult.getHtmlCode());
    }
}
