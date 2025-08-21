package com.haitao.generator.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.haitao.generator.exception.ErrorCode;
import com.haitao.generator.manager.TencentCOSManager;
import com.haitao.generator.service.ScreenShotService;
import com.haitao.generator.utils.ThrowUtils;
import com.haitao.generator.utils.WebScreenshotUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@Slf4j
public class ScreenShotServiceImpl implements ScreenShotService {

    @Resource
    private TencentCOSManager tencentCOSManager;

    @Override
    public String screenShotAndUpload(String webUrl) {
        // 参数校验
        ThrowUtils.throwIf(StrUtil.isBlank(webUrl), ErrorCode.PARAMS_ERROR, "要截取的网站不能为空");
        ThrowUtils.throwIf(!webUrl.startsWith("http://") && !webUrl.startsWith("https://"), ErrorCode.PARAMS_ERROR, "要截取的网站协议必须为http或https");
        //截图图片保存压缩图片到本地
        String compressPath = WebScreenshotUtils.saveWebPageScreenshot(webUrl);

        try {
            //上传图片到COS
            String fileName = UUID.randomUUID().toString().substring(0, 8) + "_compress.jpg";
            String key = generateScreenShotKey(fileName);
            String cosUrl = tencentCOSManager.uploadToCOS(compressPath, key);
            return cosUrl;
        } finally {
            cleanUpLocalFile(compressPath);
        }
    }

    /**
     * 生成上传到COS的key（即文件路径）
     *
     * @param fileName
     * @return
     */
    private String generateScreenShotKey(String fileName) {
        String datePath = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        return String.format("/screenshots/%s/%s", datePath, fileName);
    }

    /**
     * 删除本地的压缩图片
     * @param filePath
     */
    private void cleanUpLocalFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            FileUtil.del(file);
        }
    }
}
