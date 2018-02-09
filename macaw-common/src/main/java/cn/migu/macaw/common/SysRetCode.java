package cn.migu.macaw.common;

/**
 * 系统返回码
 * 
 * @author  soy
 */
public interface SysRetCode
{
    /**
     * 操作成功
     */
    String SUCCESS = "0000";

    /**
     * 输入参数不完整
     */
    String PARAM_INCOMPLETE = "0001";

    /**
     * 输入参数格式错误
     */
    String PARAM_FORMAT_ERR = "0002";

    /**
     * 输入参数类型错误
     */
    String PARAM_TYPE_ERR = "0003";

    /**
     * 服务器无法连接
     */
    String SERVER_NO_CONNECTED = "0004";

    /**
     * 服务器登录失败
     */
    String SERVER_LOGINON_FAILED = "0005";

    /**
     * 调用方法不存在
     */
    String METHOD_NOEXISTED = "0006";

    /**
     * Mysql异常
     */
    String MYSQL_EXCEPTION = "0088";

    /**
     * GP异常
     */
    String GP_EXCEPTION = "0089";

    /**
     * 网络异常
     */
    String NETWORK_EXCEPTION = "0090";

    /**
     * FTP异常
     */
    String FTP_EXCEPTION = "0091";

    /**
     * REDIS异常
     */
    String REDIS_EXCEPTION = "0092";

    /**
     * zookeeper异常
     */
    String ZOOKEEPER_EXCEPTION = "0093";

    /**
     * kafka异常
     */
    String KAFKA_EXCEPTION = "0094";

    /**
     * oracle异常
     */
    String ORACLE_EXCEPTION = "0095";

    /**
     * spark集群异常
     */
    String SPARK_EXCEPTION = "0096";

    /**
     * HugeTable数据仓库异常
     */
    String HT_EXCEPTION = "0097";

    /**
     * Hadoop集群异常
     */
    String HADOOP_EXCEPTION = "0098";

    /**
     * 应用系统异常
     */
    String APPSYS_EXCEPTION = "0099";
    
    //////////////////////////消息中心///////////////////

    ///////////////////////////////////////////////////////////////
    //////////////////任务调度中心/////////////////////////////////
    //
    /**
     * 任务名称不存在
     */
    String JOB_NAME_ABSENT = "0201";

    /**
     * job已经被触发执行
     */
    String JOB_ALREADY_TRIGGERED = "0202";

    /**
     * 触发器或任务类为空
     */
    String JOB_TRIGGER_OR_CLASS = "0203";
    
    ////////////////////////////////////////////////////////
    
    /////////////////spark driver资源管理中心/////////////////////////////////
    /**
     * spark核数为空
     */
    String SPARK_CORENUM_EMPTY = "0301";

    /**
     * spark内存大小为空
     */
    String SPARK_MEMSIZE_EMPTY = "0302";

    /**
     * spark核数非数字
     */
    String SPARK_CORENUM_NONNUMERIC = "0303";

    /**
     * spark内存大小非数字
     */
    String SPARK_MEMSIZE_NONNUMERIC = "0304";

    /**
     * spark核数范围不正确
     */
    String SPARK_CORENUM_OVERFLOW = "0305";

    /**
     * spark内存数范围不正确
     */
    String SPARK_MEMSIZE_OVERFLOW = "0306";

    /**
     * spark driver资源不足
     */
    String SPARK_DRIVER_INSUFFICIENT = "0307";

    /**
     * spark driver请求异常
     */
    String SPARK_DRIVER_REQUEST_ERROR = "0308";

    /**
     * 停止spark job失败
     */
    String SPARK_APP_STOP_FAIL = "0309";

    /**
     * spark job资源参数错误
     */
    String SPARK_RES_PARAM_ERROR = "0310";

    /**
     * spark job未找到
     */
    String SPARK_APP_NOT_FOUND = "0311";

    /**
     * spark job已完成
     */
    String SPARK_APP_FINISHED = "0312";

    /**
     * spark job运行中
     */
    String SPARK_APP_RUNNING = "0313";

    /**
     * spark job已停止
     */
    String SPARK_APP_KILLED = "0314";

    /**
     * spark app运行失败
     */
    String SPARK_APP_FAILED = "0315";

    /**
     * spark job创建失败
     */
    String SPARK_APP_CREATED_FAILED = "0316";

    /**
     * spark jar配置信息错误
     */
    String SPARK_JAR_CONFIG_ERROR = "0317";
    
    ////////////////////////////////////////////////////////
    
    /////////////监控中心////////////////////////////////////

    
    ///////////////////////////////////////////////////////
    
    /////////////spark sql/rdd计算中心///////////////////////////////////

    
    //////////////////////////////////////////////////////
    
    ///////////////spark机器学习算法中心////////////////////////////////

    
    ////////////////////////////////////////////////////

    ///////////////服务jar启动管理中心////////////////////////////////
    /**
     * 自定义ETL jar外部扩展功能不存在
     */
    String CUSTOM_ETL_JAR_EXT_FUNC_NOT_EXISTED = "0701";

    ////////////////////////////////////////////////////
    
    String ERROR = "ffff";
    

    
}
