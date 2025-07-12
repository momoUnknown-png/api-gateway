package com.api.gateway.center;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * API网关管理中心启动类
 */
@SpringBootApplication
@EnableScheduling
@EnableAsync
public class ApiGatewayCenterApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayCenterApplication.class, args);
    }
} 
