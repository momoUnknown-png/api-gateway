package cn.bugstack.gateway.assist.security;

import cn.bugstack.gateway.assist.config.GatewayConfigCenter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class JwtValidator {
    private static SecretKey SECRET_KEY;
    
    public static void init(GatewayConfigCenter configCenter) {
        String secret = configCenter.getSecurityConfig("jwt.secret");
        SECRET_KEY = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
    
    public static boolean validate(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public static Set<String> getRoles(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
            
            String roles = claims.get("roles", String.class);
            if (roles == null || roles.isEmpty()) 
                return Collections.emptySet();
            
            return new HashSet<>(Arrays.asList(roles.split(",")));
        } catch (Exception e) {
            return Collections.emptySet();
        }
    }
}
