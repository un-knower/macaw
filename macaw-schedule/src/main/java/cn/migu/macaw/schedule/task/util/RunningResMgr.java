package cn.migu.macaw.schedule.task.util;

import java.util.Map;

import javax.annotation.Resource;

import cn.migu.macaw.common.RestTemplateProvider;
import cn.migu.macaw.common.ServiceName;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.google.common.collect.Maps;

import cn.migu.macaw.common.log.LogUtils;
import cn.migu.macaw.common.SysRetCode;
import cn.migu.macaw.common.message.Response;
import cn.migu.macaw.schedule.task.TaskNodeBrief;
import cn.migu.macaw.schedule.cache.JobTasksCache;
import cn.migu.macaw.schedule.workflow.DataConstants;
import org.springframework.web.client.RestTemplate;

/**
 * 运行时app资源管理
 * 
 * @author  zhaocan
 * @version  [版本号, 2016年10月21日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
@Component("runningResMgr")
public class RunningResMgr
{
    @Resource
    private JobTasksCache jobTaskCache;
    
    @Resource
    private ServiceReqClient client;

    @Resource(name = "restTemplateForLoadBalance")
    private RestTemplate restTemplate;
    
    /**
     * 增加spark app资源记录
     * @param jobCode 任务编码
     * @param appName app名称
     * @see [类、类#方法、类#成员]
     */
    public synchronized void addSparkAppRecord(String jobCode, String appName)
    {
        
        if (jobTaskCache.contains(jobCode))
        {
            jobTaskCache.putList(jobCode, "-", DataConstants.RUNNING_SPARK_APPNAMES, appName);
        }
    }
    
    /**
     * 删除spark appid
     * @param jobCode 任务编码
     * @param appName app名称
     * @see [类、类#方法、类#成员]
     */
    public synchronized void delSparkAppRecord(String jobCode, String appName)
    {
        jobTaskCache.delElemList(jobCode, "-", DataConstants.RUNNING_SPARK_APPNAMES, appName);
    }
    
    /**
     * 获取提交至spark集群但可能还在执行的app的id
     * @param jobCode 任务编码
     * @return spark appid数组
     * @see [类、类#方法、类#成员]
     */
    public String[] getSparkAppRecord(String jobCode)
    {
        return jobTaskCache.getList(jobCode, "-", DataConstants.RUNNING_SPARK_APPNAMES);
    }
    
    /**
     * 缓存调用子job
     * @param jobCode
     * @param subJobCode
     * @see [类、类#方法、类#成员]
     */
    public synchronized void addSubJobRecord(String jobCode, String subJobCode)
    {
        if (jobTaskCache.contains(jobCode))
        {
            jobTaskCache.putList(jobCode, "-", DataConstants.RUNNING_SUBJOB_CODE, subJobCode);
        }
    }
    
    /**
     * 删除被调用的子job应用
     * @param jobCode
     * @param subJobCode
     * @see [类、类#方法、类#成员]
     */
    public synchronized void delSubJobRecord(String jobCode, String subJobCode)
    {
        jobTaskCache.delElemList(jobCode, "-", DataConstants.RUNNING_SUBJOB_CODE, subJobCode);
    }
    
    /**
     * 被调用的子job集合
     * @param jobCode
     * @return
     * @see [类、类#方法、类#成员]
     */
    public String[] getSubJobRecord(String jobCode)
    {
        return jobTaskCache.getList(jobCode, "-", DataConstants.RUNNING_SUBJOB_CODE);
    }
    
    /**
     * 停止spark任务
     * @param jobCode
     * @param batchNo
     * @see [类、类#方法、类#成员]
     */
    public void stopSparkApps(String jobCode, String batchNo)
    {
        String[] appids = this.getSparkAppRecord(jobCode);
        
        if (ArrayUtils.isNotEmpty(appids))
        {
            TaskNodeBrief brief = new TaskNodeBrief();
            brief.setBatchCode(batchNo);
            brief.setJobCode(jobCode);
            
            for (String id : appids)
            {
                try
                {
                    client.stopSparkTask(id, "", brief);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    LogUtils.runLogError(ExceptionUtils.getStackTrace(e));
                }
            }
        }
        
    }
    
    public void stopSubJobs(String jobCode)
    {
        String[] jobCodes = this.getSubJobRecord(jobCode);
        
        if (ArrayUtils.isNotEmpty(jobCodes))
        {
            String interruptJobUrl = StringUtils.join("http://", ServiceName.JOB_SCHEDULE,"/interruptJob.do");
            
            for (String code : jobCodes)
            {
                
                Map<String, String> formParam = Maps.newHashMap();
                formParam.put("name", code);
                
                try
                {
                    String respStr = RestTemplateProvider.postFormForEntity(restTemplate,interruptJobUrl,String.class,formParam);

                    Response resp = JSON.parseObject(respStr, Response.class, Feature.InitStringFieldAsEmpty);
                    String retCode = resp.getResponse().getCode();
                    if (!StringUtils.equals(retCode, SysRetCode.SUCCESS))
                    {
                        LogUtils.runLogError(StringUtils.join("在父job[", jobCode, "]中停止子job[", code, "]失败"));
                    }
                }
                catch (Exception e)
                {
                    LogUtils.runLogError(ExceptionUtils.getStackTrace(e));
                }
            }
        }
    }
}
