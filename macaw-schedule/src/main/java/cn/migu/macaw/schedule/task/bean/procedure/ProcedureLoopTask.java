package cn.migu.macaw.schedule.task.bean.procedure;

import java.util.Map;

import javax.annotation.Resource;

import cn.migu.macaw.schedule.service.IJobLogService;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.migu.macaw.schedule.dao.NodeLogMapper;
import cn.migu.macaw.dag.idgraph.IdDag;
import cn.migu.macaw.schedule.api.model.NodeLog;
import cn.migu.macaw.schedule.task.ITask;
import cn.migu.macaw.schedule.task.TaskNodeBrief;
import cn.migu.macaw.schedule.cache.JobTasksCache;
import cn.migu.macaw.schedule.task.util.CallProcedureUtil;
import cn.migu.macaw.schedule.task.util.ConfigParamUtil;
import cn.migu.macaw.schedule.task.util.StringTagReplaceUtil;
import cn.migu.macaw.schedule.util.ExpressionUtil;
import cn.migu.macaw.schedule.util.ScheduleLogTrace;
import cn.migu.macaw.schedule.workflow.DataConstants;

/**
 * 支持存储过程循环
 * 
 * @author soy
 */
@Component("procedureLoopTask")
public class ProcedureLoopTask implements ITask
{
    /**
     * 最小循环因子
     */
    private final String LOOP_ITER_MIN = "_loop_iter_min";

    /**
     * 最大循环因子
     */
    private final String LOOP_ITER_MAX = "_loop_iter_max";

    /**
     * 循环因子替换值
     */
    private final String LOOP_FACTOR_LABEL = "_loop_factor_label";

    /**
     * continue条件
     */
    private final String LOOP_CONTINUE_CONDITION = "_loop_continue_expr";

    /**
     * break条件
     */
    private final String LOOP_BREAK_CONDITION = "_loop_break_expr";

    /**
     * 循环序列,格式为1,2,8,4,5或a,b,c 以逗号分隔
     */
    private final String LOOP_SEQ = "_loop_seq";
    
    @Resource
    private ConfigParamUtil configParamUtil;
    
    @Resource
    private StringTagReplaceUtil convertSqlUtil;
    
    @Resource
    private JobTasksCache jobTasksCache;
    
    @Resource
    private CallProcedureUtil callProcedureHandler;
    
    @Resource
    private NodeLogMapper nodeLogDao;

    @Autowired
    private IJobLogService jobLogService;
    
    @Override
    public void run(TaskNodeBrief brief, IdDag<String> dag)
        throws Exception
    {
        
        Map<String, String> params = configParamUtil.getJobNodeSysParams(brief);
        
        if (MapUtils.isEmpty(params) || !params.containsKey(LOOP_ITER_MIN) || !params.containsKey(LOOP_ITER_MAX))
        {
            if (!params.containsKey(LOOP_SEQ))
            {
                throw new IllegalArgumentException(StringUtils.join("没有配置循环参数:[",
                    LOOP_ITER_MIN,
                    ",",
                    LOOP_ITER_MAX,
                    "]或[",
                    LOOP_SEQ,
                    "]"));
            }
            else
            {
                
            }
            
        }
        else
        {
            String minIdx = params.get(LOOP_ITER_MIN);
            
            String maxIdx = params.get(LOOP_ITER_MAX);
            
            if (StringUtils.isEmpty(minIdx) || StringUtils.isEmpty(maxIdx))
            {
                throw new NullPointerException(StringUtils.join(LOOP_ITER_MIN, "或", LOOP_ITER_MAX, "为空"));
            }
            
            minIdx = convertSqlUtil.replaceLabelsInCtx(brief.getJobCode(), minIdx);
            maxIdx = convertSqlUtil.replaceLabelsInCtx(brief.getJobCode(), maxIdx);
            
            minIdx = minIdx.trim();
            maxIdx = maxIdx.trim();
            
            if (!NumberUtils.isNumber(minIdx) || !NumberUtils.isNumber(maxIdx))
            {
                throw new IllegalArgumentException(StringUtils.join(LOOP_ITER_MIN,
                    "或",
                    LOOP_ITER_MAX,
                    "非数字[",
                    minIdx,
                    ",",
                    maxIdx,
                    "]"));
            }
            
            final String loopFactorLabel = params.get(LOOP_FACTOR_LABEL);
            if (StringUtils.isEmpty(loopFactorLabel))
            {
                ScheduleLogTrace.scheduleWarnLog(brief, "循环因子替换标签为空");
            }

            
            final String continueExpr = params.get(LOOP_CONTINUE_CONDITION);
            
            final String breakExpr = params.get(LOOP_BREAK_CONDITION);
            
            int sIdx = Integer.valueOf(minIdx);
            int eIdx = Integer.valueOf(maxIdx);
            
            Map<String, String> sysParams = callProcedureHandler.getSysParams(brief);
            
            int counter = 0;
            
            for (int i = sIdx; i <= eIdx; i++)
            {
                if (StringUtils.isNotEmpty(continueExpr) && StringUtils.isNotEmpty(loopFactorLabel))
                {

                    String nContinueExpr = StringUtils.replace(continueExpr, loopFactorLabel, String.valueOf(i));
                    
                    if (ExpressionUtil.getInstance().evalBool(nContinueExpr))
                    {
                        continue;
                    }
                }
                
                if (StringUtils.isNotEmpty(breakExpr) && StringUtils.isNotEmpty(loopFactorLabel))
                {

                    String nBreakExpr = StringUtils.replace(breakExpr, loopFactorLabel, String.valueOf(i));
                    
                    if (ExpressionUtil.getInstance().evalBool(nBreakExpr))
                    {
                        break;
                    }
                }
                
                jobTasksCache.put(brief.getJobCode(), brief.getNodeId(), loopFactorLabel, String.valueOf(i));
                
                counter++;
                
                callProcedureHandler.call(brief, sysParams);
                
                String msg =
                    StringUtils.join("第",
                        String.valueOf(counter),
                        "次调用子过程",
                        callProcedureHandler.getCallProcName(sysParams),
                        "已结束");
                this.updateNodeLog(brief.getJobCode(), brief.getNodeId(), msg);
                
            }
            
            jobTasksCache.put(brief.getJobCode(),
                brief.getNodeId(),
                DataConstants.NODE_RUNNING_TRACE,
                StringUtils.join("循环调用子过程", callProcedureHandler.getCallProcName(sysParams), "已结束"));
        }
    }
    
    /**
     * 更新节点日志
     * @param jobCode
     * @param nodeCode
     * @param msg
     * @see [类、类#方法、类#成员]
     */
    private void updateNodeLog(String jobCode, String nodeCode, String msg)
    {
        NodeLog nodeLog = jobLogService.getRecentNodeLog(jobCode, nodeCode);
        nodeLog.setNote(msg);
        
        nodeLogDao.updateByPrimaryKeySelective(nodeLog);
    }
}
