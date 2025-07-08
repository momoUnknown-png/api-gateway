package cn.bugstack.gateway.assist.domain.model.vo;

public class ApplicationInterfaceMethodVO {

    /** 系统标识 */
    private String systemId;
    /** 接口标识 */
    private String interfaceId;
    /** 方法标识 */
    private String methodId;
    /** 方法名称 */
    private String methodName;
    /** 参数类型 */
    private String parameterType;
    /** 网关接口 */
    private String uri;
    /** 接口类型 */
    private String httpCommandType;
    /** 是否鉴权 */
    private Integer auth;
    /** 所需角色 */
    private String requiredRoles;

    // Getters and Setters

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public String getInterfaceId() {
        return interfaceId;
    }

    public void setInterfaceId(String interfaceId) {
        this.interfaceId = interfaceId;
    }

    public String getMethodId() {
        return methodId;
    }

    public void setMethodId(String methodId) {
        this.methodId = methodId;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getParameterType() {
        return parameterType;
    }

    public void setParameterType(String parameterType) {
        this.parameterType = parameterType;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getHttpCommandType() {
        return httpCommandType;
    }

    public void setHttpCommandType(String httpCommandType) {
        this.httpCommandType = httpCommandType;
    }

    public Integer getAuth() {
        return auth;
    }

    public boolean isAuth(){
        return auth == 1;
    }

    public void setAuth(Integer auth) {
        this.auth = auth;
    }

    public String getRequiredRoles() {
        return requiredRoles;
    }

    public void setRequiredRoles(String requiredRoles) {
        this.requiredRoles = requiredRoles;
    }
}