package com.haitao.generator.core.builder;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class VueProjectBuilder {

    public void buildProjectAsync(String projectDir) {
        Thread.ofVirtual().name("vue-builder-" + RandomUtil.randomString(5)).start(() -> {
            try {
                buildProject(projectDir);
            } catch (Exception e) {
                log.error("异步构建Vue项目失败，原因 : {}", e.getMessage());
            }
        });
    }

    public boolean buildProject(String projectDir) {
        File file = new File(projectDir);
        if (!file.exists()) {
            log.error("projectDir :{} ,不存在", projectDir);
            return false;
        }
        File packageJsonFile = new File(projectDir, "package.json");
        if (!packageJsonFile.exists()) {
            log.error("packageJsonFile :{} ,不存在", packageJsonFile.getAbsolutePath());
            return false;
        }

        log.info("开始构建Vue项目，projectDir：{}", projectDir);

        if (!executeNpmInstall(file)) {
            log.error("npm install 执行失败");
            return false;
        }

        if (!executeNpmRunBuild(file)) {
            log.error("npm run build 执行失败");
            return false;
        }

        File distDir = new File(projectDir, "dist");
        if (!distDir.exists()) {
            log.error("build完成，但dist目录生成失败");
            return false;
        }

        log.info("build完成,dist目录：{}", distDir.getAbsolutePath());
        return true;

    }


    private boolean executeNpmInstall(File workingDir) {
        log.info("执行npm install,workingDir:{}", workingDir);
        String npmCommand = StrUtil.format("{} install", buildCommandWithOS("npm"));
        return executeCommand(workingDir, npmCommand, 300);
    }

    private boolean executeNpmRunBuild(File workingDir) {
        log.info("执行npm run build,workingDir:{}", workingDir);
        String npmCommand = StrUtil.format("{} run build", buildCommandWithOS("npm"));
        return executeCommand(workingDir, npmCommand, 300);
    }

    private String buildCommandWithOS(String baseCommand) {
        return isWin() ? baseCommand + ".cmd" : baseCommand;
    }

    /**
     * 判断操作系统是否windows
     *
     * @return
     */
    private boolean isWin() {
        return System.getProperty("os.name").toLowerCase().contains("windows");
    }


    /**
     * 执行命令通用方法
     *
     * @param workingDir     工作目录
     * @param command        命令字符串
     * @param timeoutSeconds 超时时间（秒）
     * @return 是否执行成功
     */
    private boolean executeCommand(File workingDir, String command, int timeoutSeconds) {
        try {
            log.info("在目录 {} 中执行命令: {}", workingDir.getAbsolutePath(), command);
            Process process = RuntimeUtil.exec(
                    null,
                    workingDir,
                    command.split("\\s+") // 命令分割为数组
            );
            // 等待进程完成，设置超时
            boolean finished = process.waitFor(timeoutSeconds, TimeUnit.SECONDS);
            if (!finished) {
                log.error("命令执行超时（{}秒），强制终止进程", timeoutSeconds);
                process.destroyForcibly();
                return false;
            }
            int exitCode = process.exitValue();
            if (exitCode == 0) {
                log.info("命令执行成功: {}", command);
                return true;
            } else {
                log.error("命令执行失败，退出码: {}", exitCode);
                return false;
            }
        } catch (Exception e) {
            log.error("执行命令失败: {}, 错误信息: {}", command, e.getMessage());
            return false;
        }
    }

}
