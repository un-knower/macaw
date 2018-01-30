package cn.migu.macaw.schedule.task.bean.procedure;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import cn.migu.macaw.common.log.LogUtils;
import cn.migu.macaw.schedule.dao.JobLogMapper;
import cn.migu.macaw.schedule.dao.ProcLogMapper;
import cn.migu.macaw.schedule.dao.ProcVariableMapMapper;
import cn.migu.macaw.schedule.dao.ProcVariableMapper;
import cn.migu.macaw.schedule.dao.ProcedureMapper;
import cn.migu.macaw.dag.idgraph.IdDag;
import cn.migu.macaw.schedule.api.model.JobLog;
import cn.migu.macaw.schedule.api.model.ProcLog;
import cn.migu.macaw.schedule.api.model.ProcVariable;
import cn.migu.macaw.schedule.api.model.ProcVariableMap;
import cn.migu.macaw.schedule.api.model.Procedure;
import cn.migu.macaw.schedule.service.IJobCommService;
import cn.migu.macaw.schedule.task.ITask;
import cn.migu.macaw.schedule.task.TaskNodeBrief;
import cn.migu.macaw.schedule.cache.JobTasksCache;
import cn.migu.macaw.schedule.cache.ProcContextCache;
import cn.migu.macaw.schedule.task.datasource.DataSourceAdapter;
import cn.migu.macaw.schedule.task.datasource.DataSourceFlatAttr;
import cn.migu.macaw.schedule.task.util.ServiceReqClient;
import cn.migu.macaw.schedule.task.util.SparkEventHandler;
import cn.migu.macaw.schedule.task.util.SparkResourceMgr;
import cn.migu.macaw.schedule.util.ScheduleLogTrace;
import cn.migu.macaw.schedule.workflow.DataConstants;
import tk.mybatis.mapper.entity.Example;

/**
 * 初始化存储过程变量
 * 
 * @author soy
 */
@Component("procInitParamTask")
public class ProcedureInitTask implements ITask
{
    @Resource
    private ProcContextCache contextCache;
    
    @Resource
    private ProcVariableMapper procVarDao;
    
    @Resource
    private ProcVariableMapMapper varMapDao;
    
    @Resource
    private ProcLogMapper procLogDao;
    
    @Resource
    private ProcedureMapper procDao;
    
    @Resource
    private JobLogMapper jobLogDao;
    
    @Resource
    private ServiceReqClient client;
    
    @Resource
    private DataSourceAdapter dataSourceAdapter;
    
    @Resource
    private JobTasksCache jobTasksCache;
    
    @Resource
    private IJobCommService jobCommService;
    
    @Resource
    private SparkResourceMgr srm;
    
    @Resource
    private SparkEventHandler sparkEventHandler;
    
    @Override
    public void run(TaskNodeBrief brief, IdDag<String> dag)
        throws Exception
    {
        
        List<ProcVariable> variables = this.getProcVariables(brief.getJobCode());
        
        if (CollectionUtils.isEmpty(variables))
        {
            String errMsg = StringUtils.join("[", brief.getJobCode(), "]未定义变量");
            ScheduleLogTrace.scheduleWarnLog(brief, errMsg);
            throw new IllegalStateException(errMsg);
        }
        
        ScheduleLogTrace.scheduleInfoLog(brief, StringUtils.join("存储过程中变量数量为", String.valueOf(variables.size())));
        
        StringBuilder procVars = new StringBuilder();
        
        procVars.append("存储过程变量:");
        variables.forEach(var -> {
            this.setProcEnvVars(brief,
                StringUtils.trim(var.getCode()),
                StringUtils.trim(var.getDefaultValue()),
                procVars);
        });
        
        String procVarsStr = procVars.deleteCharAt(procVars.length() - 1).toString();
        
        jobTasksCache.append(brief.getJobCode(), brief.getNodeId(), DataConstants.NODE_RUNNING_TRACE, procVarsStr);
        ScheduleLogTrace.scheduleInfoLog(brief, procVarsStr);
        
        //临时定义过程变量
        procVars.setLength(0);
        procVars.append("存储过程临时变量:");
        this.loadCustomVars(brief, procVars);
        String procTmpVarsStr = procVars.deleteCharAt(procVars.length() - 1).toString();
        jobTasksCache.append(brief.getJobCode(), brief.getNodeId(), DataConstants.NODE_RUNNING_TRACE, procTmpVarsStr);
        ScheduleLogTrace.scheduleInfoLog(brief, procTmpVarsStr);
        
        Procedure proc = jobCommService.getProcedure(brief.getJobCode());
        
        this.updateProcLog(brief, variables, proc);
        
        //计算中心driver申请
        this.allocDriver(proc, brief);
    }
    
