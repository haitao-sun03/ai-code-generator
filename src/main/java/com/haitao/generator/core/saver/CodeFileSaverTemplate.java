package com.haitao.generator.core.saver;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.haitao.generator.enums.CodeGenTypeEnum;
import com.haitao.generator.exception.BusinessException;
import com.haitao.generator.exception.ErrorCode;

import java.io.File;
import java.nio.charset.StandardCharsets;

public abstract class CodeFileSaverTemplate<T> {

    public static final String FILE_SAVE_ROOT_DIR = System.getProperty("user.dir") + "/tmp/code_output";

    /**
     * 模板方法
     * 定义保存文件的流程
     *
     * @param codeResult
     * @return
     */
    public final File saveFile(T codeResult) {
        validateInput(codeResult);
//        生成文件的保存目录
        String baseDir = buildDir();
        saveFiles(codeResult, baseDir);
        return new File(baseDir);
    }

    /**
     * 默认校验逻辑
     *
     * @return
     */
    protected void validateInput(T codeResult) {
        if (codeResult == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "待保存的生成内容不能为空");
        }
    }

    protected String buildDir() {
        CodeGenTypeEnum codeGenType = this.getGenType();
//        生成唯一的文件夹
        String directoryName = String.format("%s_%s", codeGenType.getValue(), IdUtil.getSnowflakeNextIdStr());
        String dirPath = FILE_SAVE_ROOT_DIR + File.separator + directoryName;
        FileUtil.mkdir(dirPath);
        return dirPath;
    }


    protected void writeToFile(String baseDir, String fileName, String saveContent) {
        if (StrUtil.isNotBlank(saveContent)) {
            String filePath = baseDir + File.separator + fileName;
            FileUtil.writeString(saveContent, filePath, StandardCharsets.UTF_8);
        }

    }

    /**
     * 子类自行实现
     *
     * @return 保存的类型，目前为：HTML和MULTI
     */
    abstract CodeGenTypeEnum getGenType();


    /**
     * 保存文件的具体逻辑，子类自行实现
     *
     * @param codeResult
     * @param baseDir
     */
    abstract void saveFiles(T codeResult, String baseDir);


}
