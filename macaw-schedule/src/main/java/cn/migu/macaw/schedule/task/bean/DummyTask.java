package cn.migu.macaw.schedule.task.bean;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import cn.migu.macaw.dag.idgraph.IdDag;
import cn.migu.macaw.schedule.task.ITask;
import cn.migu.macaw.schedule.task.TaskNodeBrief;

/**
 * do nothing task
 * 
 * @author soy
 */
@Component("dummyTask")
public class DummyTask implements ITask
{
    
    @Override
    public void run(TaskNodeBrief brief, IdDag<String> dag)
        throws Exception
    {
        
    }
    
}
