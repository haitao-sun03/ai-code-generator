package com.haitao.generator.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import com.haitao.generator.exception.BusinessException;
import com.haitao.generator.exception.ErrorCode;
import com.haitao.generator.service.ProjectDownloadService;
import com.haitao.generator.utils.ThrowUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Set;

@Service
@Slf4j
public class ProjectDownloadServiceImpl implements ProjectDownloadService {

    //需要过滤的文件和目录名称
    private static final Set<String> IGNORED_NAMES = Set.of(
            "node_modules",
            ".git",
            "dist",
            "build",
            ".DS_Store",
            ".env",
            "target",
            ".mvn",
            ".idea",
            ".vscode"
    );

    //需要过滤的文件扩展名
    private static final Set<String> IGNORED_EXTENSIONS = Set.of(
            ".log",
            ".tmp",
            ".cache"
    );

    /**
     * 下载项目
     *
     * @param projectDir  项目完整路径
     * @param projectName 下载到的文件名
     * @param response    请求响应
     */
    @Override
    public void downloadProject(String projectDir, String projectName, HttpServletResponse response) {
        //检查下载目录字符串不能为空
        ThrowUtils.throwIf(StrUtil.isBlank(projectDir), ErrorCode.PARAMS_ERROR, "projectDir不能为空");
        //下载到的文件名不能为空
        ThrowUtils.throwIf(StrUtil.isBlank(projectName), ErrorCode.PARAMS_ERROR, "projectName不能为空");
        //下载路径必须是目录，且必须否存在
        File projectDirFile = new File(projectDir);
        ThrowUtils.throwIf(!projectDirFile.exists() || !projectDirFile.isDirectory(), ErrorCode.PARAMS_ERROR, "projectName不能为空");
        //设置响应头
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", StrUtil.format("attachment; filename = \"{}.zip\"", projectName));

        //文件过滤
        FileFilter fileFilter = file -> isPathAllowed(projectDirFile.toPath(), file.toPath());

        try {
            ZipUtil.zip(response.getOutputStream(),
                    StandardCharsets.UTF_8,
                    false,
                    fileFilter,
                    projectDirFile);
            log.info("文件压缩成功");
        } catch (IOException e) {
            log.error("文件压缩失败，原因：{}", e.getMessage());
            throw new BusinessException(ErrorCode.OPERATION_ERROR, StrUtil.format("文件压缩失败；{}，原因：{}", projectDir, e.getMessage()));
        }
    }

    /**
     * 检查路径是否允许包含在压缩包中
     *
     * @param projectRoot 项目根目录
     * @param fullPath    完整路径
     * @return 是否允许
     */
    private boolean isPathAllowed(Path projectRoot, Path fullPath) {
        // 获取相对路径
        Path relativePath = projectRoot.relativize(fullPath);
        // 检查路径中的每一部分
        for (Path part : relativePath) {
            String partName = part.toString();
            // 检查是否在忽略名称列表中
            if (IGNORED_NAMES.contains(partName)) {
                return false;
            }
            // 检查文件扩展名
            if (IGNORED_EXTENSIONS.stream().anyMatch(partName::endsWith)) {
                return false;
            }
        }
        return true;
    }

}
