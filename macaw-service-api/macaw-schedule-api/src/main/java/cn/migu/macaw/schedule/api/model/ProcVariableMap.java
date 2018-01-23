package cn.migu.macaw.schedule.api.model;

import cn.migu.macaw.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "PROC_VARIABLE_MAP")
public class ProcVariableMap extends BaseEntity
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
     * 变量名
     */
    @Column(name = "VARI_CODE")
    private String variCode;
    
    /**
     * 默认值
     */
    @Column(name = "DEFAULT_VALUE")
    private String defaultValue;
    
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
     * 获取变量名
     *
     * @return VARI_CODE - 变量名
     */
    public String getVariCode()
    {
        return variCode;
    }
    
    /**
     * 设置变量名
     *
     * @param variCode 变量名
     */
    public void setVariCode(String variCode)
    {
        this.variCode = variCode == null ? null : variCode.trim();
    }
    
    /**
     * 获取默认值
     *
     * @return DEFAULT_VALUE - 默认值
     */
    public String getDefaultValue()
    {
        return defaultValue;
    }
    
    /**
     * 设置默认值
     *
     * @param defaultValue 默认值
     */
    public void setDefaultValue(String defaultValue)
    {
        this.defaultValue = defaultValue == null ? null : defaultValue.trim();
    }
}