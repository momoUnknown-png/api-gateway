package com.api.gateway.center.service;

import com.api.gateway.center.entity.GatewayNode;

import java.util.List;

/**
 * 网关节点服务接口
 */
public interface GatewayNodeService {
    
    /**
     * 注册网关节点
     */
    boolean registerNode(GatewayNode node);
    
    /**
     * 注销网关节点
     */
    boolean unregisterNode(String nodeId);
    
    /**
     * 更新节点状态
     */
    boolean updateNodeStatus(String nodeId, String status);
    
    /**
     * 更新节点负载信息
     */
    boolean updateNodeLoad(String nodeId, Double loadAverage, Double cpuUsage, Double memoryUsage);
    
    /**
     * 节点心跳
     */
    boolean heartbeat(String nodeId);
    
    /**
     * 获取所有在线节点
     */
    List<GatewayNode> getOnlineNodes();
    
    /**
     * 获取节点详情
     */
    GatewayNode getNodeById(String nodeId);
    
    /**
     * 获取所有节点
     */
    List<GatewayNode> getAllNodes();
    
    /**
     * 检查离线节点
     */
    void checkOfflineNodes();
} 
