package cn.migu.macaw.schedule.task.util;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

import cn.migu.macaw.common.ServiceUrlProvider;
import cn.migu.macaw.common.SysRetCode;
import cn.migu.macaw.common.log.LogUtils;
import cn.migu.macaw.common.message.Entity;
import cn.migu.macaw.schedule.api.model.Procedure;
import cn.migu.macaw.schedule.cache.JobTasksCache;
import cn.migu.macaw.schedule.cache.ProcContextCache;
import cn.migu.macaw.schedule.dao.ProcedureMapper;
import cn.migu.macaw.schedule.service.IJobCommService;
import cn.migu.macaw.schedule.task.TaskNodeBrief;
import cn.migu.macaw.schedule.util.ScheduleLogTrace;
import cn.migu.macaw.schedule.workflow.DataConstants;

/**
 * spark 提交事件处理
 *
 * @author soy
 */
@Component("sparkEventHandler")
public class SparkEventHandler
{
    private final String RUNNING_RET_E1 = "java.lang.IllegalStateException: Cannot call methods on a stopped SparkContext";

    private final String RUNNING_RET_E2 = "java.lang.OutOfMemoryError";

    private final String RUNNING_RET_E3 =
        "org.apache.spark.SparkException: Job aborted due to stage failure: Master removed our application: KILLED";

    private final String RUNNING_RET_E4 = "java.lang.IllegalStateException: SparkContext has been shutdown";

    private final String RUNNING_RET_E5 =
        "org.apache.spark.SparkException: Job aborted due to stage failure: Master removed our application: KILLED";

    private final String RUNNING_RET_E6 = "cancelled because SparkContext was shut down";
    
    @Resource
    private JobTasksCache jobTasksCache;
    
    @Resource
    private ProcContextCache procContextCache;
    
    @Resource
    private SparkResourceMgr srm;
    
    @Resource
    private ServiceReqClient client;
    
    @Resource
    private IJobCommService jobCommService;
    
    @Resource
    private ProcedureMapper procDao;
    
    /**
     * 是否为处理事件
     * @param errStack
     * @return
     * @see [类、类#方法、类#成员]
     */
    public boolean isHandlerEvent(String errStack)
    {
        if (errStack.contains(RUNNING_RET_E1) || errStack.contains(RUNNING_RET_E2) || errStack.contains(RUNNING_RET_E3)
            || errStack.contains(RUNNING_RET_E4) || errStack.contains(RUNNING_RET_E5)
            || errStack.contains(RUNNING_RET_E6))
        {
            return true;
        }
        
        return false;
    }
    
    /**
     * 重新申请计算中心spark driver
     * @param errStack
     * @param brief
     * @param appId
     * @throws Exception
     * @see [类、类#方法、类#成员]
     */
    public void reAllocDriver(String errStack, TaskNodeBrief brief, String appId)
        throws Exception
    {
        
        //重置已申请的driver节点
        releaseDriverNode(brief, appId);
        
        Thread.sleep(100);
        
        List<String> ancestor = getAllJobCodeCallLayer(brief.getJobCode());
        
        String topJobCode = CollectionUtils.isEmpty(ancestor) ? brief.getJobCode() : ancestor.get(ancestor.size() - 1);
        
        //申请新的driver节点
        String newAppId = allocDriverNode(brief, topJobCode);
        
        //刷新所有祖先的appid
        if (StringUtils.isNotEmpty(newAppId))
        {
            ScheduleLogTrace.scheduleInfoLog(brief,
                StringUtils
                    .join("更新所有上层job[", ancestor.stream().collect(Collectors.joining(",")), "]的appid为", newAppId));
            refreshAppidForebears(ancestor, newAppId);
        }
        //
        
    }
    
