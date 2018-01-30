package cn.migu.macaw.schedule.task.bean.procedure;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import cn.migu.macaw.dag.idgraph.IdDag;
import cn.migu.macaw.schedule.task.ITask;
import cn.migu.macaw.schedule.task.TaskNodeBrief;
import cn.migu.macaw.schedule.cache.ProcContextCache;
import cn.migu.macaw.schedule.task.util.ConfigParamUtil;
import cn.migu.macaw.schedule.task.util.ServiceReqClient;
import cn.migu.macaw.schedule.task.util.SqlServiceUtil;
import cn.migu.macaw.schedule.task.util.StringTagReplaceUtil;
import cn.migu.macaw.schedule.util.ExpressionUtil;
import cn.migu.macaw.schedule.util.ScheduleLogTrace;

/**
 * 循环等待Task
 * 
 * @author soy
 */
@Component("procedureReplTask")
public class ProcedureReplTask implements ITask
{
    
    @Resource
    private ConfigParamUtil configParamUtil;
    
    @Resource
    private StringTagReplaceUtil convertSqlUtil;
    
    @Resource
    private ProcContextCache procContextCache;
    
    @Resource
    private SqlServiceUtil sqlServiceUtil;
    
    @Resource
    private ServiceReqClient client;

    /**
     * 循环次数
     */
    private final String LOOP_KEY = "_repl_loop_time";

    /**
     * 循环之间等待间隔
     */
    private final String INTERVAL_KEY = "_repl_interval";

    /**
     * 比较符号
     */
    private final String COMPARE_SIGN = "_repl_compare_sign";

    /**
     * 阈值
     */
    private final String THRESHOLD = "_repl_threshold";

    /**
     * 返回值
     */
    private final String RET_VALUE = "_repl_final_value";
    
    @Override
    public void run(TaskNodeBrief brief, IdDag<String> dag)
        throws Exception
    {
        Map<String, String> vars = configParamUtil.getJobNodeProcParams(brief);
        
        if (MapUtils.isEmpty(vars) || !vars.containsKey(LOOP_KEY) || !vars.containsKey(INTERVAL_KEY)
            || !vars.containsKey(COMPARE_SIGN) || !vars.containsKey(THRESHOLD))
        {
            String errMsg = "参数不合法:没有配置循环等待参数(循环次数,间隔时间,比较符号,比较阈值)";
            ScheduleLogTrace.scheduleWarnLog(brief, errMsg);
            throw new IllegalArgumentException(errMsg);
        }
        
        List<String> sql = configParamUtil.getJobNodeRunSqlParams(brief);
        
        if (CollectionUtils.isEmpty(sql))
        {
            String errMsg = "参数不合法:没有配置查询sql";
            ScheduleLogTrace.scheduleWarnLog(brief, errMsg);
            throw new IllegalArgumentException(errMsg);
        }
        
        String loop = this.getSpecParam(brief.getJobCode(), vars.get(LOOP_KEY));
        
        String interval = this.getSpecParam(brief.getJobCode(), vars.get(INTERVAL_KEY));
        
        String sign = this.getSpecParam(brief.getJobCode(), vars.get(COMPARE_SIGN));
        
        String threshold = this.getSpecParam(brief.getJobCode(), vars.get(THRESHOLD));
        
        if (!NumberUtils.isNumber(loop) || !NumberUtils.isNumber(interval))
        {
            String errMsg = StringUtils.join("参数不合法:循环次数或等待时间不是数字[", loop, ",", interval, "]");
            ScheduleLogTrace.scheduleWarnLog(brief, errMsg);
            throw new IllegalArgumentException(errMsg);
        }
        
        ScheduleLogTrace.scheduleInfoLog(brief, StringUtils.join("循环次数:", loop, ",时间间隔:", interval));
        
        int iLoop = Integer.valueOf(loop.trim());
        int iInterval = Integer.valueOf(interval);
        
        String runSql = this.getRunSql(brief.getJobCode(), sql.get(0));
        
        String retValue = null;
        
        ScheduleLogTrace.scheduleInfoLog(brief, StringUtils.join("判断表达式为[X]", sign, threshold));

        
        runSql = convertSqlUtil.replaceLabelsInNode(runSql, brief);
        
        runSql = convertSqlUtil.replaceLabelsInCtx(brief.getJobCode(), runSql);
        
        ScheduleLogTrace.scheduleInfoLog(brief, StringUtils.join("查询Sql语句:", runSql));
        
        for (int i = 0; i < iLoop; i++)
        {
            retValue = sqlServiceUtil.queryLocal(brief, runSql);
            
            boolean forward = this.compare(StringUtils.join(retValue, sign, threshold));
            if (forward)
            {
                break;
            }
            
            Thread.sleep(iInterval);
        }
        
        ScheduleLogTrace.scheduleInfoLog(brief, StringUtils.join("loop-wait-task查询返回值为:", retValue));
        
        //最终返回值存上下文缓存
        if (vars.containsKey(RET_VALUE) && StringUtils.isNotEmpty(retValue))
        {
            String cacheKey = vars.get(RET_VALUE);
            ScheduleLogTrace.scheduleInfoLog(brief, StringUtils.join("缓存键值对=>", cacheKey, "=", retValue));
            procContextCache.put(brief.getJobCode(), cacheKey, retValue);
        }
        
    }
    
    /**
     * 查找运行sql
     * @param jobCode
     * @param sql
     * @return
     * @see [类、类#方法、类#成员]
     */
    private String getRunSql(String jobCode, String sql)
    {
        String tSql = this.getSpecParam(jobCode, sql);

        tSql = convertSqlUtil.replaceLabelsInCtx(jobCode, tSql);
        
        return tSql;
    }
    
    /**
     * 获取指定配置参数
     * @param jobCode
     * @param key
     * @return
     * @see [类、类#方法、类#成员]
     */
    private String getSpecParam(String jobCode, String key)
    {
        String value = key;
        
        if (convertSqlUtil.isRepLabelValid(key))
        {
            value = procContextCache.get(jobCode, key);
        }
        
        return value;
    }
    
    /**
     * 表达式比较
     * @param expr
     * @return
     * @see [类、类#方法、类#成员]
     */
    private boolean compare(String expr)
    {
        return ExpressionUtil.getInstance().evalBool(expr);
    }
    
}
