package cn.migu.macaw.common;

/**
 * 系统返回码
 * 
 * @author  soy
 */
public interface SysRetCode
{
    //操作成功
    String SUCCESS = "0000";
    
    //输入参数不完整
    String PARAM_INCOMPLETE = "0001";
    
    //输入参数格式错误
    String PARAM_FORMAT_ERR = "0002";
    
    //输入参数类型错误
    String PARAM_TYPE_ERR = "0003";
    
    //服务器无法连接
    String SERVER_NO_CONNECTED = "0004";
    
    //服务器登录失败
    String SERVER_LOGINON_FAILED = "0005";
    
    //调用方法不存在
    String METHOD_NOEXISTED = "0006";
    
    //Mysql异常
    String MYSQL_EXCEPTION = "0088";
    
    //GP异常
    String GP_EXCEPTION = "0089";
    
    //网络异常
    String NETWORK_EXCEPTION = "0090";
    
    //FTP异常
    String FTP_EXCEPTION = "0091";
    
    //REDIS异常
    String REDIS_EXCEPTION = "0092";
    
    //zookeeper异常
    String ZOOKEEPER_EXCEPTION = "0093";
    
    //kafka异常
    String KAFKA_EXCEPTION = "0094";
    
    //oracle异常
    String ORACLE_EXCEPTION = "0095";
    
    //spark集群异常
    String SPARK_EXCEPTION = "0096";
    
    //HugeTable数据仓库异常
    String HT_EXCEPTION = "0097";
    
    //Hadoop集群异常
    String HADOOP_EXCEPTION = "0098";
    
    //应用系统异常
    String APPSYS_EXCEPTION = "0099";
    
    //////////////////////////消息中心///////////////////
    //用户名或密码错误
    String USER_PASSWD_ERROR = "0101";
    
    //用户暂停
    String USER_PAUSE = "0102";
    
    //IP不可信
    String IP_NO_AUTHEN = "0103";
    
    //用户未上线
    String USER_NOT_ONLINE = "0104";
    
    //用户不存在
    String USER_NOT_EXISTED = "0105";
    
    //用户信息不完整
    String USER_INCOMPLETE = "0106";
    
    //必填项为空
    String REQUIRED_EMPTY = "0107";
    
    //用户接入类型不匹配
    String USER_ACCESS_TYPE_ERR = "0108";
    
    //TOKEN错误
    String TOKEN_ERROR = "0111";
    
    //TOKEN过期
    String TOKEN_EXPIRE = "0112";
    
    //TOPIC未授权
    String TOPIC_NO_AUTHEN = "0113";
    
    //消息长度过长
    String MSG_LEN_TOO_LONG = "0114";
    
    //FTP登录失败
    String FTP_LOGINON_FAIL = "0115";
    
    //文件名错误
    String FILE_NAME_ERROR = "0116";
    
    //TOPIC不存在
    String TOPIC_NOT_EXISTED = "0117";
    
    //文件无法打开
    String OPEN_FILE_FAILED = "0118";
    
    //客户端无响应
    String NO_RESPONSE = "0119";
    
    //用户级无资源
    String USER_NO_RESOURCE = "0120";
    
    //系统级无资源
    String SYS_NO_RESOURCE = "0121";
    
    //一次发送量大于设定值
    String SINGLE_MSGBYTES_GT = "0122";
    
    //发送时间间隔小于设定值
    String SEND_INTERVAL_LT = "0123";
    
    //Partition不存在或非法
    String PARTITION_ILLEGAL = "0124";
    
    //消息为空
    String PAYLOAD_EMPTY = "0125";
    
    //消息条数大于设定长度
    String MSG_COUNT_GT = "0126";
    
    //消息格式不正确
    String MSG_FORMAT_ERROR = "0127";
    
    //密码格式不符合要求
    String PASSWD_FORMAT_ERROR = "0128";
    
    //新、老密码不能一样
    String PASSWD_DIFF_ERROR = "0129";
    
    //用户角色权限错误
    String USER_ROLE_AUTH_ERROR = "0130";
    
    //输入参数格式不正确
    String ILLEGAL_PARAM = "0132";
    
    //查询范围超出允许值
    String OUT_OF_BOUND_PARAM = "0133";
    
    //Zookeeper安装文件不存在
    String ZK_INSTALL_FILE_NOEXISTED = "0134";
    
    //Broker安装文件不存在
    String BROKER_INSTALL_FILE_NOEXISTED = "0135";
    
    //Topic已存在
    String TOPIC_EXISTED = "0136";
    
    //Topic复制数大于Broker数
    String TOPIC_REPLIS_GT_BROKERS = "0137";
    
    //用户等级改变,需要重新上线
    String USER_RANK_CHANGE = "0138";
    
