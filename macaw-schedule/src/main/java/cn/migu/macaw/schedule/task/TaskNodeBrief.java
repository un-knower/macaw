package cn.migu.macaw.schedule.task;

/**
 * task node key
 * 
 * @author  soy
 */
public class TaskNodeBrief
{
    /**
     * 任务编码
     */
    private String jobCode;

    /**
     * task节点id
     */
    private String nodeId;

    /**
     * task编码
     */
    private String taskCode;

    /**
     * task class类型
     */
    private String taskClassType;

    /**
     * 批次号
     */
    private String batchCode;
    
    public TaskNodeBrief()
    {
        
    }
    
    public TaskNodeBrief(String taskCode, String taskClassType)
    {
        this.taskCode = taskCode;
        this.taskClassType = taskClassType;
    }
    
    public TaskNodeBrief(String jobCode, String nodeId, String taskCode, String batchCode)
    {
        this.jobCode = jobCode;
        this.nodeId = nodeId;
        this.taskCode = taskCode;
        this.batchCode = batchCode;
    }
    
    public String getJobCode()
    {
        return jobCode;
    }
    
    public void setJobCode(String jobCode)
    {
        this.jobCode = jobCode;
    }
    
    public String getNodeId()
    {
        return nodeId;
    }
    
    public void setNodeId(String nodeId)
    {
        this.nodeId = nodeId;
    }
    
    public String getTaskCode()
    {
        return taskCode;
    }
    
    public void setTaskCode(String taskCode)
    {
        this.taskCode = taskCode;
    }
    
    public String getBatchCode()
    {
        return batchCode;
    }
    
    public void setBatchCode(String batchCode)
    {
        this.batchCode = batchCode;
    }
    
    public String getTaskClassType()
    {
        return taskClassType;
    }
    
    public void setTaskClassType(String taskClassType)
    {
        this.taskClassType = taskClassType;
    }
    
}
