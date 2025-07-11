

//修改后代码
package com.api.gateway.sdk.annotation;

import java.lang.annotation.*;

/**
 * 增强版服务注册注解
 * 改进点：
 * 1. 使用嵌套注解替代扁平化参数
 * 2. 增加协议详细配置
 * 3. 支持多环境部署配置
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnhancedService {
    /**
     * 服务唯一ID (对应原serviceName)
     */
    String serviceId();
    
    /**
     * 服务版本控制 (增强版)
     */
    ServiceVersion version() default @ServiceVersion;
    
    /**
     * 调用策略配置 (整合原timeout/retries)
     */
    ServicePolicy policy() default @ServicePolicy;
    
    /**
     * 部署配置 (对应原group)
     */
    Deployment deployment() default @Deployment;
    
    /**
     * 协议配置 (增强原protocol)
     */
    ServiceProtocol protocol() default @ServiceProtocol;

    @interface ServiceVersion {
        String major() default "1";
        String minor() default "0";
        String patch() default "0";
    }

    @interface ServicePolicy {
        int timeoutMs() default 5000;  // 更明确的参数名
        int maxRetries() default 0;    // 增加重试策略
        boolean circuitBreak() default false;
    }

    @interface Deployment {
        String group() default "";     // 对应原group
        String region() default "";    // 新增区域标记
    }

    @interface ServiceProtocol {
        String type() default "dubbo"; // 对应原protocol
        String serializer() default "hessian2";
    }
}