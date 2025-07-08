package cn.bugstack.gateway.assist.security;

import io.netty.handler.codec.http.FullHttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class AuditLogger {
    private static final Logger logger = LoggerFactory.getLogger("SECURITY_AUDIT");
    
    public static void logUnauthorized(FullHttpRequest request, String reason) {
        logger.warn("Unauthorized|IP:{},URI:{},Reason:{}", 
            getClientIp(request), request.uri(), reason);
    }
    
    public static void logForbidden(FullHttpRequest request) {
        logger.warn("Forbidden|IP:{},URI:{}", 
            getClientIp(request), request.uri());
    }
    
    public static void logInvalidToken(FullHttpRequest request) {
        logger.warn("InvalidToken|IP:{},URI:{}", 
            getClientIp(request), request.uri());
    }
    
    private static String getClientIp(FullHttpRequest request) {
        String xff = request.headers().get("X-Forwarded-For");
        if (xff != null && !xff.isEmpty()) {
            return xff.split(",")[0].trim();
        }
        return request.headers().get("Host");
    }
}