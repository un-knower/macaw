package cn.migu.macaw.schedule.task.util;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import cn.migu.macaw.schedule.dao.ProcVariableMapMapper;
import cn.migu.macaw.schedule.dao.ProcedureMapper;
import cn.migu.macaw.schedule.api.model.Job;
import cn.migu.macaw.schedule.api.model.ProcVariableMap;
import cn.migu.macaw.schedule.api.model.Procedure;
import cn.migu.macaw.schedule.service.IJobCommService;
import cn.migu.macaw.schedule.task.TaskNodeBrief;
import cn.migu.macaw.schedule.cache.JobTasksCache;
import cn.migu.macaw.schedule.cache.ProcContextCache;
import cn.migu.macaw.schedule.util.ScheduleLogTrace;
import cn.migu.macaw.schedule.workflow.DataConstants;

/**
 * 调用存储过程工具类
 * 
 * @author soy
 */
@Component("callProcedureHanler")
public class CallProcedureUtil
{

    /**
     * 存储过程名称
     */
    private final String PROCEDURE_NAME = "procedure_name";
    
    @Resource
    private ProcedureMapper procedureDao;
    
    @Resource
    private ProcVariableMapMapper procVarMapDao;
    
    @Resource
    private ConfigParamUtil configParamUtil;
    
    @Resource
    private JobTasksCache jobCtxCache;
    
    @Resource
    private ProcContextCache procCtxCache;
    
    @Resource
    private IJobCommService jobCommService;
    
    @Resource
    private JobTriggerUtil jobTriggerUtil;
    
    public Map<String, String> getSysParams(TaskNodeBrief brief)
    {
        return configParamUtil.getJobNodeSysParams(brief);
    }
    
    public String getCallProcName(Map<String, String> params)
        throws IllegalArgumentException
    {
        if (MapUtils.isEmpty(params) || !params.containsKey(PROCEDURE_NAME)
            || StringUtils.isEmpty(params.get(PROCEDURE_NAME)))
        {
            throw new IllegalArgumentException("需要配置调用的存储过程名称(procedure_name)");
        }
        
        return params.get(PROCEDURE_NAME);
    }
    
    public void call(TaskNodeBrief brief, Map<String, String> sysParams)
        throws Exception
    {
        String parentProcCode = brief.getJobCode();

        if (MapUtils.isEmpty(sysParams) || !sysParams.containsKey(PROCEDURE_NAME)
            || StringUtils.isEmpty(sysParams.get(PROCEDURE_NAME)))
        {
            throw new IllegalArgumentException("需要配置调用的存储过程名称(procedure_name)");
        }
        
        String procName = sysParams.get(PROCEDURE_NAME);
        
        Procedure procedure = new Procedure();
        procedure.setCode(procName);
        Procedure tProcedure = procedureDao.selectOne(procedure);
        if (null == tProcedure)
        {
            String procNotExistErr = StringUtils.join("[", procName, "]存储过程不存在");
            ScheduleLogTrace.scheduleWarnLog(brief, procNotExistErr);
            throw new RuntimeException(procNotExistErr);
        }
        
        //复用对象查询本job对应的存储过程
        procedure.setCode(null);
        procedure.setJobCode(parentProcCode);
        Procedure tProc = procedureDao.selectOne(procedure);
        if (null != tProc)
        {
            parentProcCode = tProc.getCode();
        }
        
        String jobCode = tProcedure.getJobCode();
        
        if (StringUtils.isEmpty(jobCode))
        {
            throw new RuntimeException("存储过程对应的job不存在");
        }
        
        ScheduleLogTrace.scheduleInfoLog(brief, StringUtils.join("存储过程[", procName, "]对应的job为:", jobCode));
        
        //同步调用存储过程变量
        this.synProcTempVar(brief, parentProcCode, tProcedure.getCode(), jobCode);
        
        //job调用,同步返回值状态
        jobTriggerUtil.triggerAndWait(brief, jobCode, sysParams, true, procName, parentProcCode);
        
    }
    
    /**
     * 同步调用存储过程临时变量
     * @param brief
     * @param procCode
     * @see [类、类#方法、类#成员]
     */
    private void synProcTempVar(TaskNodeBrief brief, String parentProcCode, String procCode, String subJobCode)
    {
        ProcVariableMap pvm = new ProcVariableMap();
        pvm.setJobCode(subJobCode);
        pvm.setProcCode(procCode);
        
        Job job = jobCommService.getJob(brief.getJobCode());
        if (null == job)
        {
            return;
        }
        else if (0 != job.getKind().intValue())
        {
            //循环因子
            this.synLoopFactor(brief, pvm);
            
            return;
        }
        
        Map<String, String> procVars = configParamUtil.getJobNodeProcParams(brief);
        
        //父存储过程编码
        pvm.setVariCode(DataConstants.PARENT_PROC_CODE);
        pvm.setDefaultValue(parentProcCode);
        
        procVarMapDao.insertSelective(pvm);
        
        //父任务编码
        pvm.setVariCode(DataConstants.PARENT_JOB_CODE);
        pvm.setDefaultValue(brief.getJobCode());
        procVarMapDao.insertSelective(pvm);
        
        if (MapUtils.isNotEmpty(procVars))
        {
            
            for (Map.Entry<String, String> entry : procVars.entrySet())
            {
                if (StringUtils.isNotEmpty(entry.getKey()) && StringUtils.isNotEmpty(entry.getValue()))
                {
                    String value = StringUtils.trim(entry.getValue());
                    String defaultValue = procCtxCache.get(brief.getJobCode(), value);
                    if (StringUtils.isEmpty(defaultValue))
                    {
                        defaultValue = jobCtxCache.get(brief.getJobCode(), brief.getNodeId(), value);
                    }
                    
                    ScheduleLogTrace.scheduleInfoLog(brief, "调用存储过程临时变量[" + entry.getKey() + "=" + defaultValue + "]");
                    if (StringUtils.isNotEmpty(defaultValue))
                    {
                        pvm.setJobCode(subJobCode);
                        pvm.setProcCode(procCode);
                        pvm.setVariCode(StringUtils.trim(entry.getKey()));
                        
                        pvm.setDefaultValue(defaultValue);
                        
                        procVarMapDao.insertSelective(pvm);
                    }
                    
                }
            }
        }
        
    }
    
    /**
     * 同步循环因子变量(如果有)
     * @param brief
     * @param pvm
     * @see [类、类#方法、类#成员]
     */
    private void synLoopFactor(TaskNodeBrief brief, ProcVariableMap pvm)
    {
        String loopFactorName = jobCtxCache.get(brief.getJobCode(), "-", DataConstants.VAR_NAME_FOR_JOB_LOOP);
        if (StringUtils.isNotEmpty(loopFactorName))
        {
            String loopFactorVal = jobCtxCache.get(brief.getJobCode(), "-", DataConstants.VAR_VAL_FOR_JOB_LOOP);
            if (StringUtils.isNotEmpty(loopFactorVal))
            {
                //解析循环因子名字,格式应为${x}
                pvm.setVariCode(StringUtils.substringBetween(loopFactorName, "${", "}"));
                pvm.setDefaultValue(loopFactorVal);
                procVarMapDao.insertSelective(pvm);
                
            }
        }
    }
}
