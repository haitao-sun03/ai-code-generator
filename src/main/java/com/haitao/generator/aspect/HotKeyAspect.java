package com.haitao.generator.aspect;

import cn.hutool.core.collection.CollUtil;
import com.haitao.generator.annotations.HotKeyCached;
import com.haitao.generator.annotations.HotKeyInvalidCache;
import com.haitao.generator.model.ApiResponse;
import com.haitao.generator.model.entity.ChatHistory;
import com.jd.platform.hotkey.client.callback.JdHotKeyStore;
import com.mybatisflex.core.paginate.Page;
import jakarta.annotation.Resource;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
public class HotKeyAspect {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     *
     * @param joinPoint
     * @param hotKeyCached
     * @return
     * @throws Throwable
     */
    @Around("@annotation(hotKeyCached)")
    public Object doInterceptorHotKeyCached(ProceedingJoinPoint joinPoint, HotKeyCached hotKeyCached) throws Throwable {
        System.out.println("Key Serializer: " + redisTemplate.getKeySerializer().getClass().getName());
        String prefix = hotKeyCached.prefix();
        String keyExpression = hotKeyCached.key();

        // 获取方法签名和参数
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] paramNames = signature.getParameterNames();
        Object[] paramValues = joinPoint.getArgs();

        // 创建 SpEL 解析器和上下文
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();
        for (int i = 0; i < paramNames.length; i++) {
            context.setVariable(paramNames[i], paramValues[i]);
        }

        // 解析 SpEL 表达式生成最终 key
        String finalKey = parser.parseExpression("'" + prefix + "' + " + keyExpression)
                .getValue(context, String.class);

        // 打印或使用 finalKey（例如检查热点缓存）
        System.out.println("Generated cache key: " + finalKey);
        if (JdHotKeyStore.isHotKey(finalKey)) {
            Object object = redisTemplate.opsForValue().get(finalKey);
            if (object != null) {
                return (ApiResponse<Page<ChatHistory>>) object;
            }
        }
        ApiResponse<Page<ChatHistory>> result = (ApiResponse<Page<ChatHistory>>) joinPoint.proceed();

        //若是热key，设置入本地缓存
        if (JdHotKeyStore.isHotKey(finalKey)) {
            redisTemplate.opsForValue().set(finalKey, result, 10, TimeUnit.MINUTES);
        }

        return result;
    }

    @Around("@annotation(hotKeyInvalidCache)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, HotKeyInvalidCache hotKeyInvalidCache) throws Throwable {
        String prefix = hotKeyInvalidCache.prefix();
        String keyExpression = hotKeyInvalidCache.key();

        // 获取方法签名和参数
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] paramNames = signature.getParameterNames();
        Object[] paramValues = joinPoint.getArgs();

        // 创建 SpEL 解析器和上下文
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();
        for (int i = 0; i < paramNames.length; i++) {
            context.setVariable(paramNames[i], paramValues[i]);
        }

        // 解析 SpEL 表达式生成最终 key
        String redisKeyPrefix = parser.parseExpression("'" + prefix + "' + " + keyExpression)
                .getValue(context, String.class);

        // 使用 SCAN 命令查找并删除匹配的 Redis 键
        try {
            ScanOptions scanOptions = ScanOptions.scanOptions().match(redisKeyPrefix + "*").count(100).build();
            Set<String> keys = redisTemplate.keys(redisKeyPrefix + "*"); // 备选方案
            if (CollUtil.isNotEmpty(keys)) {
                redisTemplate.delete(keys);
            }
        } catch (Exception e) {
            System.err.println("Redis delete keys failed: " + e.getMessage());
        }

        // 执行目标方法
        return joinPoint.proceed();

    }
}
