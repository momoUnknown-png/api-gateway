package cn.bugstack.gateway.assist.security;

import cn.bugstack.gateway.assist.config.GatewayConfigCenter;
import cn.bugstack.gateway.core.mapping.HttpStatement;
import cn.bugstack.gateway.core.session.Configuration;
import io.netty.handler.codec.http.FullHttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class SecurityFilterChain {
    private static final Logger logger = LoggerFactory.getLogger(SecurityFilterChain.class);
    private static final String AUTH_HEADER = "Authorization";

    private final GatewayConfigCenter configCenter;
    
    public SecurityFilterChain(GatewayConfigCenter configCenter) {
        this.configCenter = configCenter;
    }
    
    public boolean doFilter(FullHttpRequest request, Configuration configuration) {
        try {
            String uri = request.uri().split("\\?")[0];
            HttpStatement httpStatement = configuration.getHttpStatement(uri);
            
            // 1. 接口无需认证直接放行
            if (!httpStatement.isAuth()) return true;
            
            // 2. 获取Token
            String token = request.headers().get(AUTH_HEADER);
            if (token == null || token.isEmpty()) {
                AuditLogger.logUnauthorized(request, "Missing token");
                return false;
            }
            
            // 3. 验证Token
            if (!JwtValidator.validate(token)) {
                AuditLogger.logInvalidToken(request);
                return false;
            }
            
            // 4. 角色验证
            String[] requiredRoles = httpStatement.getRequiredRoles();
            if (requiredRoles != null && requiredRoles.length > 0) {
                Set<String> requiredRoleSet = Arrays.stream(requiredRoles).collect(Collectors.toSet());
                Set<String> userRoles = JwtValidator.getRoles(token);
                if (!hasRequiredRole(requiredRoleSet, userRoles)) {
                    AuditLogger.logForbidden(request);
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            logger.error("Security filter error: {}", e.getMessage(), e);
            return false;
        }
    }
    
    private boolean hasRequiredRole(Set<String> requiredRoles, Set<String> userRoles) {
        return userRoles.stream().anyMatch(requiredRoles::contains);
    }
}
