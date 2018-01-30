package cn.migu.macaw.schedule.task.bean.procedure;

import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Component;

import cn.migu.macaw.dag.idgraph.IdDag;
import cn.migu.macaw.schedule.task.ITask;
import cn.migu.macaw.schedule.task.TaskNodeBrief;
import cn.migu.macaw.schedule.task.util.ConfigParamUtil;
import cn.migu.macaw.schedule.util.ScheduleLogTrace;

/**
 * 存储过程异常任务
 * 
 * @author soy
 */
@Component("procedureExcepTask")
public class ProcedureExcepTask implements ITask
{
    
    @Resource
    private ConfigParamUtil configParam;
    
    @Override
    public void run(TaskNodeBrief brief, IdDag<String> dag)
        throws Exception
    {
        String msg = "异常节点";
        
        Map<String, String> vars = configParam.getJobNodeProcParams(brief);
        
        if (!MapUtils.isEmpty(vars))
        {
            msg = vars.entrySet().stream().map(m -> m.getValue()).collect(Collectors.joining());
        }
        
        ScheduleLogTrace.scheduleInfoLog(brief, msg);
        
        throw new RuntimeException(msg);
    }
    
}
