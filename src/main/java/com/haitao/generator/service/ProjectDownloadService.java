package com.haitao.generator.service;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface ProjectDownloadService {

    /**
     * 下载项目
     * @param projectDir 项目完整路径
     * @param projectName 下载到的文件名
     * @param response 请求响应
     */
    void downloadProject(String projectDir, String projectName, HttpServletResponse response);

}
