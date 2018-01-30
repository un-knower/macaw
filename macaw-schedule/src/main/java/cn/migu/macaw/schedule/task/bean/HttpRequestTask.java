package cn.migu.macaw.schedule.task.bean;

import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import cn.migu.macaw.common.RestTemplateProvider;
import cn.migu.macaw.common.log.LogUtils;
import cn.migu.macaw.dag.idgraph.IdDag;
import cn.migu.macaw.schedule.cache.JobTasksCache;
import cn.migu.macaw.schedule.task.ITask;
import cn.migu.macaw.schedule.task.TaskNodeBrief;
import cn.migu.macaw.schedule.task.util.ConfigParamUtil;
import cn.migu.macaw.schedule.task.util.LabelTag;
import cn.migu.macaw.schedule.util.ScheduleLogTrace;
import cn.migu.macaw.schedule.workflow.DataConstants;

/**
 * 远程调用Task
 * 例如:kafka远程调用job
 * --配置参数
 *   --远程地址
 *   remote_host:"http://192.168.56.100:8081/kafka/execKafkaJob.do
 *   --请求参数
 *   kafkaJobName:"xxxx"
 * 
 * @author soy
 */
@Component("httpRequestTask")
public class HttpRequestTask implements ITask
{
    /**
     * 请求类型,仅支持两种,post和get
     * 如果没有这个参数默认为post
     */
    private final String REQ_TYPE = "http_req_type";

    /**
     * get请求配置值
     */
    private final String GET_REQ = "get";
    
    @Resource
    private ConfigParamUtil configParamUtil;
    
    @Resource
    private JobTasksCache jobTasksCache;
    
    @Resource(name = "restTemplate")
    private RestTemplate restTemplate;
    
    @Override
    public void run(TaskNodeBrief brief, IdDag<String> dag)
        throws Exception
    {
        
        Map<String, String> sysParams = configParamUtil.getJobNodeSysParams(brief);
        
        String remoteAddr = sysParams.get(ConfigParamUtil.REMOTE_HOST);
        
        if (StringUtils.isEmpty(remoteAddr))
        {
            return;
        }
        
        Map<String, String> formParams = sysParams.entrySet()
            .stream()
            .filter(map -> !StringUtils.equalsIgnoreCase(map.getKey(), ConfigParamUtil.REMOTE_HOST)
                && !StringUtils.equalsIgnoreCase(map.getKey(), REQ_TYPE))
            .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
        
        //job循环变量
        String factorName = jobTasksCache.get(brief.getJobCode(), "-", DataConstants.VAR_NAME_FOR_JOB_LOOP);
        if (StringUtils.isNotEmpty(factorName))
        {
            String factorVal = jobTasksCache.get(brief.getJobCode(), "-", DataConstants.VAR_VAL_FOR_JOB_LOOP);
            if (StringUtils.isNotEmpty(factorVal))
            {
                formParams.put(StringUtils.substringBetween(factorName, LabelTag.LABEL_PREFIX, LabelTag.LABEL_SUFFIX),
                    factorVal);
            }
        }

        String respStr = null;
        if(sysParams.containsKey(REQ_TYPE) && StringUtils.equalsIgnoreCase(sysParams.get(REQ_TYPE),GET_REQ))
        {
            respStr = RestTemplateProvider.getForEntity(restTemplate, remoteAddr, String.class);
        }
        else
        {
            respStr = RestTemplateProvider.postFormForEntity(restTemplate, remoteAddr, String.class, formParams);
        }
        
        ScheduleLogTrace.scheduleInfoLog(brief, StringUtils.join("请求返回消息:", respStr));
        
        Map<String, String> redisParams = configParamUtil.getJobNodeRedisOutParams(brief);

        final String retVal = respStr;
        
        if (!MapUtils.isEmpty(redisParams))
        {
            redisParams.forEach((k, v) -> {
                try
                {
                    jobTasksCache.put(brief.getJobCode(), "", k, retVal);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    LogUtils.runLogError(ExceptionUtils.getStackTrace(e));
                }
            });
        }
        
    }
    
}