    /**
     * 申请driver
     * @param proc
     * @param brief
     * @throws Exception
     * @see [类、类#方法、类#成员]
     */
    private void allocDriver(Procedure proc, TaskNodeBrief brief)
        throws Exception
    {
        //spark driver context继承处理
        String pJobCode = contextCache.get(brief.getJobCode(), genLabelKey(DataConstants.PARENT_JOB_CODE));
        if (StringUtils.isNotEmpty(pJobCode))
        {
            boolean isUnInheritedAndUnUsed = srm.getSparkCtxInheritedJobScope(pJobCode);
            if (isUnInheritedAndUnUsed)
            {
                String pAppId = srm.getSparkContextAppIdJobScope(pJobCode);
                jobTasksCache.put(brief.getJobCode(), "-", DataConstants.SPARK_CONTEXT_APPID, pAppId);
                srm.setSparkCtxUnusedJobScope(pJobCode);
                return;
            }
        }
        
        sparkEventHandler.allocDriverNode(proc, brief);
        
    }

    /**
     * 获取存储过程下的所有变量定义
     * @param jobCode 任务编码
     * @return List<ProcVariable>- 存储过程所有变量
     */
    private List<ProcVariable> getProcVariables(String jobCode)
    {
        ProcVariable vari = new ProcVariable();
        vari.setJobCode(jobCode);
        
        return procVarDao.select(vari);
    }
    
    /**
     * 设置存储过程变量至执行上下文
     * @param brief 任务节点简明信息
     * @param varName 变量名称
     * @param varValue 变量值
     * @param procVars
     * @see [类、类#方法、类#成员]
     */
    private void setProcEnvVars(TaskNodeBrief brief, String varName, String varValue, StringBuilder procVars)
    {
        
        if (StringUtils.isNotEmpty(varName))
        {
            try
            {
                String value = this.initValue(brief, varValue);
                
                contextCache.put(brief.getJobCode(), genLabelKey(varName), value);
                
                procVars.append(StringUtils.join(varName, "=", value, ","));
                
             }
            catch (Exception e)
            {
                e.printStackTrace();
                ScheduleLogTrace.scheduleWarnLog(brief, ExceptionUtils.getStackTrace(e));
            }
        }
        else
        {
            ScheduleLogTrace.scheduleWarnLog(brief,
                StringUtils.join("[", brief.getJobCode(), "]-[", varName, "]-[", varValue, "]"));
        }
        
    }
    
    /**
     * 变量初始化
     * @param brief
     * @param defaultValue
     * @return
     * @see [类、类#方法、类#成员]
     */
    private String initValue(TaskNodeBrief brief, String defaultValue)
    {
        String intValue = defaultValue;
        
        if (isSql(defaultValue))
        {
            List<Object[]> vs;
            try
            {
                DataSourceFlatAttr dsAttr =
                    dataSourceAdapter.getNodeDataSourceConf(brief.getJobCode(), brief.getNodeId());
                
                //dsAttr = (null == dsAttr) ? new DataSourceFlatAttr() : dsAttr;
                vs = client.executeJdbcQuery(defaultValue, brief, dsAttr, null, "VALUE");
                if (CollectionUtils.isNotEmpty(vs))
                {
                    Object[] ov = vs.get(0);
                    if (ArrayUtils.isNotEmpty(ov))
                    {
                        intValue = (String)ov[0];
                    }
                }
            }
            catch (Exception e)
            {
                ScheduleLogTrace.scheduleWarnLog(brief, ExceptionUtils.getStackTrace(e));
                intValue = "";
            }
            
        }
        
        return intValue;
    }
    
    /**
     * 是否是sql
     * @param vStr
     * @return
     * @see [类、类#方法、类#成员]
     */
    private boolean isSql(String vStr)
    {
        return StringUtils.startsWith(vStr, "select");
    }
    
