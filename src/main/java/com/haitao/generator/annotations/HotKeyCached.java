package com.haitao.generator.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * hotkey结合分布式缓存redis
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface HotKeyCached {

    //热key前缀
    String prefix() default "$$undefined$$";


    //热key
    String key() default "$$undefined$$";

}
