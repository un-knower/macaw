package cn.migu.macaw.schedule.task.bean;

import org.springframework.stereotype.Component;

import cn.migu.macaw.dag.idgraph.IdDag;
import cn.migu.macaw.schedule.task.ITask;
import cn.migu.macaw.schedule.task.TaskNodeBrief;
import cn.migu.macaw.schedule.util.ScheduleLogTrace;

/**
 * 测试task,带睡眠时间
 *
 * @author soy
 */
@Component("sleepTestTask")
public class SleepTestTask implements ITask
{
    
    @Override
    public void run(TaskNodeBrief brief, IdDag<String> dag)
        throws Exception
    {
        ScheduleLogTrace.scheduleInfoLog(brief, "sleep test task begin");
        
        Thread.sleep(60 * 1000);
        
        ScheduleLogTrace.scheduleInfoLog(brief, "sleep test task end");
        
    }
    
}
