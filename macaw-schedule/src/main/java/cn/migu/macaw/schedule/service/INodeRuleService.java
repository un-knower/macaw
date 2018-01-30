package cn.migu.macaw.schedule.service;

import cn.migu.macaw.schedule.task.TaskNodeBrief;

import java.util.Map;

/**
 * 节点规则业务类
 * 
 * @author soy
 */
public interface INodeRuleService
{
    /**
     * 获取节点对应的规则表达式
     * @param brief 节点简明信息
     * @return
     * @see [类、类#方法、类#成员]
     */
    Map<String, String> getRuleExpr(TaskNodeBrief brief);
}
