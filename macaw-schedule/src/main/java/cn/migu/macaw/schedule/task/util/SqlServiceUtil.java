package cn.migu.macaw.schedule.task.util;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;

import cn.migu.macaw.common.ServiceName;
import cn.migu.macaw.common.ServiceUrlProvider;
import cn.migu.macaw.schedule.cache.JobTasksCache;
import cn.migu.macaw.schedule.task.TaskNodeBrief;
import cn.migu.macaw.schedule.task.datasource.DataSourceAdapter;
import cn.migu.macaw.schedule.task.datasource.DataSourceFlatAttr;
import cn.migu.macaw.schedule.util.ScheduleLogTrace;
import cn.migu.macaw.schedule.workflow.DataConstants;

/**
 * 调度中心sql业务通用类
 *
 * @author soy
 */
@Component("sqlServiceUtil")
public class SqlServiceUtil
{
    private final int RETRY_TIME = 3;

    private final String SQL_SELECT = "select";
    
    private final String SPARK_SQL_SELECT_KEY = "_sparksql_ret_val";
    
    @Resource
    private JdbcTemplate localJdbc;
    
    @Resource
    private ServiceReqClient client;
    
    @Resource
    private ConfigParamUtil configParamUtil;
    
    @Resource
    private StringTagReplaceUtil convertSqlUtil;
    
    @Resource
    private JobTasksCache jobTasksCache;
    
    @Resource
    private DataSourceAdapter dataSourceAdapter;
    
    @Resource
    private SparkResourceMgr srm;
    
    /**
     * 是否是spark sql
     * @param dsAttr
     * @return
     * @see [类、类#方法、类#成员]
     */
    public boolean isSparkSql(DataSourceFlatAttr dsAttr)
    {
        return null == dsAttr || dsAttr.isSparkDs();
    }
    
    /**
     * 是否为jdbc hugetable连接
     * @param dsAttr
     * @return
     * @see [类、类#方法、类#成员]
     */
    public boolean isHtConnection(DataSourceFlatAttr dsAttr)
    {
        return ((null != dsAttr) && StringUtils.equals(dsAttr.getDriverClass(), "com.chinamobile.cmss.ht.Driver"));
    }
    
    /**
     * jdbc查询
     * @param brief
     * @param dsAttr
     * @param sql
     * @return
     * @see [类、类#方法、类#成员]
     */
    public String queryJdbc(TaskNodeBrief brief, DataSourceFlatAttr dsAttr, String sql)
    {
        String driverClassName = dsAttr.getDriverClass();
        String username = dsAttr.getUsername();
        String password = dsAttr.getPassword();
        String jdbcurl = dsAttr.getConnectAddr();
        
        JdbcTemplate jdbc = new JdbcTemplate();
        //String driver = this.getDriver(datasource);
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setPassword(null == password ? "" : password);
        dataSource.setUsername(username);
        dataSource.setUrl(jdbcurl);
        jdbc.setDataSource(dataSource);
        
        ScheduleLogTrace.scheduleInfoLog(brief, StringUtils.join("查询sql:", sql));
        
        String retValue = jdbc.queryForObject(sql, String.class);
        
        try
        {
            dataSource.close();
        }
        catch (SQLException e)
        {
        }
        
        return retValue;
        
    }
    
    /**
     * 本地数据库连接执行sql
     * @param sql
     * @return
     * @see [类、类#方法、类#成员]
     */
    public String queryLocal(TaskNodeBrief brief, String sql)
    {
        ScheduleLogTrace.scheduleInfoLog(brief, StringUtils.join("查询sql:", sql));
        return localJdbc.queryForObject(sql, String.class);
    }
    
    /**
     * jdbc查询
     * @param brief
     * @param dsAttr
     * @param sql
     * @return
     * @see [类、类#方法、类#成员]
     */
    public String query(TaskNodeBrief brief, DataSourceFlatAttr dsAttr, String sql)
    {
        return this.queryJdbc(brief, dsAttr, sql);
    }
    
