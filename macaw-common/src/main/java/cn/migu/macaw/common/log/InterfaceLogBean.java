package cn.migu.macaw.common.log;

/**
 * 接口日志bean
 * @author soy
 */
public class InterfaceLogBean
{
    private String reqResIdent;

    private String uniqueId;

    private String interfaceSrc;

    private String interfaceName;

    private String systemModuleName;

    private String message;

    private String userId;

    private String returnInfo;

    public String getReqResIdent()
    {
        return reqResIdent;
    }

    public void setReqResIdent(String reqResIdent)
    {
        this.reqResIdent = reqResIdent;
    }

    public String getInterfaceName()
    {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName)
    {
        this.interfaceName = interfaceName;
    }

    public String getSystemModuleName()
    {
        return systemModuleName;
    }

    public void setSystemModuleName(String systemModuleName)
    {
        this.systemModuleName = systemModuleName;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getReturnInfo()
    {
        return returnInfo;
    }

    public void setReturnInfo(String returnInfo)
    {
        this.returnInfo = returnInfo;
    }

    public String getUniqueId()
    {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId)
    {
        this.uniqueId = uniqueId;
    }

    public String getInterfaceSrc()
    {
        return interfaceSrc;
    }

    public void setInterfaceSrc(String interfaceSrc)
    {
        this.interfaceSrc = interfaceSrc;
    }
}
