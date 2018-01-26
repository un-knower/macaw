package cn.migu.macaw.schedule.workflow;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * workflow执行过程中需要使用的常量
 * 
 * @author soy
 */
public interface DataConstants
{
    // ===========================================================================
    // metadata键值定义(workflow运行时环境或redis[如有需要]中)
    // ===========================================================================
    /**
     * job编码
     */
    String JOB_CODE = "jobcode";

    /**
     * task节点类型,存储task实例名称
     */
    String TASK_NODE_TYPE = "tn_type";

    /**
     * 任务运行批次号
     */
    String BATCH_NO = "batch_no";

    /**
     * task编码
     */
    String TASK_CODE = "task_code";

    /**
     * job运行实例所在主机ip
     */
    String JOB_INSTANCE_ADDRESS = "job_instance_ip";

    /**
     * job运行时异常标记
     */
    String JOB_EXCEP_FLAG = "exception_flag";

    /**
     * job异常信息
     */
    String JOB_EXCEP_MSG = "exception_msg";

    /**
     * job运行时中断标记
     */
    String JOB_INTERRUPT_FLAG = "interrupt_flag";

    /**
     * 区域运行模式
     */
    String JOB_MODE_REGION = "region_mode";

    /**
     * 单节点运行模式
     */
    String JOB_MODE_SINGLE = "single_mode";

    /**
     * 父存储过程编码
     */
    String PARENT_PROC_CODE = "parent_proc_code";

    /**
     * 父任务编码
     */
    String PARENT_JOB_CODE = "parent_job_code";

    /**
     * spark context继承标记
     */
    String SPARK_CONTEXT_INHERITED = "spark_context_inherited";

    /**
     * spark context已使用标记
     */
    String SPARK_CONTEXT_USED = "spark_context_used";

    /**
     * spark context对应的appid
     */
    String SPARK_CONTEXT_APPID = "spark_context_appid";

    /**
     * job循环因子变量名
     */
    String VAR_NAME_FOR_JOB_LOOP = "var_name_for_job_loop";

    /**
     * job循环因子变量值
     */
    String VAR_VAL_FOR_JOB_LOOP = "var_val_for_job_loop";
    
    // ===========================================================================
    // task实例类型定义,名称与spring bean管理的名称一致
    // ===========================================================================
    /**
     * 决策task
     */
    String TASK_TYPE_DECISION = "decisionTask";

    /**
     * 测试task
     */
    String TASK_TYPE_DUMMY = "dummyTask";

    /**
     * 查询sql task
     */
    String TASK_TYPE_SINGLESQLOUT = "singleSqlOutworkTask";

    /**
     * 直接结束task
     */
    String TASK_TYPE_DIRECT_TERMINATE = "directTerminateTask";
    
    // ===========================================================================
    // 全局参数
    // redis缓存中存放的格式为[unify_global]-[key]-[value]
    // ===========================================================================
    /**
     * 全局参数前缀
     */
    String GLOBAL = "global";
    
    // ===========================================================================
    // job-tasks缓存使用键值
    // redis缓存中存放的格式为[jobcode]-[nodeid|taskcode_key]-[value]
    // ===========================================================================
    /**
     * 决策分支结果
     */
    String DICISION_BRANCH = "dicision_branch";

    /**
     * 自定义取消执行标记
     */
    String CUSTOM_CANCEL_RUN = "custom_cancel_run";

    /**
     * 取消执行标记
     */
    String CANCEL_RUN = "cancel_run";

    /**
     * 不能取消执行标记
     */
    String CANNOT_CANCEL = "cannot_cancel";

    /**
     * 决策判断实际值
     */
    String DICISION_REAL_VALUE = "decision_value";

    /**
     * spark资源核数
     */
    String CORE_NUM = "core_num";

    /**
     * spark资源内存大小
     */
    String MEM_SIZE = "mem_size";

    /**
     * spark查询重试次数
     */
    String SPARK_QUERY_RETRY_TIME = "spark_retry_time";

    /**
     * 提交spark集群并且正在执行的spark appids
     */
    String RUNNING_SPARK_APPNAMES = "run_spark_appnames";

    /**
     * 运行时调用子job code集合
     */
    String RUNNING_SUBJOB_CODE = "run_subjob_codes";

    /**
     * node运行时信息
     */
    String NODE_RUNNING_TRACE = "node_running_trace";

    /**
     * node数据源信息
     */
    String NODE_DATASOURCE_INFO = "node_datasource_info";
    
    // ===========================================================================
    // workflow全局参数
    // ===========================================================================
    /**
     * workflow默认类型
     */
    String WORKFLOW_TYPE = "macaw_workflow_type";

    /**
     * workflow默认版本号,对调度模块中的所有job使用同一版本号
     */
    String WORKFLOW_VERSION = "0.1";
    
    // ===========================================================================
    // 节点自定义,内部使用
    // ===========================================================================
    /**
     * 占位root节点
     */
    String FAKE_ROOT = "root";

    /**
     * 有效值
     */
    String VALID = "1";

    /**
     * 可以并发执行的节点线程数
     */
    int MAX_NODE_THREAD = 50;

}
