package cn.migu.macaw.schedule.task.bean;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import cn.migu.macaw.schedule.task.util.LabelTag;
import com.alibaba.fastjson.JSON;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import cn.migu.macaw.dag.alg.PathsLib;
import cn.migu.macaw.dag.idgraph.IdDag;
import cn.migu.macaw.schedule.service.INodeRuleService;
import cn.migu.macaw.schedule.task.ITask;
import cn.migu.macaw.schedule.task.TaskNodeBrief;
import cn.migu.macaw.schedule.cache.JobTasksCache;
import cn.migu.macaw.schedule.task.util.SqlServiceUtil;
import cn.migu.macaw.schedule.util.ExpressionUtil;
import cn.migu.macaw.schedule.util.ScheduleLogTrace;
import cn.migu.macaw.schedule.workflow.DataConstants;

import com.google.common.collect.ImmutableSet;

/**
 * 决策节点类
 * 
 * @author soy
 */
@Component("decisionTask")
public class DecisionTask implements ITask
{
    
    @Resource
    private JobTasksCache jobTasksCache;
    
    @Resource
    private INodeRuleService nodeRuleService;
    
    @Resource
    private SqlServiceUtil sqlService;
    
    @Override
    public void run(TaskNodeBrief brief, IdDag<String> dag)
        throws Exception
    {
        String jobCode = brief.getJobCode();
        
        String nodeId = brief.getNodeId();

        //如果有运行sql,会将执行结果保存至上下文
        sqlService.queryWithRet(brief);
        
        //设置取消后代节点执行标记
        //当前判断节点的所有后代
        ImmutableSet<String> descendants = dag.descendantIdSet(nodeId, false);
        
        //先把所有节点设置为取消
        for (String node : descendants)
        {
            //增加已确定分支节点判断,防止已生效节点被屏蔽
            if (!jobTasksCache.contains(jobCode, node, DataConstants.CANNOT_CANCEL))
            {
                jobTasksCache.put(jobCode, node, DataConstants.CANCEL_RUN, "1");
            }
        }
        
        //获取所有子节点
        ImmutableSet<String> children = dag.childIdSet(nodeId);
        
        Map<String, String> nodeExpression = nodeRuleService.getRuleExpr(brief);
        
        if (MapUtils.isEmpty(nodeExpression))
        {
            ScheduleLogTrace.scheduleWarnLog(brief, "没有配置决策规则或规则无效");
            throw new IllegalStateException(StringUtils.join("[",
                brief.getJobCode(),
                "]-决策节点[",
                brief.getNodeId(),
                "]未配置规则或规则无效"));
        }

        ScheduleLogTrace.scheduleInfoLog(brief,String.format("决策路径表达式:%s", JSON.toJSONString(nodeExpression)));

        if(nodeExpression.entrySet().stream().filter(e -> StringUtils.contains(e.getValue(), LabelTag.LABEL_PREFIX)).count() > 0)
        {
            ScheduleLogTrace.scheduleWarnLog(brief, "存在未处理的判断表达式");
            throw new IllegalStateException(StringUtils.join("[",
                brief.getJobCode(),
                "]-决策节点[",
                brief.getNodeId(),
                "]存在未处理的判断表达式"));
        }

        
        ExpressionUtil exprUtil = ExpressionUtil.getInstance();
        
        for (String child : children)
        {
            String expr = nodeExpression.get(child);
            
            if (StringUtils.isEmpty(expr))
            {
                ScheduleLogTrace.scheduleWarnLog(brief, StringUtils.join("没有配置规则详情,节点[", child, "]将被取消执行"));
                continue;
            }
            
            String exprMsg = StringUtils.join("子节点", child, "决策表达式为:", expr);
            ScheduleLogTrace.scheduleInfoLog(brief, exprMsg);
            
            jobTasksCache.append(jobCode, nodeId, DataConstants.NODE_RUNNING_TRACE, exprMsg);
            
            boolean flag = exprUtil.evalBool(expr);
            
            if (flag)
            {
                jobTasksCache.putList(jobCode, nodeId, DataConstants.DICISION_BRANCH, child);
            }
        }
        
        //分支节点集取消处理流程
        //设置取消执行的节点
        String[] branchs = jobTasksCache.getList(jobCode, nodeId, DataConstants.DICISION_BRANCH);
        if (ArrayUtils.isNotEmpty(branchs))
        {
            for (String branch : branchs)
            {
                if (StringUtils.isNotEmpty(branch))
                {
                    ImmutableSet<String> branchDescends = dag.descendantIdSet(branch, true);
                    
                    for (String dn : branchDescends)
                    {
                        List<List<String>> paths = PathsLib.getAllPaths(branch, dn, dag);
                        for (List<String> ps : paths)
                        {
                            if (ps.size() == 1)
                            {
                                jobTasksCache.remove(jobCode, ps.get(0), DataConstants.CANCEL_RUN);
                                jobTasksCache.put(jobCode, ps.get(0), DataConstants.CANNOT_CANCEL, "1");
                            }
                            //判断路径节点是否存在动态决策节点,排除最后一个节点为决策节点的情况
                            if (this.checkSubPath(ps, brief))
                            {
                                for (String dp : ps)
                                {
                                    jobTasksCache.remove(jobCode, dp, DataConstants.CANCEL_RUN);
                                    jobTasksCache.put(jobCode, dp, DataConstants.CANNOT_CANCEL, "1");
                                }
                            }
                        }
                    }

                }
            }
        }
        
    }
    
    /**
     * 检测路径节点类型
     * @param path
     * @param brief
     * @return
     * @see [类、类#方法、类#成员]
     */
    private boolean checkSubPath(List<String> path, TaskNodeBrief brief)
    {
        for (int i = 0; i < path.size() - 1; i++)
        {
            String node = path.get(i);
            
            String taskClass = jobTasksCache.get(brief.getJobCode(), node, DataConstants.TASK_NODE_TYPE);
            if (StringUtils.equals(taskClass, DataConstants.TASK_TYPE_DECISION))
            {
                return false;
            }
        }
        
        return true;
    }
    
}
