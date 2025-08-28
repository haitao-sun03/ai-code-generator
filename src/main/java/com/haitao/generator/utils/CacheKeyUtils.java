package com.haitao.generator.utils;

import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 缓存key生成工具类
 *
 * @author haitao
 */
@Slf4j
public class CacheKeyUtils {

    private CacheKeyUtils(){}

    /**
     * 根据对象生成缓存key (JSON + MD5)
     *
     * @param obj 要生成key的对象
     * @return MD5哈希后的缓存key
     */
    public static String generateKey(Object obj) {
        if (obj == null) {
            String key = DigestUtil.md5Hex("null");
            log.debug("生成缓存key for null: {}", key);
            return key;
        }
        
        try {
            // 先转JSON，再MD5
            String jsonStr = JSONUtil.toJsonStr(obj);
            String key = DigestUtil.md5Hex(jsonStr);
            log.debug("生成缓存key for object {}: {} -> {}", obj.getClass().getSimpleName(), jsonStr, key);
            return key;
        } catch (Exception e) {
            log.error("生成缓存key失败，对象: {}", obj, e);
            // 降级处理：使用对象的toString + hashCode
            String fallbackStr = obj.getClass().getName() + ":" + obj.toString() + ":" + obj.hashCode();
            String key = DigestUtil.md5Hex(fallbackStr);
            log.warn("使用降级策略生成缓存key: {}", key);
            return key;
        }
    }
}
