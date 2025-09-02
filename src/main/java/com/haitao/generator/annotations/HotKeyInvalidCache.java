package com.haitao.generator.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * hotkey结合分布式缓存redis
 * 失效缓存
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface HotKeyInvalidCache {

    String prefix() default "";
    String key() default "";
}
