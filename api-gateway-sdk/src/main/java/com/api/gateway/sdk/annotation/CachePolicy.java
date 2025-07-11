package com.api.gateway.sdk.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 缓存策略注解
 * 新增功能：
 * 1. 声明式缓存控制
 * 2. 多维度缓存配置
 * 3. 条件缓存支持
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CachePolicy {
    /**
     * 是否启用缓存
     */
    boolean enabled() default true;
    
    /**
     * 缓存时间
     */
    int duration() default 30;
    
    /**
     * 时间单位
     */
    TimeUnit unit() default TimeUnit.SECONDS;
    
    /**
     * 缓存键生成规则
     */
    String keyPattern() default "";
    
    /**
     * 条件缓存表达式
     */
    String condition() default "";
}