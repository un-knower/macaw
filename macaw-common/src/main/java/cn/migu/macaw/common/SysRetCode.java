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

    /**
     * 丢失spark提交成功
     */
    String SPARK_SUBMIT_MISS_ERROR = "0318";
    
    ////////////////////////////////////////////////////////

    /////////////hadoop服务中心////////////////////////////////////
    /**
     * 数据同步功能不支持
     */
    String DATA_SYNC_FUNC_NOT_SUPPORT = "0401";

    /**
     * 数据同步参数解析错误
     */
    String DATA_SYNC_PARAM_PARSE_ERROR = "0402";

    /**
     * 数据同步配置创建失败
     */
    String DATA_SYNC_JOB_CONFIG_CREATE_FAILED = "0403";

    /**
     * 数据同步客户端创建失败
     */
    String DATA_SYNC_CLIENT_CREATE_FAILED = "0404";

    /**
     * 数据同步任务失败
     */
    String DATA_SYNC_JOB_FAILED = "0405";

    /**
     * 数据同步任务被停止
     */
    String DATA_SYNC_JOB_KILLED = "0406";

    /**
     * 停止数据同步任务失败
     */
    String DATA_SYNC_JOB_KILL_FAILED = "0407";

    /**
     * 数据同步任务id为空
     */
    String DATA_SYNC_JOB_ID_EMPTY = "0408";

    ///////////////////////////////////////////////////////

    
    /////////////监控中心////////////////////////////////////

    
    ///////////////////////////////////////////////////////
    
    /////////////spark sql/rdd计算中心///////////////////////////////////

    
    //////////////////////////////////////////////////////

    ///////////////服务jar启动管理中心////////////////////////////////
    /**
     * 自定义ETL jar外部扩展功能不存在
     */
    String CUSTOM_ETL_JAR_EXT_FUNC_NOT_EXISTED = "0701";

    /**
     * jar id为空
     */
    String JAR_ID_EMPTY = "0702";

    /**
     * 部署服务id为空
     */
    String DEPLOY_APP_ID_EMPTY = "0703";

    /**
     * 部署服务器id为空
     */
    String DEPLOY_SERVER_ID_EMPTY = "0704";

    /**
     * 部署路径为空
     */
    String DEPLOY_PATH_EMPTY = "0705";

    /**
     * jar类型错误
     */
    String DEPLOY_JAR_TYPE_ERROR = "0706";

    /**
     * jar名称为空
     */
    String DEPLOY_JAR_NAME_EMPTY = "0707";

    /**
     * 服务部署操作人为空
     */
    String DEPLOY_USER_EMPTY = "0708";

    /**
     * 部署服务端口号为空
     */
    String DEPLOY_JAR_PORT_EMPTY = "0709";

    /**
     * 数据采集时间为空
     */
    String DATA_COLLECT_TIME_EMPTY = "0710";

    /**
     * 启动参数解析错误
     */
    String BOOT_PARAM_PARSE_ERROR = "0711";

    /**
     * 部署主机信息不存在
     */
    String DEPLOY_HOST_NOT_EXISTED = "0712";

    /**
     * 登录主机用户名或密码错误
     */
    String LOGIN_USERNAME_OR_PASSWD_ERROR = "0713";

    /**
     * 执行shell命令异常
     */
    String EXECUTE_SHELL_EXCEPTION = "0714";

    /**
     * 端口被占用
     */
    String PORT_ALREADY_USED = "0715";

    /**
     * 原始文件路径错误
     */
    String SOURCE_PATH_ERROR = "0716";

    /**
     * 采集文件路径错误
     */
    String COLLECT_PATH_ERROR = "0717";

    /**
     * 输入参数少于8个
     */
    String PARAMS_LESS_EIGHT_ERROR = "0718";

    /**
     * 启动失败
     */
    String BOOT_FAILED = "0719";

    /**
     * 参数个数不能小于4个,顺序为appID,serviceID,port,jarID
     */
    String PARAMS_LESS_FOUR_ERROR = "0720";

    /**
     * 部署路径错误
     */
    String DEPLOY_PATH_ERROR = "0721";

    /**
     * 系统权限不足
     */
    String PERMISSION_DENY = "0722";

    /**
     * 无系统资源可用
     */
    String NOT_ANY_RESOURCES = "0723";

    /**
     * 不支持命令启动
     */
    String NOT_SUPPORT_BOOT = "0724";

    /**
     * 继续分析下一行
     */
    String NEXT_LINE_PARSE = "0725";

    /**
     * 停止服务失败
     */
    String STOP_FAILED = "0726";

    ////////////////////////////////////////////////////

    ///////////////spark机器学习算法中心////////////////////////////////


    ////////////////////////////////////////////////////
    
    String ERROR = "ffff";
    

    
}
