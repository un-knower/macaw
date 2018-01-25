package cn.migu.macaw.schedule.task.util;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import cn.migu.macaw.schedule.task.TaskNodeBrief;
import cn.migu.macaw.schedule.cache.JobTasksCache;
import cn.migu.macaw.schedule.workflow.DataConstants;

/**
 * spark资源管理类
 * 
 * @author soy
 */
@Component("sparkResourceMgr")
public class SparkResourceMgr
{
    
    ////////////////资源定义/////////////////////////////////
    /**
     * 最小核数
     */
    static final int DEFAULT_CORE_NUM = 2;

    /**
     * 最小内存大小,单位为M
     */
    static final int DEFAULT_MEM_SIZE = 512;
    
    ///////////////////////////////////////////////////////
    
    @Resource
    private ConfigParamUtil configParamUtil;
    
    @Resource
    private JobTasksCache jobTasksCache;
    
    /**
     * spark任务资源分配
     * @param brief
     * @param sysParams
     * @throws CloneNotSupportedException 
     * @see [类、类#方法、类#成员]
     */
    public void sparkResourceAlloc(TaskNodeBrief brief, Map<String, String> sysParams)
        throws CloneNotSupportedException
    {
        if (MapUtils.isNotEmpty(sysParams) && sysParams.containsKey(ConfigParamKey.CPU_CORE_NUM))
        {
            int coreNum = Integer.valueOf(sysParams.get(ConfigParamKey.CPU_CORE_NUM));
            if (coreNum < DEFAULT_CORE_NUM)
            {
                jobTasksCache.put(brief.getJobCode(),
                    brief.getNodeId(),
                    DataConstants.CORE_NUM,
                    String.valueOf(DEFAULT_CORE_NUM));
            }
            /*else if (coreNum > MAX_CORE_NUM)
            {
                throw new IllegalArgumentException("资源核数不合法,最大值为128,当前设置为" + coreNum);
            }*/
            else
            {
                jobTasksCache.put(brief.getJobCode(),
                    brief.getNodeId(),
                    DataConstants.CORE_NUM,
                    String.valueOf(coreNum));
            }
        }
        else
        {
            jobTasksCache.put(brief.getJobCode(),
                brief.getNodeId(),
                DataConstants.CORE_NUM,
                String.valueOf(DEFAULT_CORE_NUM));
        }
        
        if (MapUtils.isNotEmpty(sysParams) && sysParams.containsKey(ConfigParamUtil.MEM_SIZE))
        {
            int memSize = Integer.valueOf(sysParams.get(ConfigParamUtil.MEM_SIZE));
            
            if (memSize < DEFAULT_MEM_SIZE)
            {
                jobTasksCache.put(brief.getJobCode(),
                    brief.getNodeId(),
                    DataConstants.MEM_SIZE,
                    String.valueOf(DEFAULT_MEM_SIZE));
            }
            /*else if (memSize > MAX_MEM_SIZE)
            {
                throw new IllegalArgumentException("资源内存大小(单位MB)不合法,最大值为18432,当前设置为" + memSize);
            }*/
            else
            {
                jobTasksCache.put(brief.getJobCode(),
                    brief.getNodeId(),
                    DataConstants.MEM_SIZE,
                    String.valueOf(memSize));
            }
        }
        else
        {
            jobTasksCache.put(brief.getJobCode(),
                brief.getNodeId(),
                DataConstants.MEM_SIZE,
                String.valueOf(DEFAULT_MEM_SIZE));
        }
        
        //重试次数
        if (MapUtils.isNotEmpty(sysParams) && sysParams.containsKey(DataConstants.SPARK_QUERY_RETRY_TIME))
        {
            String retryTime = sysParams.get(DataConstants.SPARK_QUERY_RETRY_TIME);
            if (StringUtils.isNotEmpty(retryTime))
            {
                jobTasksCache.put(brief.getJobCode(),
                    brief.getNodeId(),
                    DataConstants.SPARK_QUERY_RETRY_TIME,
                    retryTime);
            }
            
        }
        
    }
    
    /**
     * spark任务资源分配
     * @param ctx
     * @throws IllegalArgumentException
     * @see [类、类#方法、类#成员]
     */
    public void sparkResourceAlloc(TaskNodeBrief ctx)
        throws Exception
    {
        Map<String, String> params = configParamUtil.getJobNodeSysParams(ctx);
        
        this.sparkResourceAlloc(ctx, params);
    }
    
    /**
     * 获取计算中心spark context是否使用标记
     * @param jobCode
     * @return
     * @see [类、类#方法、类#成员]
     */
    public synchronized boolean getSparkCtxUsedJobScope(String jobCode)
        throws Exception
    {
        String appIdScUsed = jobTasksCache.get(jobCode, "-", DataConstants.SPARK_CONTEXT_USED);
        if (StringUtils.isEmpty(appIdScUsed))
        {
            jobTasksCache.put(jobCode, "-", DataConstants.SPARK_CONTEXT_USED, "1");
            return true;
        }
        
        return false;
    }
    
    /**
     * spark context是否被继承
     * @param jobCode
     * @return
     * @throws Exception
     * @see [类、类#方法、类#成员]
     */
    public synchronized boolean getSparkCtxInheritedJobScope(String jobCode)
        throws Exception
    {
        String scInherited = jobTasksCache.get(jobCode, "-", DataConstants.SPARK_CONTEXT_INHERITED);
        String appIdScUsed = jobTasksCache.get(jobCode, "-", DataConstants.SPARK_CONTEXT_USED);
        if (StringUtils.isEmpty(scInherited) && StringUtils.isEmpty(appIdScUsed))
        {
            jobTasksCache.put(jobCode, "-", DataConstants.SPARK_CONTEXT_INHERITED, "1");
            jobTasksCache.put(jobCode, "-", DataConstants.SPARK_CONTEXT_USED, "1");
            return true;
        }
        
        return false;
    }
    
    /**
     * 设置spark context未使用
     * @param jobCode
     * @throws Exception
     * @see [类、类#方法、类#成员]
     */
    public synchronized void setSparkCtxUnusedJobScope(String jobCode)
        throws Exception
    {
        jobTasksCache.remove(jobCode, "-", DataConstants.SPARK_CONTEXT_USED);
    }
    
    /**
     * 获取spark context appid
     * @param jobCode
     * @return
     * @see [类、类#方法、类#成员]
     */
    public synchronized String getSparkContextAppIdJobScope(String jobCode)
    {
        return jobTasksCache.get(jobCode, "-", DataConstants.SPARK_CONTEXT_APPID);
    }
    
}
