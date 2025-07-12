package com.api.gateway.center.controller;

import com.api.gateway.center.entity.GatewayNode;
import com.api.gateway.center.service.GatewayNodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 网关节点管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/gateway/node")
public class GatewayNodeController {
    
    @Autowired
    private GatewayNodeService gatewayNodeService;
    
    /**
     * 注册网关节点
     */
    @PostMapping("/register")
    public Map<String, Object> registerNode(@RequestBody GatewayNode node) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = gatewayNodeService.registerNode(node);
            if (success) {
                result.put("code", 200);
                result.put("message", "节点注册成功");
                result.put("data", node);
            } else {
                result.put("code", 500);
                result.put("message", "节点注册失败");
            }
        } catch (Exception e) {
            log.error("注册网关节点失败", e);
            result.put("code", 500);
            result.put("message", "节点注册失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 注销网关节点
     */
    @PostMapping("/unregister/{nodeId}")
    public Map<String, Object> unregisterNode(@PathVariable String nodeId) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = gatewayNodeService.unregisterNode(nodeId);
            if (success) {
                result.put("code", 200);
                result.put("message", "节点注销成功");
            } else {
                result.put("code", 500);
                result.put("message", "节点注销失败");
            }
        } catch (Exception e) {
            log.error("注销网关节点失败", e);
            result.put("code", 500);
            result.put("message", "节点注销失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 节点心跳
     */
    @PostMapping("/heartbeat/{nodeId}")
    public Map<String, Object> heartbeat(@PathVariable String nodeId) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = gatewayNodeService.heartbeat(nodeId);
            if (success) {
                result.put("code", 200);
                result.put("message", "心跳成功");
            } else {
                result.put("code", 500);
                result.put("message", "心跳失败");
            }
        } catch (Exception e) {
            log.error("节点心跳失败", e);
            result.put("code", 500);
            result.put("message", "心跳失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 更新节点负载信息
     */
    @PostMapping("/load/{nodeId}")
    public Map<String, Object> updateNodeLoad(
            @PathVariable String nodeId,
            @RequestBody Map<String, Double> loadInfo) {
        Map<String, Object> result = new HashMap<>();
        try {
            Double loadAverage = loadInfo.get("loadAverage");
            Double cpuUsage = loadInfo.get("cpuUsage");
            Double memoryUsage = loadInfo.get("memoryUsage");
            
            boolean success = gatewayNodeService.updateNodeLoad(nodeId, loadAverage, cpuUsage, memoryUsage);
            if (success) {
                result.put("code", 200);
                result.put("message", "负载信息更新成功");
            } else {
                result.put("code", 500);
                result.put("message", "负载信息更新失败");
            }
        } catch (Exception e) {
            log.error("更新节点负载信息失败", e);
            result.put("code", 500);
            result.put("message", "负载信息更新失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 获取所有在线节点
     */
    @GetMapping("/online")
    public Map<String, Object> getOnlineNodes() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<GatewayNode> nodes = gatewayNodeService.getOnlineNodes();
            result.put("code", 200);
            result.put("message", "获取成功");
            result.put("data", nodes);
        } catch (Exception e) {
            log.error("获取在线节点失败", e);
            result.put("code", 500);
            result.put("message", "获取失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 获取所有节点
     */
    @GetMapping("/all")
    public Map<String, Object> getAllNodes() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<GatewayNode> nodes = gatewayNodeService.getAllNodes();
            result.put("code", 200);
            result.put("message", "获取成功");
            result.put("data", nodes);
        } catch (Exception e) {
            log.error("获取所有节点失败", e);
            result.put("code", 500);
            result.put("message", "获取失败：" + e.getMessage());
        }
        return result;
    }
    
    /**
     * 获取节点详情
     */
    @GetMapping("/{nodeId}")
    public Map<String, Object> getNodeById(@PathVariable String nodeId) {
        Map<String, Object> result = new HashMap<>();
        try {
            GatewayNode node = gatewayNodeService.getNodeById(nodeId);
            if (node != null) {
                result.put("code", 200);
                result.put("message", "获取成功");
                result.put("data", node);
            } else {
                result.put("code", 404);
                result.put("message", "节点不存在");
            }
        } catch (Exception e) {
            log.error("获取节点详情失败", e);
            result.put("code", 500);
            result.put("message", "获取失败：" + e.getMessage());
        }
        return result;
    }
} 
