package cn.migu.macaw.schedule.task;

import cn.migu.macaw.dag.idgraph.IdDag;

/**
 * 
 * task接口
 * 所有任务都需要实现此接口
 * 
 * @author soy
 */
public interface ITask
{
    /**
     * 任务节点业务处理
     * @param brief 任务元信息
     * @param dag   任务有向图,如果有需要则使用
     * @throws Exception
     * @see [类、类#方法、类#成员]
     */
    void run(TaskNodeBrief brief, IdDag<String> dag)
        throws Exception;
}
