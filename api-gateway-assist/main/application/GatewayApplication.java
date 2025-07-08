package cn.bugstack.gateway.assist.application;

import cn.bugstack.gateway.assist.config.GatewayServiceProperties;
import cn.bugstack.gateway.assist.domain.model.aggregates.ApplicationSystemRichInfo;
import cn.bugstack.gateway.assist.domain.model.vo.ApplicationInterfaceMethodVO;
import cn.bugstack.gateway.assist.domain.model.vo.ApplicationInterfaceVO;
import cn.bugstack.gateway.assist.domain.model.vo.ApplicationSystemVO;
import cn.bugstack.gateway.assist.domain.service.GatewayCenterService;
import cn.bugstack.gateway.assist.security.SecurityFilterChain;
import cn.bugstack.gateway.core.mapping.HttpCommandType;
import cn.bugstack.gateway.core.mapping.HttpStatement;
import cn.bugstack.gateway.core.session.Configuration;
import com.alibaba.fastjson.JSON;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

import java.util.List;
import java.util.function.BiPredicate;

public class GatewayApplication implements ApplicationContextAware, ApplicationListener<ContextClosedEvent> {

    private final Logger logger = LoggerFactory.getLogger(GatewayApplication.class);

    private final GatewayServiceProperties properties;
    private final GatewayCenterService gatewayCenterService;
    private final Configuration configuration;
    private final Channel gatewaySocketServerChannel;
    private final SecurityFilterChain securityFilterChain;

    public GatewayApplication(GatewayServiceProperties properties, 
                             GatewayCenterService gatewayCenterService, 
                             Configuration configuration, 
                             Channel gatewaySocketServerChannel,
                             SecurityFilterChain securityFilterChain) {
        this.properties = properties;
        this.gatewayCenterService = gatewayCenterService;
        this.configuration = configuration;
        this.gatewaySocketServerChannel = gatewaySocketServerChannel;
        this.securityFilterChain = securityFilterChain;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        try {
            // 1. 注册网关服务
            gatewayCenterService.doRegister(properties.getAddress(),
                    properties.getGroupId(),
                    properties.getGatewayId(),
                    properties.getGatewayName(),
                    properties.getGatewayAddress());
            
            // 2. 设置安全过滤器
            configuration.setSecurityFilter(new BiPredicate<FullHttpRequest, Configuration>() {
                @Override
                public boolean test(FullHttpRequest request, Configuration configuration) {
                    return securityFilterChain.doFilter(request, configuration);
                }
            });
            
            addMappers("");
        } catch (Exception e) {
            logger.error("网关服务启动失败，停止服务。{}", e.getMessage(), e);
            throw e;
        }
    }

    public void addMappers(String systemId) {
        // 2. 拉取网关配置
        ApplicationSystemRichInfo applicationSystemRichInfo = gatewayCenterService.pullApplicationSystemRichInfo(properties.getAddress(), properties.getGatewayId(), systemId);
        List<ApplicationSystemVO> applicationSystemVOList = applicationSystemRichInfo.getApplicationSystemVOList();
        if (applicationSystemVOList.isEmpty()) {
            logger.warn("网关{}服务注册映射为空，请排查 gatewayCenterService.pullApplicationSystemRichInfo 是否检索到此网关算力需要拉取的配置数据。", systemId);
            return;
        }
        for (ApplicationSystemVO system : applicationSystemVOList) {
            List<ApplicationInterfaceVO> interfaceList = system.getInterfaceList();
            for (ApplicationInterfaceVO itf : interfaceList) {
                // 2.1 创建配置信息加载注册
                configuration.registryConfig(system.getSystemId(), system.getSystemRegistry(), itf.getInterfaceId(), itf.getInterfaceVersion());
                List<ApplicationInterfaceMethodVO> methodList = itf.getMethodList();
                // 2.2 注册系统服务接口信息
                for (ApplicationInterfaceMethodVO method : methodList) {
                    HttpStatement httpStatement = new HttpStatement(
                            system.getSystemId(),
                            itf.getInterfaceId(),
                            method.getMethodId(),
                            method.getParameterType(),
                            method.getUri(),
                            HttpCommandType.valueOf(method.getHttpCommandType()),
                            method.isAuth(),
                            parseRoles(method.getRequiredRoles()));
                    configuration.addMapper(httpStatement);
                    logger.info("网关服务注册映射 系统：{} 接口：{} 方法：{}", system.getSystemId(), itf.getInterfaceId(), method.getMethodId());
                }
            }
        }
    }

    private String[] parseRoles(String roles) {
        if (roles == null || roles.isEmpty()) return new String[0];
        return roles.split(",");
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        try {
            if (gatewaySocketServerChannel.isActive()) {
                logger.info("应用容器关闭，Api网关服务关闭。localAddress：{}", gatewaySocketServerChannel.localAddress());
                gatewaySocketServerChannel.close();
            }
        } catch (Exception e) {
            logger.error("应用容器关闭，Api网关服务关闭失败", e);
        }
    }

    public void receiveMessage(Object message) {
        logger.info("【事件通知】接收注册中心推送消息 message：{}", message);
        addMappers(message.toString().substring(1, message.toString().length() - 1));
    }
}
