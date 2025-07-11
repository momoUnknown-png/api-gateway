package cn.bugstack.gateway.assist.domain.model.vo;

import java.util.List;

public class ApplicationSystemVO {

    /** 系统标识 */
    private String systemId;
    /** 系统名称 */
    private String systemName;
    /** 系统类型 */
    private String systemType;
    /** 注册中心 */
    private String systemRegistry;
    /** 接口方法 */
    private List<ApplicationInterfaceVO> interfaceList;

    // Getters and Setters
    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getSystemType() {
        return systemType;
    }

    public void setSystemType(String systemType) {
        this.systemType = systemType;
    }

    public String getSystemRegistry() {
        return systemRegistry;
    }

    public void setSystemRegistry(String systemRegistry) {
        this.systemRegistry = systemRegistry;
    }

    public List<ApplicationInterfaceVO> getInterfaceList() {
        return interfaceList;
    }

    public void setInterfaceList(List<ApplicationInterfaceVO> interfaceList) {
        this.interfaceList = interfaceList;
    }
}
