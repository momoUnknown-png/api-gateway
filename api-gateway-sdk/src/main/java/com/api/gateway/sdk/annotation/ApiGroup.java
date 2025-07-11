package com.api.gateway.sdk.annotation;

import java.lang.annotation.*;

/**
 * API分组注解
 * 新增功能：
 * 1. 支持接口分类管理
 * 2. 多维度环境标记
 * 3. 生命周期管理
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiGroup {
    /**
     * 分组名称 (必填)
     */
    String value();
    
    /**
     * 环境标记 (新增)
     */
    Environment env() default Environment.PRODUCTION;
    
    /**
     * 接口状态 (新增)
     */
    ApiStatus status() default ApiStatus.STABLE;

    enum Environment {
        DEVELOPMENT, TESTING, STAGING, PRODUCTION
    }

    enum ApiStatus {
        EXPERIMENTAL, STABLE, DEPRECATED
    }
}