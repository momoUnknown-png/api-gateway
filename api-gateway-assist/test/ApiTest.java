package cn.bugstack.gateway.assist.test;

import cn.bugstack.gateway.assist.common.Result;
import cn.bugstack.gateway.assist.domain.model.aggregates.ApplicationSystemRichInfo;
import cn.bugstack.gateway.assist.security.JwtValidator;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class ApiTest {

    public static void main(String[] args) {
        System.out.println("Hi Api Gateway");
    }

    @Test
    public void test_register_gateway() {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("groupId", "10001");
        paramMap.put("gatewayId", "api-gateway-g4");
        paramMap.put("gatewayName", "电商配送网关");
        paramMap.put("gatewayAddress", "127.0.0.1");

        String resultStr = HttpUtil.post("http://localhost:8001/wg/admin/config/registerGateway", paramMap, 350);
        System.out.println(resultStr);

        Result result = JSON.parseObject(resultStr, Result.class);
        System.out.println(result.getCode());
    }

    @Test
    public void test_pullApplicationSystemRichInfo() {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("gatewayId", "api-gateway-g4");
        String resultStr = HttpUtil.post("http://172.20.10.12:8001/wg/admin/config/queryApplicationSystemRichInfo", paramMap, 1350);
        Result<ApplicationSystemRichInfo> result = JSON.parseObject(resultStr, new TypeReference<Result<ApplicationSystemRichInfo>>() {
        });
        System.out.println(JSON.toJSONString(result.getData()));
    }
    
    @Test
    public void test_security_config_load() {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("gatewayId", "api-gateway-g4");
        String resultStr = HttpUtil.post("http://localhost:8001/wg/admin/config/security", paramMap, 1000);
        System.out.println("Security config: " + resultStr);
    }
    
    @Test
    public void test_jwt_generation() {
        // 注意：实际使用时需要配置JWT密钥
        String token = JwtValidator.generateToken("user123", "admin,operator");
        System.out.println("Generated token: " + token);
    }
}
