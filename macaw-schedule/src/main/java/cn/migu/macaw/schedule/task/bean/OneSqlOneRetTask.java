package cn.migu.macaw.schedule.task.bean;

import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import cn.migu.macaw.dag.idgraph.IdDag;
import cn.migu.macaw.schedule.task.ITask;
import cn.migu.macaw.schedule.task.TaskNodeBrief;
import cn.migu.macaw.schedule.cache.JobTasksCache;
import cn.migu.macaw.schedule.cache.ProcContextCache;
import cn.migu.macaw.schedule.task.util.ConfigParamUtil;
import cn.migu.macaw.schedule.task.util.SqlServiceUtil;
import cn.migu.macaw.schedule.util.ScheduleLogTrace;
import cn.migu.macaw.schedule.workflow.DataConstants;

/**
 * sql语句返回一个value
 * sql格式必须为select xx as value ...
 * 
 * @author  soy
 */
@Component("singleSqlOutworkTask")
public class OneSqlOneRetTask implements ITask
{
    @Resource
    private ConfigParamUtil configParamUtil;
    
    @Resource
    private ProcContextCache procContextCache;
    
    @Resource
    private SqlServiceUtil sqlService;
    
    @Resource
    private JobTasksCache jobTasksCache;
    
    @Override
    public void run(TaskNodeBrief brief, IdDag<String> dag)
        throws Exception
    {
        
        String retValue = sqlService.queryWithRet(brief);
        
        if (null == retValue)
        {
            return;
        }
        
        Map<String, String> labels = configParamUtil.getJobNodeProcParams(brief);
        if (MapUtils.isEmpty(labels) || 1 != labels.size())
        {
            ScheduleLogTrace.scheduleWarnLog(brief, "返回值赋值变量不正确");
            throw new IllegalArgumentException("返回值赋值变量不正确");
        }
        
        String key = labels.entrySet().stream().map(m -> StringUtils.trim(m.getKey())).collect(Collectors.joining());
        
        ScheduleLogTrace.scheduleInfoLog(brief, StringUtils.join("缓存返回值=>", key, "=", (String)retValue));
        
        //设置返回值到环境变量
        procContextCache.put(brief.getJobCode(), key, retValue);
        
        jobTasksCache.append(brief.getJobCode(),
            brief.getNodeId(),
            DataConstants.NODE_RUNNING_TRACE,
            StringUtils.join(key, "=", retValue));
        
    }
    
}
