package cn.bugstack.gateway.assist.config;

import cn.bugstack.gateway.assist.domain.service.GatewayCenterService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

@Component
public class GatewayConfigCenter {

    private final RedisTemplate<String, String> redisTemplate;
    private final GatewayCenterService centerService;
    private final GatewayServiceProperties properties;
    
    public GatewayConfigCenter(RedisTemplate<String, String> redisTemplate,
                              GatewayCenterService centerService,
                              GatewayServiceProperties properties) {
        this.redisTemplate = redisTemplate;
        this.centerService = centerService;
        this.properties = properties;
    }
    
    @PostConstruct
    public void init() {
        refreshSecurityConfig();
    }
    
    public void refreshSecurityConfig() {
        Map<String, String> configs = centerService.loadSecurityConfig();
        configs.forEach((k, v) -> 
            redisTemplate.opsForValue().set("security:config:" + k, v)
        );
    }
    
    public String getSecurityConfig(String key) {
        return redisTemplate.opsForValue().get("security:config:" + key);
    }
}
