package cn.migu.macaw.schedule.task.datasource;

/**
 * 数据源属性
 * 
 * @author  zhaocan
 * @version  [版本号, 2016年11月15日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class DataSourceFlatAttr
{
    //spark master地址
    private String sparkMaster;
    
    //spark application查询ip
    private String sparkHistoryServerIp;
    
    //spark appication历史服务器端口号
    private String sparkHistoryServerPort;
    
    //hdfs文件系统前缀schema
    private String hdfsPrefixSchema;
    
    //hdfs HA配置json字符串
    private String hdfsHaConfJsonStr;

    //类型
    private String dsType;
    
    //连接地址
    private String connectAddr;

    //驱动名
    private String driverClass;
    
    //用户名
    private String username;
    
    //密码
    private String password;
    
    //redis ip
    private String redisHost;
    
    //redis密码
    private String redisPasswd;

    //端口
    private int port;

    //db索引
    private int dbIndex;
    
    //是否是spark数据源
    private boolean sparkDs = false;
    
    //消息系统对外url地址
    private String kafkaHttpUrl;
    
    //kafka zookeeper地址
    private String kafkaZkUrl;
    
    //kafka broke地址
    private String kafkaBrokeUrl;
    
    //ssh连接主机地址
    private String sshHost;
    
    //ssh连接用户名
    private String sshUser;
    
    //ssh连接密码
    private String sshPassword;
    
    ///////////数据同步(crossdata)时使用////////////
    //驱动名
    private String targetDriverClass;
    
    //数据类型
    private String targetDsType;
    
    //连接地址
    private String targetconnectAddr;
    
    //用户名
    private String targetUsername;
    
    //密码
    private String targetPassword;
    
    //hdfs地址路径schema
    private String targetHdfsPrefixSchema;
    
    /////////////////////////////////////////
    
    public String getSparkMaster()
    {
        return sparkMaster;
    }
    
    public void setSparkMaster(String sparkMaster)
    {
        this.sparkMaster = sparkMaster;
    }
    
    public String getSparkHistoryServerIp()
    {
        return sparkHistoryServerIp;
    }
    
    public void setSparkHistoryServerIp(String sparkHistoryServerIp)
    {
        this.sparkHistoryServerIp = sparkHistoryServerIp;
    }
    
    public String getSparkHistoryServerPort()
    {
        return sparkHistoryServerPort;
    }
    
    public void setSparkHistoryServerPort(String sparkHistoryServerPort)
    {
        this.sparkHistoryServerPort = sparkHistoryServerPort;
    }
    
    public String getHdfsPrefixSchema()
    {
        return hdfsPrefixSchema;
    }
    
    public void setHdfsPrefixSchema(String hdfsPrefixSchema)
    {
        this.hdfsPrefixSchema = hdfsPrefixSchema;
    }
    
    public String getHdfsHaConfJsonStr()
    {
        return hdfsHaConfJsonStr;
    }
    
    public void setHdfsHaConfJsonStr(String hdfsHaConfJsonStr)
    {
        this.hdfsHaConfJsonStr = hdfsHaConfJsonStr;
    }
    
    public String getDriverClass()
    {
        return driverClass;
    }
    
    public void setDriverClass(String driverClass)
    {
        this.driverClass = driverClass;
    }
    
    public String getConnectAddr()
    {
        return connectAddr;
    }
    
    public void setConnectAddr(String connectAddr)
    {
        this.connectAddr = connectAddr;
    }
    
    public String getUsername()
    {
        return username;
    }
    
    public void setUsername(String username)
    {
        this.username = username;
    }
    
    public String getPassword()
    {
        return password;
    }
    
    public void setPassword(String password)
    {
        this.password = password;
    }
    
    public boolean isSparkDs()
    {
        return sparkDs;
    }
    
    public void setSparkDs(boolean sparkDs)
    {
        this.sparkDs = sparkDs;
    }
    
    public String getKafkaHttpUrl()
    {
        return kafkaHttpUrl;
    }
    
    public void setKafkaHttpUrl(String kafkaHttpUrl)
    {
        this.kafkaHttpUrl = kafkaHttpUrl;
    }
    
    public String getKafkaZkUrl()
    {
        return kafkaZkUrl;
    }
    
    public void setKafkaZkUrl(String kafkaZkUrl)
    {
        this.kafkaZkUrl = kafkaZkUrl;
    }
    
    public String getKafkaBrokeUrl()
    {
        return kafkaBrokeUrl;
    }
    
    public void setKafkaBrokeUrl(String kafkaBrokeUrl)
    {
        this.kafkaBrokeUrl = kafkaBrokeUrl;
    }
    
    public String getTargetDriverClass()
    {
        return targetDriverClass;
    }
    
    public void setTargetDriverClass(String targetDriverClass)
    {
        this.targetDriverClass = targetDriverClass;
    }
    
    public String getTargetconnectAddr()
    {
        return targetconnectAddr;
    }
    
    public void setTargetconnectAddr(String targetconnectAddr)
    {
        this.targetconnectAddr = targetconnectAddr;
    }
    
    public String getTargetUsername()
    {
        return targetUsername;
    }
    
    public void setTargetUsername(String targetUsername)
    {
        this.targetUsername = targetUsername;
    }
    
    public String getTargetPassword()
    {
        return targetPassword;
    }
    
    public void setTargetPassword(String targetPassword)
    {
        this.targetPassword = targetPassword;
    }
    
    public String getTargetHdfsPrefixSchema()
    {
        return targetHdfsPrefixSchema;
    }
    
    public void setTargetHdfsPrefixSchema(String targetHdfsPrefixSchema)
    {
        this.targetHdfsPrefixSchema = targetHdfsPrefixSchema;
    }
    
    public String getDsType()
    {
        return dsType;
    }
    
    public void setDsType(String dsType)
    {
        this.dsType = dsType;
    }
    
    public String getTargetDsType()
    {
        return targetDsType;
    }
    
    public void setTargetDsType(String targetDsType)
    {
        this.targetDsType = targetDsType;
    }
    
    public int getPort()
    {
        return port;
    }
    
    public void setPort(int port)
    {
        this.port = port;
    }
    
    public int getDbIndex()
    {
        return dbIndex;
    }
    
    public void setDbIndex(int dbIndex)
    {
        this.dbIndex = dbIndex;
    }
    
    public String getRedisHost()
    {
        return redisHost;
    }
    
    public void setRedisHost(String redisHost)
    {
        this.redisHost = redisHost;
    }
    
    public String getRedisPasswd()
    {
        return redisPasswd;
    }
    
    public void setRedisPasswd(String redisPasswd)
    {
        this.redisPasswd = redisPasswd;
    }
    
    public String getSshHost()
    {
        return sshHost;
    }
    
    public void setSshHost(String sshHost)
    {
        this.sshHost = sshHost;
    }
    
    public String getSshUser()
    {
        return sshUser;
    }
    
    public void setSshUser(String sshUser)
    {
        this.sshUser = sshUser;
    }
    
    public String getSshPassword()
    {
        return sshPassword;
    }
    
    public void setSshPassword(String sshPassword)
    {
        this.sshPassword = sshPassword;
    }
    
}
