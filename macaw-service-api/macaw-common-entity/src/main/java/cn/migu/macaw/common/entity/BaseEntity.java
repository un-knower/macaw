package cn.migu.macaw.common.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

public class BaseEntity
{
    @Id
    @Column(name = "OBJ_ID")
    @GeneratedValue(generator = "UUID")
    private String objId;
    
    @Transient
    private Integer page = 1;
    
    @Transient
    private Integer rows = 10;
    
    public String getObjId()
    {
        return objId;
    }
    
    public void setObjId(String objId)
    {
        this.objId = objId;
    }
    
    public Integer getPage()
    {
        return page;
    }
    
    public void setPage(Integer page)
    {
        this.page = page;
    }
    
    public Integer getRows()
    {
        return rows;
    }
    
    public void setRows(Integer rows)
    {
        this.rows = rows;
    }
}