    /**
     * 申请计算中心节点进程资源
     * @param proc
     * @param brief
     * @return
     * @throws Exception
     * @see [类、类#方法、类#成员]
     */
    public String allocDriverNode(Procedure proc, TaskNodeBrief brief)
        throws Exception
    {
        String cores = proc.getCpus();
        
        String memSize = proc.getMemory();
        
        cores = StringUtils.isEmpty(cores) ? "2" : cores;
        memSize = StringUtils.isEmpty(memSize) ? "512" : memSize;
        
        Entity rltEntity =
            client.postCommonTaskForEntity(ServiceUrlProvider.sparkJobMgrService(ServiceReqClient.SPARK_DRIVER_INIT),
                client.sparkCtxInitEntity(brief, proc.getCode(), cores, memSize),
                brief);
        
        if (StringUtils.equals(rltEntity.getCode(), SysRetCode.SUCCESS))
        {
            String appId = rltEntity.getAppid();
            if (StringUtils.isNotEmpty(appId))
            {
                jobTasksCache.put(brief.getJobCode(), "-", DataConstants.SPARK_CONTEXT_APPID, appId);
                return appId;
            }
        }
        
        return null;
    }
    
    /**
     * 申请计算中心进程节点
     * @param brief
     * @param topJobCode
     * @return
     * @throws Exception
     * @see [类、类#方法、类#成员]
     */
    private String allocDriverNode(TaskNodeBrief brief, String topJobCode)
        throws Exception
    {
        //存储过程
        Procedure proc = jobCommService.getProcedure(topJobCode);
        
        return allocDriverNode(proc, brief);
    }
    
    /**
     * 释放计算中心进程节点
     * @param brief
     * @param appId
     * @see [类、类#方法、类#成员]
     */
    private void releaseDriverNode(TaskNodeBrief brief, String appId)
    {
        try
        {
            //释放请求
            client.postCommonTask(ServiceUrlProvider.sparkJobMgrService(ServiceReqClient.SPARK_DRIVER_FREE),
                client.sparkCtxFreeEntity(appId),
                brief);
        }
        catch (Exception e)
        {
            LogUtils.runLogError(ExceptionUtils.getStackTrace(e));
        }
    }
    
    /**
     * 释放计算中心进程节点
     * @param jobCode
     * @param batchCode
     * @param appId
     * @see [类、类#方法、类#成员]
     */
    public void releaseDriverNode(String jobCode, String batchCode, String appId)
    {
        if (!StringUtils.isEmpty(appId))
        {
            TaskNodeBrief brief = new TaskNodeBrief();
            brief.setBatchCode(batchCode);
            brief.setJobCode(jobCode);
            
            releaseDriverNode(brief, appId);
        }
    }
    
    /**
     * 调用层级序列上的job编码
     * 从当前节点开始向上回溯,第一个索引位置为父节点
     * 最后一个索引位置节点为最早祖先节点
     * @param jobCode
     * @return
     * @see [类、类#方法、类#成员]
     */
    private List<String> getAllJobCodeCallLayer(String jobCode)
    {
        List<String> codes = Lists.newArrayList();
        
        if (!StringUtils.isEmpty(jobCode))
        {
            String pJobCode = procContextCache.get(jobCode, StringUtils.join("${", DataConstants.PARENT_JOB_CODE, "}"));
            
            while (StringUtils.isNotEmpty(pJobCode))
            {
                codes.add(pJobCode);
                pJobCode = procContextCache.get(pJobCode, StringUtils.join("${", DataConstants.PARENT_JOB_CODE, "}"));
            }
        }
        
        return codes;
    }
    
    /**
     * 刷新上级调用层环境上下文中的可用appid
     * @param jobCodes
     * @param appId
     * @see [类、类#方法、类#成员]
     */
    private void refreshAppidForebears(List<String> jobCodes, String appId)
    {
        for (String code : jobCodes)
        {
            try
            {
                jobTasksCache.put(code, "-", DataConstants.SPARK_CONTEXT_APPID, appId);
            }
            catch (Exception e)
            {
                LogUtils.runLogError(ExceptionUtils.getStackTrace(e));
                break;
            }
        }
    }
    
}
