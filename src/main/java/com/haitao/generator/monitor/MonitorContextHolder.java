package com.haitao.generator.monitor;

import java.util.concurrent.Callable;

/**
 * 监控上下文持有者 - 支持跨线程传递
 */
public class MonitorContextHolder {
    
    // 使用 InheritableThreadLocal 支持父子线程间的上下文传递
    private static final ThreadLocal<MonitorContext> contextHolder = new InheritableThreadLocal<>();
    
    /**
     * 设置监控上下文
     */
    public static void setContext(MonitorContext context) {
        contextHolder.set(context);
    }
    
    /**
     * 获取监控上下文
     */
    public static MonitorContext getContext() {
        return contextHolder.get();
    }
    
    /**
     * 清除监控上下文
     */
    public static void clearContext() {
        contextHolder.remove();
    }
    
    /**
     * 包装Runnable以传递上下文
     */
    public static Runnable wrapRunnable(Runnable runnable) {
        MonitorContext context = getContext();
        return () -> {
            MonitorContext oldContext = getContext();
            try {
                setContext(context);
                runnable.run();
            } finally {
                setContext(oldContext);
            }
        };
    }
    
    /**
     * 包装Callable以传递上下文
     */
    public static <T> Callable<T> wrapCallable(Callable<T> callable) {
        MonitorContext context = getContext();
        return () -> {
            MonitorContext oldContext = getContext();
            try {
                setContext(context);
                return callable.call();
            } finally {
                setContext(oldContext);
            }
        };
    }
}