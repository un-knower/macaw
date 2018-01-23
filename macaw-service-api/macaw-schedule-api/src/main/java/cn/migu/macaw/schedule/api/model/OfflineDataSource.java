package cn.migu.macaw.schedule.api.model;

import cn.migu.macaw.common.entity.BaseEntity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "OFFLINE_DATA_SOURCE")
public class OfflineDataSource extends BaseEntity
{
    
    /**
     * 1：Spark，2：Ht，3：Oracle，4：FTP，5：HDFS，6：GP，7：Mysql，8：Kafka  , 9 : SFTP  , 10  : SSH
     */
    @Column(name = "KIND")
    private Integer kind;
    
    /**
     * 名称
     */
    @Column(name = "NAME")
    private String name;
    
    /**
     * 地址
     */
    @Column(name = "ADDRESS")
    private String address;
    
    /**
     * 端口
     */
    @Column(name = "PORT")
    private Integer port;
    
    /**
     * 用户名
     */
    @Column(name = "USERNAME")
    private String username;
    
    /**
     * 密码
     */
    @Column(name = "PASSWORD")
    private String password;
    
    /**
     * 备用1
     */
    @Column(name = "RESERVE1")
    private String reserve1;
    
    /**
     * 备用2
     */
    @Column(name = "RESERVE2")
    private String reserve2;
    
    /**
     * 备用3
     */
    @Column(name = "RESERVE3")
    private String reserve3;
    
    /**
     * 备用4
     */
    @Column(name = "RESERVE4")
    private String reserve4;
    
    /**
     * 创建人
     */
    @Column(name = "CREATE_USER")
    private String createUser;
    
    /**
     * 创建时间
     */
    @Column(name = "CREATE_TIME")
    private Date createTime;
    
    /**
     * 修改人
     */
    @Column(name = "MODIFY_USER")
    private String modifyUser;
    
    /**
     * 修改时间
     */
    @Column(name = "MODIFY_TIME")
    private Date modifyTime;
    
    /**
     * 说明
     */
    @Column(name = "NOTE")
    private String note;
    
    /**
     * 获取1：Spark，2：Ht，3：Oracle，4：FTP，5：HDFS，6：GP，7：Mysql，8：Kafka  , 9 : SFTP  , 10  : SSH
     *
     * @return KIND - 1：Spark，2：Ht，3：Oracle，4：FTP，5：HDFS，6：GP，7：Mysql，8：Kafka  , 9 : SFTP  , 10  : SSH
     */
    public Integer getKind()
    {
        return kind;
    }
    
    /**
     * 设置1：Spark，2：Ht，3：Oracle，4：FTP，5：HDFS，6：GP，7：Mysql，8：Kafka  , 9 : SFTP  , 10  : SSH
     *
     * @param kind 1：Spark，2：Ht，3：Oracle，4：FTP，5：HDFS，6：GP，7：Mysql，8：Kafka  , 9 : SFTP  , 10  : SSH
     */
    public void setKind(Integer kind)
    {
        this.kind = kind;
    }
    
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
     * 获取地址
     *
     * @return ADDRESS - 地址
     */
    public String getAddress()
    {
        return address;
    }
    
    /**
     * 设置地址
     *
     * @param address 地址
     */
    public void setAddress(String address)
    {
        this.address = address == null ? null : address.trim();
    }
    
    /**
     * 获取端口
     *
     * @return PORT - 端口
     */
    public Integer getPort()
    {
        return port;
    }
    
    /**
     * 设置端口
     *
     * @param port 端口
     */
    public void setPort(Integer port)
    {
        this.port = port;
    }
    
    /**
     * 获取用户名
     *
     * @return USERNAME - 用户名
     */
    public String getUsername()
    {
        return username;
    }
    
    /**
     * 设置用户名
     *
     * @param username 用户名
     */
    public void setUsername(String username)
    {
        this.username = username == null ? null : username.trim();
    }
    
    /**
     * 获取密码
     *
     * @return PASSWORD - 密码
     */
    public String getPassword()
    {
        return password;
    }
    
    /**
     * 设置密码
     *
     * @param password 密码
     */
    public void setPassword(String password)
    {
        this.password = password == null ? null : password.trim();
    }
    
    /**
     * 获取备用1
     *
     * @return RESERVE1 - 备用1
     */
    public String getReserve1()
    {
        return reserve1;
    }
    
    /**
     * 设置备用1
     *
     * @param reserve1 备用1
     */
    public void setReserve1(String reserve1)
    {
        this.reserve1 = reserve1 == null ? null : reserve1.trim();
    }
    
    /**
     * 获取备用2
     *
     * @return RESERVE2 - 备用2
     */
    public String getReserve2()
    {
        return reserve2;
    }
    
    /**
     * 设置备用2
     *
     * @param reserve2 备用2
     */
    public void setReserve2(String reserve2)
    {
        this.reserve2 = reserve2 == null ? null : reserve2.trim();
    }
    
    /**
     * 获取备用3
     *
     * @return RESERVE3 - 备用3
     */
    public String getReserve3()
    {
        return reserve3;
    }
    
    /**
     * 设置备用3
     *
     * @param reserve3 备用3
     */
    public void setReserve3(String reserve3)
    {
        this.reserve3 = reserve3 == null ? null : reserve3.trim();
    }
    
    /**
     * 获取备用4
     *
     * @return RESERVE4 - 备用4
     */
    public String getReserve4()
    {
        return reserve4;
    }
    
    /**
     * 设置备用4
     *
     * @param reserve4 备用4
     */
    public void setReserve4(String reserve4)
    {
        this.reserve4 = reserve4 == null ? null : reserve4.trim();
    }
    
    /**
     * 获取创建人
     *
     * @return CREATE_USER - 创建人
     */
    public String getCreateUser()
    {
        return createUser;
    }
    
    /**
     * 设置创建人
     *
     * @param createUser 创建人
     */
    public void setCreateUser(String createUser)
    {
        this.createUser = createUser == null ? null : createUser.trim();
    }
    
    /**
     * 获取创建时间
     *
     * @return CREATE_TIME - 创建时间
     */
    public Date getCreateTime()
    {
        return createTime;
    }
    
    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }
    
    /**
     * 获取修改人
     *
     * @return MODIFY_USER - 修改人
     */
    public String getModifyUser()
    {
        return modifyUser;
    }
    
    /**
     * 设置修改人
     *
     * @param modifyUser 修改人
     */
    public void setModifyUser(String modifyUser)
    {
        this.modifyUser = modifyUser == null ? null : modifyUser.trim();
    }
    
    /**
     * 获取修改时间
     *
     * @return MODIFY_TIME - 修改时间
     */
    public Date getModifyTime()
    {
        return modifyTime;
    }
    
    /**
     * 设置修改时间
     *
     * @param modifyTime 修改时间
     */
    public void setModifyTime(Date modifyTime)
    {
        this.modifyTime = modifyTime;
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