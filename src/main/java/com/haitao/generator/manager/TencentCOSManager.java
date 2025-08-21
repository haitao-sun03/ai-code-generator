package com.haitao.generator.manager;

import cn.hutool.core.util.StrUtil;
import com.haitao.generator.config.TencentCOSConfig;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@Slf4j
public class TencentCOSManager {

    @Resource
    private TencentCOSConfig tencentCOSConfig;

    @Resource
    private COSClient cosClient;


    /**
     * 上传文件到COS
     * @param localPath 要上传的本地对象路径
     * @param key 上传到存储桶的存储路径
     * @return COS的访问路径
     */
    public String uploadToCOS(String localPath, String key) {
        // 指定要上传的文件
        File localFile = new File(localPath);
        // 指定文件将要存放的存储桶
        // 指定文件上传到 COS 上的路径，即对象键。例如对象键为 folder/picture.jpg，则表示将文件 picture.jpg 上传到 folder 路径下
        PutObjectRequest putObjectRequest = new PutObjectRequest(tencentCOSConfig.getBucket(), key, localFile);
        PutObjectResult result = cosClient.putObject(putObjectRequest);
        if (result != null) {
            String url = StrUtil.format("{}/{}", tencentCOSConfig.getHost(), key);
            log.info("上传图片到COS成功，{}->{}", localPath, url);
            return url;
        }

        log.error("上传图片到COS成功，{}", localPath);
        return null;
    }
}
