package com.api.gateway.center.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 网关节点实体
 */
@Data
@TableName("gateway_node")
public class GatewayNode {
    
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 网关节点ID
     */
    private String nodeId;
    
    /**
     * 网关名称
     */
    private String nodeName;
    
    /**
     * 主机地址
     */
    private String host;
    
    /**
     * 端口号
     */
    private Integer port;
    
    /**
     * 状态：ONLINE-在线, OFFLINE-离线, MAINTENANCE-维护中
     */
    private String status;
    
    /**
     * 权重
     */
    private Integer weight;
    
    /**
     * 版本号
     */
    private String version;
    
    /**
     * 负载
     */
    private Double loadAverage;
    
    /**
     * CPU使用率
     */
    private Double cpuUsage;
    
    /**
     * 内存使用率
     */
    private Double memoryUsage;
    
    /**
     * 最后心跳时间
     */
    private LocalDateTime lastHeartbeat;
    
    /**
     * 注册时间
     */
    private LocalDateTime registerTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    
    /**
     * 是否删除
     */
    private Boolean deleted;
} 
