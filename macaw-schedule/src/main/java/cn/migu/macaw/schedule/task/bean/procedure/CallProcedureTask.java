package cn.migu.macaw.schedule.task.bean.procedure;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import cn.migu.macaw.dag.idgraph.IdDag;
import cn.migu.macaw.schedule.task.ITask;
import cn.migu.macaw.schedule.task.TaskNodeBrief;
import cn.migu.macaw.schedule.task.util.CallProcedureUtil;

/**
 * 存储过程调用task
 * 调用一个存储过程直到该存储过程执行完毕
 * 
 * @author soy
 */
@Component("callProcedureTask")
public class CallProcedureTask implements ITask
{
    
    @Resource
    private CallProcedureUtil callProcedureHander;
    
    @Override
    public void run(TaskNodeBrief brief, IdDag<String> dag)
        throws Exception
    {
        Map<String, String> sysParams = callProcedureHander.getSysParams(brief);
        
        callProcedureHander.call(brief, sysParams);
        
    }
    
}
