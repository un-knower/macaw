package cn.migu.macaw.sparkdrivermgr.model;

/**
 * 提交的spark job元数据
 *
 * @author soy
 */
public class SparkJobMetaData
{
    /**
     * spark job appid
     */
    private String appId;

    /**
     * spark job app名称
     */
    private String appName;

    /**
     * spark driver ip地址
     */
    private String driverIp;

    /**
     * spark driver端口
     */
    private int port;

    /**
     * spark driver业务运行类型
     */
    private String driverType;

    /**
     * 对应进程表主键
     */
    private String processId;

    /**
     * spark资源核数
     */
    private int coreNum;

    /**
     * spark executor内存大小
     */
    private int memSize;

    /**
     * 调度任务编码
     */
    private String jobCode;

    /**
     * 运行批次号
     */
    private String batchNo;

    /**
     * 调度流程节点描述
     */
    private String taskCode;

    /**
     * spark session是否已创建成功
     */
    private boolean resAlloc;

    public String getAppId()
    {
        return appId;
    }

    public void setAppId(String appId)
    {
        this.appId = appId;
    }

    public String getAppName()
    {
        return appName;
    }

    public void setAppName(String appName)
    {
        this.appName = appName;
    }

    public String getDriverIp()
    {
        return driverIp;
    }

    public void setDriverIp(String driverIp)
    {
        this.driverIp = driverIp;
    }

    public int getPort()
    {
        return port;
    }

    public void setPort(int port)
    {
        this.port = port;
    }

    public String getDriverType()
    {
        return driverType;
    }

    public void setDriverType(String driverType)
    {
        this.driverType = driverType;
    }

    public String getProcessId()
    {
        return processId;
    }

    public void setProcessId(String processId)
    {
        this.processId = processId;
    }

    public int getCoreNum()
    {
        return coreNum;
    }

    public void setCoreNum(int coreNum)
    {
        this.coreNum = coreNum;
    }

    public int getMemSize()
    {
        return memSize;
    }

    public void setMemSize(int memSize)
    {
        this.memSize = memSize;
    }

    public String getJobCode()
    {
        return jobCode;
    }

    public void setJobCode(String jobCode)
    {
        this.jobCode = jobCode;
    }

    public String getBatchNo()
    {
        return batchNo;
    }

    public void setBatchNo(String batchNo)
    {
        this.batchNo = batchNo;
    }

    public String getTaskCode()
    {
        return taskCode;
    }

    public void setTaskCode(String taskCode)
    {
        this.taskCode = taskCode;
    }

    public boolean isResAlloc()
    {
        return resAlloc;
    }

    public void setResAlloc(boolean resAlloc)
    {
        this.resAlloc = resAlloc;
    }
}
