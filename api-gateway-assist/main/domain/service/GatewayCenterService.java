package cn.bugstack.gateway.assist.domain.service;

import cn.bugstack.gateway.assist.GatewayException;
import cn.bugstack.gateway.assist.common.Result;
import cn.bugstack.gateway.assist.config.GatewayServiceProperties;
import cn.bugstack.gateway.assist.domain.model.aggregates.ApplicationSystemRichInfo;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class GatewayCenterService {

    private final Logger logger = LoggerFactory.getLogger(GatewayCenterService.class);
    private final GatewayServiceProperties properties;

    public GatewayCenterService(GatewayServiceProperties properties) {
        this.properties = properties;
    }

    public void doRegister(String address, String groupId, String gatewayId, String gatewayName, String gatewayAddress) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("groupId", groupId);
        paramMap.put("gatewayId", gatewayId);
        paramMap.put("gatewayName", gatewayName);
        paramMap.put("gatewayAddress", gatewayAddress);
        String resultStr;
        try {
            resultStr = HttpUtil.post(address + "/wg/admin/config/registerGateway", paramMap, 1550);
        } catch (Exception e) {
            logger.error("网关服务注册异常，链接资源不可用：{}", address + "/wg/admin/config/registerGateway");
            throw e;
        }
        Result<Boolean> result = JSON.parseObject(resultStr, new TypeReference<Result<Boolean>>() {
        });
        logger.info("向网关中心注册网关算力服务 gatewayId：{} gatewayName：{} gatewayAddress：{} 注册结果：{}", gatewayId, gatewayName, gatewayAddress, resultStr);
        if (!"0000".equals(result.getCode()))
            throw new GatewayException("网关服务注册异常 [gatewayId：" + gatewayId + "] 、[gatewayAddress：" + gatewayAddress + "]");
    }

    public ApplicationSystemRichInfo pullApplicationSystemRichInfo(String address, String gatewayId, String systemId) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("gatewayId", gatewayId);
        paramMap.put("systemId", systemId);
        String resultStr;
        try {
            resultStr = HttpUtil.post(address + "/wg/admin/config/queryApplicationSystemRichInfo", paramMap, 1550);
        } catch (Exception e) {
            logger.error("网关服务拉取异常，链接资源不可用：{}", address + "/wg/admin/config/queryApplicationSystemRichInfo", e);
            throw e;
        }
        Result<ApplicationSystemRichInfo> result = JSON.parseObject(resultStr, new TypeReference<Result<ApplicationSystemRichInfo>>() {
        });
        logger.info("从网关中心拉取应用服务和接口的配置信息到本地完成注册。gatewayId：{}", gatewayId);
        if (!"0000".equals(result.getCode()))
            throw new GatewayException("从网关中心拉取应用服务和接口的配置信息到本地完成注册异常 [gatewayId：" + gatewayId + "]");
        return result.getData();
    }

    public Map<String, String> queryRedisConfig(String address) {
        String resultStr;
        try {
            resultStr = HttpUtil.post(address + "/wg/admin/config/queryRedisConfig", "", 1550);
        } catch (Exception e) {
            logger.error("网关服务拉取配置异常，链接资源不可用：{}", address + "/wg/admin/config/queryRedisConfig", e);
            throw e;
        }
        Result<Map<String, String>> result = JSON.parseObject(resultStr, new TypeReference<Result<Map<String, String>>>() {
        });
        logger.info("从网关中心拉取Redis配置信息完成。result：{}", resultStr);
        if (!"0000".equals(result.getCode()))
            throw new GatewayException("从网关中心拉取Redis配置信息异常");
        return result.getData();
    }
    
    public Map<String, String> loadSecurityConfig() {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("gatewayId", properties.getGatewayId());
        String resultStr;
        try {
            resultStr = HttpUtil.post(properties.getAddress() + "/wg/admin/config/security", paramMap, 1000);
        } catch (Exception e) {
            logger.error("Load security config error: {}", e.getMessage(), e);
            throw new GatewayException("Load security config failed");
        }
        
        Result<Map<String, String>> result = JSON.parseObject(
            resultStr, new TypeReference<Result<Map<String, String>>>() {});
        
        if (!"0000".equals(result.getCode())) {
            throw new GatewayException("Load security config error: " + result.getInfo());
        }
        return result.getData();
    }
}
