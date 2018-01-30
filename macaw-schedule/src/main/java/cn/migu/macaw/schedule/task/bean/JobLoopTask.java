package cn.migu.macaw.schedule.task.bean;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;

import cn.migu.macaw.schedule.dao.JobParamMapper;
import cn.migu.macaw.dag.idgraph.IdDag;
import cn.migu.macaw.schedule.api.model.JobParam;
import cn.migu.macaw.schedule.task.ITask;
import cn.migu.macaw.schedule.task.TaskNodeBrief;
import cn.migu.macaw.schedule.task.util.ConfigParamUtil;
import cn.migu.macaw.schedule.task.util.JobTriggerUtil;
import cn.migu.macaw.schedule.task.util.StringTagReplaceUtil;
import cn.migu.macaw.schedule.util.ScheduleLogTrace;

/**
 * 循环执行job,并且把循环变量传递给执行任务
 * 
 * @author soy
 */
@Component("jobLoopTask")
public class JobLoopTask implements ITask
{
    /**
     * 要执行的任务编码
     */
    private final String JOB_CODE_KEY = "job_code";

    /**
     * 循环变量初始值
     */
    private final String LOOP_FACTOR_INIT_VALUE = "loop_factor_init_val";

    /**
     * 循环变量最大值
     */
    private final String LOOP_FACTOR_MAX_VALUE = "loop_factor_max_val";

    /**
     * 循环变量步长
     */
    private final String LOOP_STEP_KEY = "loop_step";

    /**
     * 传递给job的循环因子变量名
     */
    private final String LOOP_FACTOR_VAR = "loop_factor_var";

    /**
     * 每次循环等待时间(单位s)
     */
    private final String WAIT_TIME_EVERY_TIME = "wait_time_for_loop";
    
    @Resource
    private ConfigParamUtil configParamUtil;
    
    @Resource
    private StringTagReplaceUtil strTagReplaceUtil;
    
    @Resource
    private JobTriggerUtil jobTriggerUtil;
    
    @Resource
    private JobParamMapper jobParamDao;
    
    @Override
    public void run(TaskNodeBrief brief, IdDag<String> dag)
        throws Exception
    {
        Map<String, String> sysParams = configParamUtil.getJobNodeSysParams(brief);
        if (MapUtils.isEmpty(sysParams))
        {
            throw new IllegalArgumentException("配置参数为空");
        }
        
        String jobCode = sysParams.get(JOB_CODE_KEY);
        if (StringUtils.isEmpty(jobCode))
        {
            throw new IllegalArgumentException("没有配置循环的任务编码job_code");
        }
        
        String outVarStr = sysParams.get(LOOP_FACTOR_VAR);
        if (StringUtils.isEmpty(outVarStr))
        {
            throw new IllegalArgumentException("没有配置输出变量" + LOOP_FACTOR_VAR);
        }
        
        if (!strTagReplaceUtil.isRepLabelValid(outVarStr))
        {
            throw new IllegalArgumentException("输出变量格式错误,正确为${x}");
        }
        
        String loopInitStr = sysParams.get(LOOP_FACTOR_INIT_VALUE);
        if (StringUtils.isEmpty(loopInitStr))
        {
            throw new IllegalArgumentException("没有配置循环初始值" + LOOP_FACTOR_INIT_VALUE);
        }
        
        if (!NumberUtils.isNumber(loopInitStr))
        {
            throw new IllegalArgumentException(LOOP_FACTOR_INIT_VALUE + "不是数字");
        }
        
        int loopInitVal = Integer.valueOf(loopInitStr);
        
        String loopMaxValStr = sysParams.get(LOOP_FACTOR_MAX_VALUE);
        if (StringUtils.isEmpty(loopMaxValStr))
        {
            throw new IllegalArgumentException("没有配置循环最大值" + LOOP_FACTOR_MAX_VALUE);
        }
        
        if (!NumberUtils.isNumber(loopMaxValStr))
        {
            throw new IllegalArgumentException(LOOP_FACTOR_MAX_VALUE + "不是数字");
        }
        
        int loopMaxVal = Integer.valueOf(loopMaxValStr);
        
        String loopStepStr = sysParams.get(LOOP_STEP_KEY);
        if (StringUtils.isEmpty(loopStepStr))
        {
            throw new IllegalArgumentException("没有配置循环步长" + LOOP_STEP_KEY);
        }
        
        if (!NumberUtils.isNumber(loopStepStr))
        {
            throw new IllegalArgumentException(LOOP_STEP_KEY + "不是数字");
        }
        
        int loopStep = Integer.valueOf(loopStepStr);
        
        String waitTimeStr = sysParams.get(WAIT_TIME_EVERY_TIME);
        if (StringUtils.isEmpty(waitTimeStr))
        {
            waitTimeStr = "0";
        }
        
        if (!NumberUtils.isNumber(waitTimeStr))
        {
            throw new IllegalArgumentException(WAIT_TIME_EVERY_TIME + "不是数字");
        }
        
        int waitTime = Integer.valueOf(waitTimeStr);
        waitTime = (waitTime < 0) ? 0 : waitTime * 1000;
        
        ScheduleLogTrace.scheduleInfoLog(brief, StringUtils.join("配置循环参数:init_val=",
            loopInitStr,
            ",top_val=",
            loopMaxValStr,
            ",step=",
            loopStepStr,
            ",call_job=",
            jobCode,
            ",循环变量名称=",
            outVarStr,
            ",每次循环执行后等待时间:",
            waitTimeStr,
            "s"));
        
        for (int i = loopInitVal; loopRstRegx(i, loopMaxVal, loopStep); i += loopStep)
        {
            //同步循环变量
            synLoopVar(brief, jobCode, outVarStr, String.valueOf(i));
            
            //调用job
            jobTriggerUtil.triggerAndWait(brief, jobCode, sysParams, false, null, null);
            
            //等待时间
            if (waitTime > 0)
            {
                Thread.sleep(waitTime);
            }
        }
        
    }
    
    /**
     * 循环结果表达式
     * @param idx 
     * @param maxVal
     * @param step
     * @return
     * @see [类、类#方法、类#成员]
     */
    private boolean loopRstRegx(final int idx, final int maxVal, final int step)
    {
        if (step < 0)
        {
            return idx >= maxVal;
        }
        else
        {
            return idx <= maxVal;
        }
    }
    
    /**
     * 同步循环变量到job参数表
     * @see [类、类#方法、类#成员]
     */
    private void synLoopVar(TaskNodeBrief brief, String subJobCode, String key, String value)
    {
        JobParam jobParam = new JobParam();
        jobParam.setJobCode(subJobCode);
        jobParam.setPkey(key);
        jobParam.setValue(value);
        //1.一般参数 2.sql替换参数 3.循环变量参数
        jobParam.setKind(3);
        
        jobParamDao.insertSelective(jobParam);
    }
}