    /**
     * 执行关系型数据库sql
     * @param dsAttr
     * @param sql
     * @param brief
     * @see [类、类#方法、类#成员]
     */
    public void executeRemote(DataSourceFlatAttr dsAttr, String sql, TaskNodeBrief brief)
    {
        String driverClass = dsAttr.getDriverClass();
        String username = dsAttr.getUsername();
        String password = dsAttr.getPassword();
        String jdbcurl = dsAttr.getConnectAddr();
        
        JdbcTemplate jdbc = new JdbcTemplate();
        
        //String driver = getDriver(driverClass);
        
        BasicDataSource dataSource = new BasicDataSource();
        
        dataSource.setDriverClassName(driverClass);
        
        dataSource.setPassword(null == password ? "" : password);
        
        dataSource.setUsername(username);
        
        dataSource.setUrl(jdbcurl);
        
        jdbc.setDataSource(dataSource);
        
        ScheduleLogTrace.scheduleInfoLog(brief,
            StringUtils.join("运行参数[jdbcurl:",
                jdbcurl,
                ",driverClassName:",
                driverClass,
                ",username:",
                username,
                ",password:",
                password,
                "]"));
        
        try
        {
            ScheduleLogTrace.scheduleInfoLog(brief, StringUtils.join("执行sql为==>", sql));
            jdbc.execute(sql);
            
            ScheduleLogTrace.scheduleInfoLog(brief, "sql执行结束");
            
        }
        catch (Exception e)
        {
            
            ScheduleLogTrace.scheduleWarnLog(brief, ExceptionUtils.getStackTrace(e));
            
            throw e;
            
        }
        finally
        {
            try
            {
                dataSource.close();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * 本地数据库执行sql
     * @param conf
     * @param sql
     * @param brief
     * @see [类、类#方法、类#成员]
     */
    public void executeLocal(Map<String, String> conf, String sql, TaskNodeBrief brief)
    {
        ScheduleLogTrace.scheduleInfoLog(brief, StringUtils.join("执行sql:", sql));
        
        localJdbc.execute(sql);
    }
    
    /**
     * spark查询并返回
     * @param brief
     * @param dsAttr
     * @param nodeRunSql
     * @return
     * @throws Exception
     * @see [类、类#方法、类#成员]
     */
    public String queryFromSpark(TaskNodeBrief brief, DataSourceFlatAttr dsAttr, String nodeRunSql)
        throws Exception
    {
        
        String value = "";
        
        String appId = srm.getSparkContextAppIdJobScope(brief.getJobCode());
        if (StringUtils.isNotEmpty(appId))
        {
            boolean isUnUsed = srm.getSparkCtxUsedJobScope(brief.getJobCode());
            if (isUnUsed)
            {
                //spark查询
                value = client.postCommonTaskForString(
                    ServiceUrlProvider.sparkJobMgrService(ServiceReqClient.SPARK_SELECT_QUERY),
                    client.sqlHiveEntity(nodeRunSql, appId),
                    brief);
                
                srm.setSparkCtxUnusedJobScope(brief.getJobCode());
            }
            else
            {
                value = queryFromHt(brief, dsAttr, nodeRunSql);
            }
        }
        else
        {
            value = queryFromHt(brief, dsAttr, nodeRunSql);
        }
        
        return value;
        
    }
    
    /**
     * 执行ht sql
     * @param brief
     * @param nodeRunSql
     * @return
     * @throws Exception
     * @see [类、类#方法、类#成员]
     */
    public String queryFromHt(TaskNodeBrief brief, DataSourceFlatAttr dsAttr, String nodeRunSql)
        throws Exception
    {
        String retValue = null;
        List<Object[]> vs = client.executeJdbcQuery(nodeRunSql, brief, dsAttr, "VALUE");
        if (CollectionUtils.isNotEmpty(vs))
        {
            Object[] ov = vs.get(0);
            if (ArrayUtils.isNotEmpty(ov))
            {
                if (null == ov[0])
                {
                    throw new IllegalArgumentException("请检查查询赋值语句,格式应为select xx as value");
                }
                retValue = ov[0].toString();
            }
        }
        
        return retValue;
    }
    
    /**
     * 执行ht sql
     * @param brief
     * @param nodeRunSql
     * @see [类、类#方法、类#成员]
     */
    public void executeForHt(TaskNodeBrief brief, DataSourceFlatAttr dsAttr, String nodeRunSql)
        throws Exception
    {
        String url = StringUtils.join("http://", ServiceName.DATA_SYN_AND_HT, "/", "/SparkSQL/executeSql.do");
        
        for (int i = 0; i < RETRY_TIME; i++)
        {
            try
            {
                client.postCommonTask(url, this.jdbcHtParams(nodeRunSql, dsAttr, brief), brief);
            }
            catch (RuntimeException e)
            {
                String errorStack = e.getMessage();
                if (StringUtils.contains(errorStack,
                    "com.chinamobile.cmss.ht.jdbc.JdbcSQLException: Connection is broken")
                    && StringUtils.contains(errorStack, "session closed"))
                {
                    if (2 == i)
                    {
                        throw e;
                    }
                    Thread.sleep(30000);
                    continue;
                }
                else
                {
                    throw e;
                }
            }
            
            break;
            
        }
        
    }
    
    /**
     * 业务返回值sql查询通用方法
     * @param brief
     * @return
     * @throws Exception
     * @see [类、类#方法、类#成员]
     */
    public String queryWithRet(TaskNodeBrief brief)
        throws Exception
    {
        //运行sql
        List<String> nodeRunSqls = configParamUtil.getJobNodeRunSqlParams(brief);
        if (CollectionUtils.isEmpty(nodeRunSqls))
        {
            ScheduleLogTrace.scheduleWarnLog(brief, "没有配置运行sql");
            return null;
        }
        
        if (nodeRunSqls.size() > 1)
        {
            ScheduleLogTrace.scheduleWarnLog(brief, "运行sql数量大于1");
            throw new IllegalArgumentException("运行sql数量大于1");
        }
        
        String nodeRunSql = nodeRunSqls.get(0);
        
        //node运行时系统配置参数
        Map<String, String> nodeSysConf = configParamUtil.getJobNodeSysParams(brief);
        
        DataSourceFlatAttr dsAttr = dataSourceAdapter.getNodeDataSourceConf(brief.getJobCode(), brief.getNodeId());
        
        nodeRunSql = convertSqlUtil.replaceLabelsInNode(nodeRunSql, brief);
        
        nodeRunSql = convertSqlUtil.replaceLabelsInCtx(brief.getJobCode(), nodeRunSql);
        
        jobTasksCache.append(brief.getJobCode(),
            brief.getNodeId(),
            DataConstants.NODE_RUNNING_TRACE,
            StringUtils.join("运行sql:", nodeRunSql));
        
        String retValue = null;
        
        if (null != dsAttr && !this.isHtConnection(dsAttr))
        {
            retValue = this.query(brief, dsAttr, nodeRunSql);
        }
        else
        {
            //RequestConfig reqConf = HttpConfigUtil.getHttpReqConf(brief, nodeSysConf);
            
            try
            {
                boolean isSparkSql = (null == nodeSysConf) ? false : nodeSysConf.containsKey(SPARK_SQL_SELECT_KEY);
                //过滤sql
                if (this.isAbleToSparkQuery(nodeRunSql, isSparkSql))
                {
                    retValue = this.queryFromSpark(brief, dsAttr, nodeRunSql);
                }
                else
                {
                    retValue = this.queryFromHt(brief, dsAttr, nodeRunSql);
                }
            }
            catch (Exception e)
            {
                String excepStr = ExceptionUtils.getStackTrace(e);
                
                ScheduleLogTrace.scheduleWarnLog(brief, StringUtils.join("查询hugetable异常=>", excepStr));
                throw e;
            }
        }
        
        return retValue;
    }
    
    /**
     * jdbc执行ht sql
     * @param sql
     * @return
     * @see [类、类#方法、类#成员]
     */
    /*private Map<String, String> jdbcHtParams(String sql)
    {
        Map<String, String> params = Maps.newHashMap();
        params.put(soaClientUtil.getSql(), sql);
        params.put(soaClientUtil.getDataSource(), SoaClientUtil.DATA_SOURCE_HUGETABLE);
        
        return params;
    }*/
    
    /**
     * 发送到spark集群上执行
     * @param sql
     * @return
     * @see [类、类#方法、类#成员]
     */
    private boolean isAbleToSparkQuery(String sql, boolean isSparkSql)
    {
        
        if (isSparkSql && StringUtils.startsWithIgnoreCase(StringUtils.trim(sql), SQL_SELECT))
        {
            /*if (StringUtils.containsIgnoreCase(sql, "count(") || StringUtils.containsIgnoreCase(sql, "sum(")
                || StringUtils.containsIgnoreCase(sql, "avg(") || StringUtils.containsIgnoreCase(sql, "max(")
                || StringUtils.containsIgnoreCase(sql, "min("))
            {
                return true;
            }*/
            return true;
        }
        
        return false;
        
    }
    
    /**
     * jdbc执行ht sql
     * @param sql
     * @return
     * @see [类、类#方法、类#成员]
     */
    private Map<String, String> jdbcHtParams(String sql, DataSourceFlatAttr dsAttr, TaskNodeBrief brief)
    {
        Map<String, String> params = Maps.newHashMap();
        params.put(RequestKey.SQL, sql);
        params.put(RequestKey.DATA_SOURCE, ServiceReqClient.DATA_SOURCE_HUGETABLE);
        
        if (this.isHtConnection(dsAttr))
        {
            params.put(RequestKey.DATA_BASE_NAME, dsAttr.getConnectAddr());
            params.put(RequestKey.USER_NAME, dsAttr.getUsername());
            params.put(RequestKey.PASSWORD, dsAttr.getPassword());
        }
        
        return params;
    }
    
    /**
     * 驱动匹配
     * @param datasource
     * @return
     * @see [类、类#方法、类#成员]
     */
    public String getDriver(String datasource)
    {
        
        String driv = "";
        
        switch (datasource)
        {
            
            case "oracle":
                driv = "oracle.jdbc.driver.OracleDriver";
                break;
            case "mysql":
                driv = "com.mysql.jdbc.Driver";
                break;
            case "hugetable":
                driv = "com.chinamobile.cmss.ht.Driver";
                break;
            case "gp":
                driv = "org.postgresql.Driver";
                break;
            default:
                driv = datasource;
                break;
        }
        
        return driv;
    }
    
}
