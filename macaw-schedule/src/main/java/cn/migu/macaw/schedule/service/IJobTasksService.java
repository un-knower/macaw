package cn.migu.macaw.schedule.service;

import cn.migu.macaw.dag.idgraph.IdDag;
import cn.migu.macaw.schedule.task.TaskNodeBrief;

/**
 * job运行tasks接口
 * 
 * @author  zhaocan
 * @version  [版本号, 2016年2月14日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public interface IJobTasksService
{
    /**
     * 关闭http请求
     * @param jobCode
     * @see [类、类#方法、类#成员]
     */
    void closeHttpReq(String jobCode);
    
    /**
     * 资源回收
     * @param jobCode
     * @param batchNo
     * @see [类、类#方法、类#成员]
     */
    void freeResource(String jobCode, String batchNo);
    
    /**
     * 
     * job执行业务方法入口
     * @author zhaocan
     * @param jobCode 任务编码
     * @throws Exception 异常类
     */
    void jobRun(String jobCode, String batchNo)
        throws Exception;
    
    /**
     * 业务节点运行
     * @param brief
     * @param dag
     * @param classType
     */
    void runNode(TaskNodeBrief brief, IdDag<String> dag, String classType)
        throws Exception;
    
    /**
     * 运行指定区域图
     * @param jobCode 任务编码
     * @param firstNode 开始节点
     * @param batchNo 运行批次号
     * @throws Exception
     */
    void runSpecRegion(String jobCode, String firstNode, String batchNo)
        throws Exception;

    /**
     * 单节点运行
     * @param jobCode 任务编码
     * @param nodeCode 节点编码
     * @param batchNo 运行批次号
     * @throws Exception
     */
    void runSingleNode(String jobCode, String nodeCode, String batchNo)
        throws Exception;
    
    /**
     * 任务执行完成回调接口
     * @param jobCode
     * @see [类、类#方法、类#成员]
     */
    void jobSuccCallbackIntf(String jobCode);
    
    /**
     * 任务执行异常回调接口
     * @param jobCode 任务编码
     * @param info 异常信息 
     * @see [类、类#方法、类#成员]
     */
    void jobExcepCallbackIntf(String jobCode, String info);
    
    /**
     * job执行结束回调接口
     * @param jobCode
     * @param info
     * @see [类、类#方法、类#成员]
     */
    void jobCallbackIntf(String jobCode, String info, boolean excep);
    
    /**
     * 设置job运行环境所需上下文标记
     * @param jobCode
     * @param nodeId
     * @param key
     * @param value
     * @see [类、类#方法、类#成员]
     */
    void setJobTaskCtxFlag(String jobCode, String nodeId, String key, String value);
    
    /**
     * 是否为残留的job上下文信息
     * @param jobCode
     * @see [类、类#方法、类#成员]
     */
    boolean isResidualCtx(String jobCode);

    /**
     * 获取job运行实例的服务器的地址
     * @param jobCode 任务编码
     * @return
     */
    String getJobInstanceAddress(String jobCode);
}
