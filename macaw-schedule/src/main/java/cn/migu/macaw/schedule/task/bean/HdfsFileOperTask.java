package cn.migu.macaw.schedule.task.bean;

import java.util.Map;

import javax.annotation.Resource;

import cn.migu.macaw.schedule.feign.FeignHdfsService;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.migu.macaw.dag.idgraph.IdDag;
import cn.migu.macaw.schedule.task.ITask;
import cn.migu.macaw.schedule.task.TaskNodeBrief;
import cn.migu.macaw.schedule.task.datasource.DataSourceAdapter;
import cn.migu.macaw.schedule.task.datasource.DataSourceFlatAttr;
import cn.migu.macaw.schedule.task.util.ConfigParamUtil;
import cn.migu.macaw.schedule.util.ScheduleLogTrace;

/**
 * hdfs文件操作
 * 
 * @author soy
 */
@Component("hdfsFileOperTask")
public class HdfsFileOperTask implements ITask
{
    private final String ACTION = "action";
    
    private final String SOURCE = "sourcePath";
    
    private final String TARGET = "targetPath";
    
    private final String USER = "user";
    
    @Resource
    private ConfigParamUtil configParamUtil;
    
    @Resource
    private DataSourceAdapter dataSourceAdapter;

    @Autowired
    private FeignHdfsService hdfsService;
    
    @Override
    public void run(TaskNodeBrief brief, IdDag<String> dag)
        throws Exception
    {
        Map<String, String> params = configParamUtil.getJobNodeSysParams(brief);
        
        if (MapUtils.isEmpty(params) || StringUtils.isEmpty(params.get(ACTION)))
        {
            throw new IllegalArgumentException("参数不合法,需要配置action");
        }

        
        DataSourceFlatAttr dsAttr = dataSourceAdapter.getNodeDataSourceConf(brief.getJobCode(), brief.getNodeId());

        
        this.action(brief, params);
        
    }
    
    private void action(TaskNodeBrief brief, Map<String, String> params)
        throws Exception
    {
        String action = StringUtils.upperCase(params.get(ACTION));
        
        switch (action)
        {
            case "DELETE":
                delete(brief, params);
                break;
            case "COPY_TO_LOCAL":
                copyToLocal(brief, params);
                break;
            default:
                throw new IllegalStateException(StringUtils.join("不支持的文件操作:", action));
        }
    }
    
    /**
     * 删除
     * @param brief
     * @param params
     * @see [类、类#方法、类#成员]
     */
    private void delete(TaskNodeBrief brief, Map<String, String> params)
    {
        String target = params.get(TARGET);
        
        if (StringUtils.isEmpty(target))
        {
            ScheduleLogTrace.scheduleWarnLog(brief, "删除文件路径为空");
            return;
        }
        
        ScheduleLogTrace.scheduleInfoLog(brief, StringUtils.join("删除hdfs文件:", target, " 开始"));
        
        try
        {
            
            String user = params.getOrDefault(USER, "service");

            hdfsService.delete(target, user);
            
            ScheduleLogTrace.scheduleInfoLog(brief, StringUtils.join("使用用户", user, "删除hdfs文件:", target, " 成功"));
        }
        catch (Exception e)
        {
            ScheduleLogTrace.scheduleWarnLog(brief, ExceptionUtils.getStackTrace(e));
        }
    }
    
    /**
     * 拷贝hdfs文件到本地
     * @param brief
     * @param params
     * @see [类、类#方法、类#成员]
     */
    private void copyToLocal(TaskNodeBrief brief, Map<String, String> params)
    {
        String source = params.get(SOURCE);
        String target = params.get(TARGET);
        if (StringUtils.isEmpty(source) || StringUtils.isEmpty(target))
        {
            ScheduleLogTrace.scheduleWarnLog(brief, "拷贝源或目的文件路径为空");
            return;
        }
        
        ScheduleLogTrace.scheduleInfoLog(brief, StringUtils.join("从hdfs路径", source, "拷贝至本地", target, " 开始"));
        
        try
        {
            hdfsService.copyToLocal(source, target);
            
            ScheduleLogTrace.scheduleInfoLog(brief, StringUtils.join("从hdfs路径", source, "拷贝至本地", target, " 结束"));
        }
        catch (Exception e)
        {
            ScheduleLogTrace.scheduleWarnLog(brief, ExceptionUtils.getStackTrace(e));
        }
    }
    
}