    //topic授权已暂停
    String TOPIC_AUTH_STOP = "0139";
    
    //消费用户已上线
    String CONSUMER_LOGIN_ON = "0140";
    ///////////////////////////////////////////////////////////////
    //////////////////调度中心/////////////////////////////////
    //任务名称不存在
    String JOB_NAME_ABSENT = "0201";
    
    //job已经被触发执行
    String JOB_ALREADY_TRIGGERED = "0202";
    
    //触发器或任务类为空
    String JOB_TRIGGER_OR_CLASS = "0203";
    
    ////////////////////////////////////////////////////////
    
    /////////////////资源中心/////////////////////////////////
    //spark核数为空
    String SPARK_CORENUM_EMPTY = "0301";
    
    //spark内存数为空
    String SPARK_MEMSIZE_EMPTY = "0302";
    
    //spark核数非数字
    String SPARK_CORENUM_NONNUMERIC = "0303";
    
    //spark内存数非数字
    String SPARK_MEMSIZE_NONNUMERIC = "0304";
    
    //spark核数范围不正确
    String SPARK_CORENUM_OVERFLOW = "0305";
    
    //spark内存数范围不正确
    String SPARK_MEMSIZE_OVERFLOW = "0306";
    
    //spark driver资源不足
    String SPARK_DRIVER_INSUFFICIENT = "0307";
    
    //spark driver请求异常
    String SPARK_DRIVER_REQUEST_ERROR = "0308";
    
    //停止spark app失败
    String SPARK_APP_STOP_FAIL = "0309";
    
    //spark app资源参数错误
    String SPARK_RES_PARAM_ERROR = "0310";
    
    //spark app没有找到
    String SPARK_APP_NOT_FOUND = "0311";
    
    //spark app已完成
    String SPARK_APP_FINISHED = "0312";
    
    //spark app运行中
    String SPARK_APP_RUNNING = "0313";
    
    //spark app已被手动停止
    String SPARK_APP_KILLED = "0314";
    
    //spark app运行失败
    String SPARK_APP_FAILED = "0315";
    
    //spark 任务创建失败
    String SPARK_APP_CREATED_FAILED = "0316";
    
    //spark jar配置信息错误
    String SPARK_JAR_CONFIG_ERROR = "0317";
    
    ////////////////////////////////////////////////////////
    
    /////////////监控中心////////////////////////////////////
    //第三方系统无响应
    String REQSYS_NORESP = "0401";
    
    ///////////////////////////////////////////////////////
    
    /////////////计算中心///////////////////////////////////
    //HDFS路径错误或资源不存在
    String HDFS_SCHEMAL_ERR_C = "0501";
    
    //SQL预编译失败
    String SPARK_SQL_PRECOMPILE_FAILED_C = "0502";
    
    //Spark集群连接失败
    String SPARK_CLUSTER_CONNECT_FAILED_C = "0503";
    
    //HugeTable数据仓库连接失败
    String HT_CONNECT_FAILED_C = "0504";
    
    //Hadoop集群连接失败
    String HADOOP_CONNECT_FAILED_C = "0505";
    
    //HDFS路径下资源已存在
    String HDFS_RES_EXISTED_C = "0506";
    
    //HDFS可用空间不足
    String HDFS_SPACE_INSUFFICIENT_C = "0507";
    
    //////////////////////////////////////////////////////
    
    ///////////////算法中心////////////////////////////////
    //创建Spark任务失败
    String SPARK_JOB_CREATE_FAILED_A = "0601";
    
    //HDFS路径错误或资源不存在
    String HDFS_SCHEMAL_ERR_A = "0602";
    
    //SQL预编译失败
    String SPARK_SQL_PRECOMPILE_FAILED_A = "0603";
    
    //Spark集群连接失败
    String SPARK_CLUSTER_CONNECT_FAILED_A = "0604";
    
    //Hadoop集群连接失败
    String HADOOP_CONNECT_FAILED_A = "0605";
    
    //HDFS路径下资源已存在
    String HDFS_RES_EXISTED_A = "0606";
    
    //HDFS可用空间不足
    String HDFS_SPACE_INSUFFICIENT_A = "0607";
    
    //算法中心操作oracle数据库失败
    String ALGO_DOORACLE_ERROR = "0608";
    
    ////////////////////////////////////////////////////
    
    String ERROR = "ffff";
    
    //通知pop计算结果消息头
    String SYNC_POP = "bizCode";
    
    //调用pop接口返回失败
    String POP_FAIL = "9999";
    
    //调用pop接口请求参数xml
    String MEG_NAME = "xml";
    
    ////////////////////////////////////////////////////
    
    //文件或目录不存在
    String DIRORFILE_NOTEXISTS = "0007";
    
    //文件字段长度不一致
    String FIELD_LENGTH_ERR = "0008";
    
}
