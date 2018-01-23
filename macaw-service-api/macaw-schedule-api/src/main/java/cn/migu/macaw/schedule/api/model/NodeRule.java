package cn.migu.macaw.schedule.api.model;

import cn.migu.macaw.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "NODE_RULE")
public class NodeRule extends BaseEntity
{
    
    /**
     * 名称
     */
    @Column(name = "NAME")
    private String name;
    
    /**
     * 编码
     */
    @Column(name = "CODE")
    private String code;
    
    /**
     * 本节点
     */
    @Column(name = "LOCAL_NODE")
    private String localNode;
    
    /**
     * 子节点编码
     */
    @Column(name = "NEXT_NODE")
    private String nextNode;
    
    @Column(name = "JOB_CODE")
    private String jobCode;
    
    /**
     * 说明
     */
    @Column(name = "NOTE")
    private String note;
    
    /**
     * 获取名称
     *
     * @return NAME - 名称
     */
    public String getName()
    {
        return name;
    }
    
    /**
     * 设置名称
     *
     * @param name 名称
     */
    public void setName(String name)
    {
        this.name = name == null ? null : name.trim();
    }
    
    /**
     * 获取编码
     *
     * @return CODE - 编码
     */
    public String getCode()
    {
        return code;
    }
    
    /**
     * 设置编码
     *
     * @param code 编码
     */
    public void setCode(String code)
    {
        this.code = code == null ? null : code.trim();
    }
    
    /**
     * 获取本节点
     *
     * @return LOCAL_NODE - 本节点
     */
    public String getLocalNode()
    {
        return localNode;
    }
    
    /**
     * 设置本节点
     *
     * @param localNode 本节点
     */
    public void setLocalNode(String localNode)
    {
        this.localNode = localNode == null ? null : localNode.trim();
    }
    
    /**
     * 获取子节点编码
     *
     * @return NEXT_NODE - 子节点编码
     */
    public String getNextNode()
    {
        return nextNode;
    }
    
    /**
     * 设置子节点编码
     *
     * @param nextNode 子节点编码
     */
    public void setNextNode(String nextNode)
    {
        this.nextNode = nextNode == null ? null : nextNode.trim();
    }
    
    /**
     * @return JOB_CODE
     */
    public String getJobCode()
    {
        return jobCode;
    }
    
    /**
     * @param jobCode
     */
    public void setJobCode(String jobCode)
    {
        this.jobCode = jobCode == null ? null : jobCode.trim();
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