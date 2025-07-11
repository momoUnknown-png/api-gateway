

//修改后代码
package com.api.gateway.sdk.annotation;

import java.lang.annotation.*;

/**
 * 增强版方法注解
 * 改进点：
 * 1. 使用枚举确保HTTP方法安全
 * 2. 分离安全控制和流量控制
 * 3. 结构化参数映射配置
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnhancedMethod {
    /**
     * API路径 (对应原path)
     */
    String endpoint();
    
    /**
     * HTTP方法 (类型安全改进)
     */
    HttpMethod[] methods() default {HttpMethod.POST};
    
    /**
     * 安全配置 (增强原requireAuth)
     */
    Security security() default @Security;
    
    /**
     * 流量控制 (增强原rateLimit)
     */
    RateLimit rate() default @RateLimit;
    
    /**
     * 参数映射 (增强原paramMapping)
     */
    Param[] params() default {};
    
    /**
     * 响应处理 (对应原responseMapping)
     */
    Response response() default @Response;

    enum HttpMethod {
        GET, POST, PUT, DELETE, PATCH, HEAD
    }

    @interface Security {
        boolean authRequired() default true;  // 对应原requireAuth
        String[] allowedRoles() default {};  // 新增角色控制
    }

    @interface RateLimit {
        int qps() default -1;       // 对应原rateLimit
        int burst() default 10;      // 新增突发流量控制
    }

    @interface Param {
        String from();              // 来源参数名
        String to();                // 目标参数名
        boolean required() default true;
    }

    @interface Response {
        String wrapper() default ""; // 对应原responseMapping
    }
}