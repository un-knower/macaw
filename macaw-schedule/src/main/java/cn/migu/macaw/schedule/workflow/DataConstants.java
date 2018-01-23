package cn.migu.macaw.schedule.workflow;

/**
 * workflow执行过程中需要使用的常量
 * 
 * @author  zhaocan
 * @version  [版本号, 2016年5月26日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class DataConstants
{
    // ===========================================================================
    // metadata键值定义(workflow运行时环境或redis[如有需要]中)
    // ===========================================================================
    //job编码
    public static final String JOB_CODE = "jobcode";
    
    //task节点类型,存储task实例名称
    public static final String TASK_NODE_TYPE = "tn_type";
    
    //batch_no
    public static final String BATCH_NO = "batch_no";
    
    //task编码
    public static final String TASK_CODE = "task_code";

    /**
     * job运行实例所在主机ip
     */
    public static final String JOB_INSTANCE_ADDRESS = "job_instance_ip";
    
    //job运行时异常标记
    public static final String JOB_EXCEP_FLAG = "exception_flag";
    
    //job异常信息
    public static final String JOB_EXCEP_MSG = "exception_msg";
    
    //job运行时中断标记
    public static final String JOB_INTERRUPT_FLAG = "interrupt_flag";
    
    //区域运行模式
    public static final String JOB_MODE_REGION = "region_mode";
    
    //单节点运行模式
    public static final String JOB_MODE_SINGLE = "single_mode";
    
    //父存储过程编码
    public static final String PARENT_PROC_CODE = "parent_proc_code";
    
    //父任务编码
    public static final String PARENT_JOB_CODE = "parent_job_code";
    
    //spark context继承标记
    public static final String SPARK_CONTEXT_INHERITED = "spark_context_inherited";
    
    //spark context已使用标记
    public static final String SPARK_CONTEXT_USED = "spark_context_used";
    
    //spark context对应的appid
    public static final String SPARK_CONTEXT_APPID = "spark_context_appid";
    
    //job循环因子变量名
    public static final String VAR_NAME_FOR_JOB_LOOP = "var_name_for_job_loop";
    
    //job循环因子变量值
    public static final String VAR_VAL_FOR_JOB_LOOP = "var_val_for_job_loop";
    
    // ===========================================================================
    // task实例类型定义,名称与spring bean管理的名称一致
    // ===========================================================================
    public static final String TASK_TYPE_DECISION = "decisionTask";
    
    public static final String TASK_TYPE_DUMMY = "dummyTask";
    
    public static final String TASK_TYPE_SINGLESQLOUT = "singleSqlOutworkTask";
    
    public static final String TASK_TYPE_DIRECT_TERMINATE = "directTerminateTask";
    
    // ===========================================================================
    // 全局参数
    // redis缓存中存放的格式为[unify_global]-[key]-[value]
    // ===========================================================================
    public static final String GLOBAL = "global";
    
    // ===========================================================================
    // job-tasks缓存使用键值
    // redis缓存中存放的格式为[jobcode]-[nodeid|taskcode_key]-[value]
    // ===========================================================================
    //决策分支结果
    public static final String DICISION_BRANCH = "dicision_branch";
    
    //自定义取消执行标记
    public static final String CUSTOM_CANCEL_RUN = "custom_cancel_run";
    
    //取消执行标记
    public static final String CANCEL_RUN = "cancel_run";
    
    //不能取消执行标记
    public static final String CANNOT_CANCEL = "cannot_cancel";
    
    //决策判断实际值
    public static final String DICISION_REAL_VALUE = "decision_value";
    
    //spark资源核数
    public static final String CORE_NUM = "core_num";
    
    //spark资源内存大小
    public static final String MEM_SIZE = "mem_size";
    
    //spark查询重试次数
    public static final String SPARK_QUERY_RETRY_TIME = "spark_retry_time";
    
    //提交spark集群并且正在执行的spark appids
    public static final String RUNNING_SPARK_APPNAMES = "run_spark_appnames";
    
    //运行时调用子job code集合
    public static final String RUNNING_SUBJOB_CODE = "run_subjob_codes";
    
    //node运行时信息
    public static final String NODE_RUNNING_TRACE = "node_running_trace";
    
    //node数据源信息
    public static final String NODE_DATASOURCE_INFO = "node_datasource_info";
    
    // ===========================================================================
    // workflow全局参数
    // ===========================================================================
    //workflow默认类型
    public static final String WORKFLOW_TYPE = "macaw_workflow_type";
    
    //workflow默认版本号,对调度模块中的所有job使用同一版本号
    public static final String WORKFLOW_VERSION = "0.1";
    
    // ===========================================================================
    // 节点自定义,内部使用
    // ===========================================================================
    // 占位root节点
    public static final String FAKE_ROOT = "root";
}
