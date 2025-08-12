package com.haitao.generator.core;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.haitao.generator.ai.model.HtmlCodeResult;
import com.haitao.generator.ai.model.MultiFileCodeResult;
import com.haitao.generator.enums.CodeGenTypeEnum;

import java.io.File;
import java.nio.charset.StandardCharsets;

@Deprecated
public class CodeFileSaver {
    private static final String FILE_SAVE_ROOT_DIR = System.getProperty("user.dir") + "/tmp/code_output";

    public static File saveHtmlCode(HtmlCodeResult result) {
        String baseDir = buildDir(CodeGenTypeEnum.HTML.getValue());
        writeToFile(baseDir, "index.html", result.getHtmlCode());
        return new File(baseDir);
    }


    public static File saveMultiCode(MultiFileCodeResult result) {
        String baseDir = buildDir(CodeGenTypeEnum.MULTI_FILE.getValue());
        writeToFile(baseDir, "index.html", result.getHtmlCode());
        writeToFile(baseDir, "style.css", result.getCssCode());
        writeToFile(baseDir, "script.js", result.getJsCode());
        return new File(baseDir);
    }


    private static String buildDir(String codeGenType) {
//        生成唯一的文件夹
        String directoryName = String.format("%s_%s", codeGenType, IdUtil.getSnowflakeNextIdStr());
        String dirPath = FILE_SAVE_ROOT_DIR + File.separator + directoryName;
        FileUtil.mkdir(dirPath);
        return dirPath;
    }

    private static void writeToFile(String baseDir, String fileName, String saveContent) {
        String filePath = baseDir + File.separator + fileName;
        FileUtil.writeString(saveContent, filePath, StandardCharsets.UTF_8);

    }
}
