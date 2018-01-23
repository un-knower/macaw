package cn.migu.macaw.schedule.api.model;

import cn.migu.macaw.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "PROC_SQL")
public class ProcSql extends BaseEntity
{
    
    /**
     * job编码
     */
    @Column(name = "JOB_CODE")
    private String jobCode;
    
    /**
     * 过程编码
     */
    @Column(name = "PROC_CODE")
    private String procCode;
    
    /**
     * task编码
     */
    @Column(name = "TASK_CODE")
    private String taskCode;
    
    /**
     * node编码
     */
    @Column(name = "NODE_CODE")
    private String nodeCode;
    
    /**
     * 父节点
     */
    @Column(name = "PARENT_CODE")
    private String parentCode;
    
    /**
     * 0-终断标识（return）,1-无返回sql，2-返回sql，3-条件判断，4-数值运算，5-注释，6-循环等待
     */
    @Column(name = "KIND")
    private Integer kind;
    
    /**
     * sql文本
     */
    @Column(name = "SQL_TEXT")
    private String sqlText;
    
    /**
     * 说明
     */
    @Column(name = "NOTE")
    private String note;
    
    /**
     * 获取job编码
     *
     * @return JOB_CODE - job编码
     */
    public String getJobCode()
    {
        return jobCode;
    }
    
    /**
     * 设置job编码
     *
     * @param jobCode job编码
     */
    public void setJobCode(String jobCode)
    {
        this.jobCode = jobCode == null ? null : jobCode.trim();
    }
    
    /**
     * 获取过程编码
     *
     * @return PROC_CODE - 过程编码
     */
    public String getProcCode()
    {
        return procCode;
    }
    
    /**
     * 设置过程编码
     *
     * @param procCode 过程编码
     */
    public void setProcCode(String procCode)
    {
        this.procCode = procCode == null ? null : procCode.trim();
    }
    
    /**
     * 获取task编码
     *
     * @return TASK_CODE - task编码
     */
    public String getTaskCode()
    {
        return taskCode;
    }
    
    /**
     * 设置task编码
     *
     * @param taskCode task编码
     */
    public void setTaskCode(String taskCode)
    {
        this.taskCode = taskCode == null ? null : taskCode.trim();
    }
    
    /**
     * 获取node编码
     *
     * @return NODE_CODE - node编码
     */
    public String getNodeCode()
    {
        return nodeCode;
    }
    
    /**
     * 设置node编码
     *
     * @param nodeCode node编码
     */
    public void setNodeCode(String nodeCode)
    {
        this.nodeCode = nodeCode == null ? null : nodeCode.trim();
    }
    
    /**
     * 获取父节点
     *
     * @return PARENT_CODE - 父节点
     */
    public String getParentCode()
    {
        return parentCode;
    }
    
    /**
     * 设置父节点
     *
     * @param parentCode 父节点
     */
    public void setParentCode(String parentCode)
    {
        this.parentCode = parentCode == null ? null : parentCode.trim();
    }
    
    /**
     * 获取0-终断标识（return）,1-无返回sql，2-返回sql，3-条件判断，4-数值运算，5-注释，6-循环等待
     *
     * @return KIND - 0-终断标识（return）,1-无返回sql，2-返回sql，3-条件判断，4-数值运算，5-注释，6-循环等待
     */
    public Integer getKind()
    {
        return kind;
    }
    
    /**
     * 设置0-终断标识（return）,1-无返回sql，2-返回sql，3-条件判断，4-数值运算，5-注释，6-循环等待
     *
     * @param kind 0-终断标识（return）,1-无返回sql，2-返回sql，3-条件判断，4-数值运算，5-注释，6-循环等待
     */
    public void setKind(Integer kind)
    {
        this.kind = kind;
    }
    
    /**
     * 获取sql文本
     *
     * @return SQL_TEXT - sql文本
     */
    public String getSqlText()
    {
        return sqlText;
    }
    
    /**
     * 设置sql文本
     *
     * @param sqlText sql文本
     */
    public void setSqlText(String sqlText)
    {
        this.sqlText = sqlText == null ? null : sqlText.trim();
    }
    
    /**
     * 获取说明
     *
     * @return NOTE - 说明
     */
    public String getNote()
    {
        return note;
    }
    
    /**
     * 设置说明
     *
     * @param note 说明
     */
    public void setNote(String note)
    {
        this.note = note == null ? null : note.trim();
    }
}