    /**
     * 构造标签键
     * @param key
     * @return
     * @see [类、类#方法、类#成员]
     */
    private String genLabelKey(String key)
    {
        return StringUtils.join("${", key, "}");
    }
    
    /**
     * 自定义临时变量载入
     * @param brief
     * @see [类、类#方法、类#成员]
     */
    private void loadCustomVars(TaskNodeBrief brief, StringBuilder procVars)
    {
        ProcVariableMap tVarMap = new ProcVariableMap();
        tVarMap.setJobCode(brief.getJobCode());
        
        List<ProcVariableMap> varMaps = varMapDao.select(tVarMap);
        
        if (CollectionUtils.isEmpty(varMaps))
        {
            return;
        }
        
        for (ProcVariableMap varMap : varMaps)
        {
            this.setProcEnvVars(brief,
                StringUtils.trim(varMap.getVariCode()),
                StringUtils.trim(varMap.getDefaultValue()),
                procVars);
        }
        
    }
    
    /**
     * 更新存储过程日志
     * @param brief
     * @see [类、类#方法、类#成员]
     */
    private void updateProcLog(TaskNodeBrief brief, List<ProcVariable> variables, Procedure proc)
    {
        String pDayVar = contextCache.get(brief.getJobCode(), genLabelKey("p_day"));
        String pRedoVar = contextCache.get(brief.getJobCode(), genLabelKey("p_redo"));
        
        updateProcJobSumDate(pDayVar, brief);
        
        //其他输入参数,只记录输入参数
        String othersVars = variables.stream()
            .filter(x -> (!StringUtils.equals(x.getCode(), "p_day") && !StringUtils.equals(x.getCode(), "p_redo")
                && StringUtils.isNotEmpty(x.getCode()) && StringUtils.equals(x.getKind().toString(), "1")))
            .map(s -> StringUtils.join(s.getCode(), "=", s.getDefaultValue()))
            .collect(Collectors.joining(","));
        
        Example example = new Example(ProcLog.class);
        example.createCriteria().andEqualTo("jobCode", brief.getJobCode()).andIsNull("jobBatchno");
        
        List<ProcLog> pls = procLogDao.selectByExample(example);
        if (!CollectionUtils.isEmpty(pls))
        {
            
            for (ProcLog pl : pls)
            {
                pl.setJobBatchno(brief.getBatchCode());
                
                procLogDao.updateByPrimaryKeySelective(pl);
            }
        }
        else
        {
            if (null != proc)
            {
                ProcLog procLog = new ProcLog();
                procLog.setBatchno(brief.getBatchCode());
                procLog.setJobCode(brief.getJobCode());
                procLog.setJobBatchno(brief.getBatchCode());
                procLog.setType(0);
                
                String pProc = contextCache.get(brief.getJobCode(), genLabelKey(DataConstants.PARENT_PROC_CODE));
                
                pProc = StringUtils.isEmpty(pProc) ? "定时任务|远程调用" : pProc;
                
                procLog.setDealUser(pProc);
                
                if (StringUtils.isNotEmpty(pDayVar))
                {
                    procLog.setpDay(pDayVar);
                }
                if (StringUtils.isNotEmpty(pRedoVar))
                {
                    procLog.setpRedo(pRedoVar);
                }
                if (StringUtils.isNotEmpty(othersVars))
                {
                    procLog.setOthersVars(othersVars);
                }
                procLog.setStartTime(new Date());
                
                procLog.setProcCode(proc.getCode());
                
                procLogDao.insertSelective(procLog);
            }
            
        }
        
    }
    
    /**
     * 更新sumdate
     * @param pDayVar
     * @param brief
     * @see [类、类#方法、类#成员]
     */
    private void updateProcJobSumDate(String pDayVar, TaskNodeBrief brief)
    {
        JobLog jobLog = new JobLog();
        jobLog.setBatchno(brief.getBatchCode());
        jobLog.setJobCode(brief.getJobCode());
        
        try
        {
            JobLog tJobLog = jobLogDao.selectOne(jobLog);
            if (null != tJobLog)
            {
                jobLog.setSumDate(pDayVar);
                jobLog.setObjId(tJobLog.getObjId());
                jobLogDao.updateByPrimaryKeySelective(jobLog);
            }
        }
        catch (Exception e)
        {
            LogUtils.runLogError(ExceptionUtils.getStackTrace(e));
        }
        
    }
    
}