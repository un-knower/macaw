package cn.migu.macaw.schedule.task.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import cn.migu.macaw.schedule.api.model.JobParam;
import cn.migu.macaw.schedule.api.model.NodeParam;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import tk.mybatis.mapper.entity.Example;
import cn.migu.macaw.schedule.dao.JobParamMapper;
import cn.migu.macaw.schedule.dao.NodeParamMapper;
import cn.migu.macaw.schedule.task.TaskNodeBrief;

/**
 * task配置运行时参数
 * 
 * @author  zhaocan
 */
@Component("configParamUtil")
public class ConfigParamUtil
{
    public static final String USERNAME_KEY = "username";
    
    public static final String PASSWD_KEY = "password";
    
    public static final String TOKEN_KEY = "token";
    
    public static final String PAYLOAD_KEY = "messages";
    
    //////////////////////////////////////
    ///task运行参数键值
    public static final String KAFKA_URL = "kafka_url";
    
    public static final String TOPIC = "topic";
    
    public static final String SYS_USERNAME = "sys_username";
    
    public static final String SYS_PASSWD = "sys_passwd";
    
    public static final String SEND_DATASET_SQL = "send_dataset_sql";
    
    public static final String COLUMN_SET = "column_set";
    
    public static final String CONSUMER_OUTPUT_TB = "output_tb";
    
    public static final String CONSUMER_DF_TB = "dataframe_tb";
    
    public static final String NUM_PER_MSG = "send_num";
    
    public static final String BATCH_NO = "batch_no";
    
    public static final String MODEL_CODE = "model_code";
    
    public static final String CPU_CORE_NUM = "core_num";
    
    public static final String MEM_SIZE = "memory_size";
    
    public static final String OS_CMD = "os_cmd";
    
    public static final String SSH_USER = "ssh_user";
    
    public static final String SSH_PASSWD = "ssh_passwd";
    
    public static final String REMOTE_HOST = "remote_host";
    
    ///////////////////////////////////
    //系统参数键值
    public static String SOA_PATH_KEY = "spark_soa_path";
    
    public static String HT_DATABASE_KEY = "ht_database";
    
    public static String HT_IP_KEY = "ht_ip";
    
    public static String HT_PORT_KEY = "ht_port";
    
    public static String HT_USERNAME_KEY = "ht_username";
    
    public static String HT_PASSWORD_KEY = "ht_password";
    
    public static String KMEANS_DATASET = "kmeans_dataset";
    
    public static String KMEANS_RESULT = "kmeans_result";
    
    public static String KMEANS_RESULT_HOST = "kmeans_result_host";
    
    public static String HDFS_HA_CONF_KEY = "hdfs_ha_conf";
    
    //public static String HDFS_URL_KEY = "hdfs_url";
    
    public static String SPARK_PATH_KEY = "spark_path";
    
    public static String SPARK_MGRPORT_KEY = "spark_mgr_port";
    
    //////////////////////////////////
    //sql标签
    public static final String PROVINCES = "\\$\\{province\\}";
    
    public static final String PRODUCTS = "\\$\\{product\\}";
    
    public static final String CHANNELS = "\\$\\{channel\\}";
    
    //系统类型
    public static final String SYS_TYPE = "1";
    
    //自定义SQL标签替换类型
    public static final String SQL_LABEL_TYPE = "2";
    
    //运行sql
    public static final String RUN_SQL_TYPE = "3";
    
    //运行shell命令
    public static final String RUN_SHELL_TYPE = "4";
    
    //存储过程变量
    public static final String PROC_VAR = "5";
    
    //输出redis变量
    public static final String REDIS_OUT_VAR = "6";


    /**
     * job运行时参数
     */
    @Resource
    private JobParamMapper jobParamDao;
    
    /**
     * job-node运行时配置参数
     */
    @Resource
    private NodeParamMapper nodeParamDao;

    
    /**
     * 获取job运行时系统参数
     * @param brief
     * @param kind
     * @return
     * @see [类、类#方法、类#成员]
     */
    public List<JobParam> getJobRunParams(TaskNodeBrief brief, String kind)
    {
        Example example = new Example(JobParam.class);
        example.setOrderByClause("pkey asc");
        example.createCriteria().andEqualTo("jobCode", brief.getJobCode()).andEqualTo("kind", kind);
        
        List<JobParam> params = jobParamDao.selectByExample(example);
        
        return params;
    }
    
    /**
     * 获取job运行时sql语句参数
     * @param brief
     * @return
     * @see [类、类#方法、类#成员]
     */
    public Map<String, String> getJobLabelParams(TaskNodeBrief brief)
    {
        List<JobParam> sqlParams = this.getJobRunParams(brief, SQL_LABEL_TYPE);
        
        Map<String, String> sqls = null;
        
        if (CollectionUtils.isNotEmpty(sqlParams))
        {
            sqls =
                sqlParams.stream()
                    .filter(x -> StringUtils.isNotEmpty(x.getPkey()) && StringUtils.isNotEmpty(x.getValue()))
                    .collect(HashMap::new, (m, jp) -> {
                        m.put(jp.getPkey(), jp.getValue());
                    }, HashMap::putAll);
        }
        
        return sqls;
    }
    
    /**
     * 获取job运行时其他自定义系统参数
     * @param brief
     * @return
     * @see [类、类#方法、类#成员]
     */
    public List<String> getJobRunSysParams(TaskNodeBrief brief)
    {
        List<JobParam> sysParams = this.getJobRunParams(brief, SYS_TYPE);
        
        List<String> sParams = new ArrayList<String>();
        
        for (JobParam jp : sysParams)
        {
            sParams.add(jp.getValue());
        }
        
        return sParams;
    }
    
    /**
     * 获取Job运行时命令脚本参数
     * @param brief
     * @return
     * @see [类、类#方法、类#成员]
     */
    public List<String> getJobRunCmdParams(TaskNodeBrief brief)
    {
        Example example = new Example(JobParam.class);
        example.setOrderByClause("pkey asc");
        example.createCriteria().andLike("pkey", StringUtils.join(OS_CMD, "_%")).andEqualTo("kind", SYS_TYPE);
        
        List<JobParam> params = jobParamDao.selectByExample(example);
        
        List<String> cmds = new ArrayList<String>();
        for (JobParam jp : params)
        {
            cmds.add(jp.getValue());
        }
        
        return cmds;
    }
    
    /**
     * 得到job关联参数
     * @param brief
     * @param kind
     * @return
     * @see [类、类#方法、类#成员]
     */
    public Map<String, String> getJobParams(TaskNodeBrief brief, String kind)
    {
        JobParam ujp = new JobParam();
        ujp.setJobCode(brief.getJobCode());
        ujp.setKind(Integer.valueOf(kind));
        
        List<JobParam> jobParams = jobParamDao.select(ujp);
        
        Map<String, String> params = new HashMap<String, String>();
        for (JobParam _ujp : jobParams)
        {
            String key = _ujp.getPkey();
            
            if (StringUtils.isNotEmpty(key))
            {
                if (StringUtils.length(key) > OS_CMD.length())
                {
                    String cmdStr = StringUtils.substring(key, 0, OS_CMD.length());
                    
                    if (StringUtils.equals(cmdStr, OS_CMD))
                    {
                        continue;
                    }
                }
                params.put(_ujp.getPkey(), _ujp.getValue());
            }
        }
        
        return params;
    }
    
    /**
     * 获取SQL标签参数
     * @param brief
     * @return
     * @see [类、类#方法、类#成员]
     */
    public Map<String, String> getJobSqlLabelParams(TaskNodeBrief brief)
    {
        return this.getJobParams(brief, SQL_LABEL_TYPE);
    }
    
    /**
     * 获取task运行时系统参数
     * @param brief
     * @return
     * @see [类、类#方法、类#成员]
     */
    public Map<String, String> getJobSysParams(TaskNodeBrief brief)
    {
        return this.getJobParams(brief, SYS_TYPE);
    }
    
    // ===========================================================================
    // job-node参数
    // ===========================================================================
    /**
     * 获取job下node的运行时参数
     * @param brief
     * @param kind
     * @return
     * @see [类、类#方法、类#成员]
     */
    public Map<String, String> getJobNodeParams(TaskNodeBrief brief, String kind)
    {
        NodeParam np = new NodeParam();
        np.setJobCode(brief.getJobCode());
        np.setNodeCode(brief.getNodeId());
        np.setKind(Integer.valueOf(kind));
        
        List<NodeParam> nps = nodeParamDao.select(np);
        if (CollectionUtils.isEmpty(nps))
        {
            return null;
        }
        
        Map<String, String> paramMap = nps.stream().collect(HashMap::new, (m, _np) -> {
            m.put(_np.getPkey(), _np.getValue());
        }, HashMap::putAll);
        
        return paramMap;
        
    }
    
    /**
     * 获取节点运行时系统参数
     * @param brief
     * @return
     * @see [类、类#方法、类#成员]
     */
    public Map<String, String> getJobNodeSysParams(TaskNodeBrief brief)
    {
        return this.getJobNodeParams(brief, SYS_TYPE);
    }
    
    /**
     * 获取节点运行时sql标签参数
     * @param brief
     * @return
     * @see [类、类#方法、类#成员]
     */
    public Map<String, String> getJobNodeSqlLabelParams(TaskNodeBrief brief)
    {
        return this.getJobNodeParams(brief, SQL_LABEL_TYPE);
    }
    
    /**
     * 存储过程变量
     * @param brief
     * @return
     * @see [类、类#方法、类#成员]
     */
    public Map<String, String> getJobNodeProcParams(TaskNodeBrief brief)
    {
        return this.getJobNodeParams(brief, PROC_VAR);
    }
    
    /**
     * 输出至redis变量
     * @param brief
     * @return
     * @see [类、类#方法、类#成员]
     */
    public Map<String, String> getJobNodeRedisOutParams(TaskNodeBrief brief)
    {
        return this.getJobNodeParams(brief, REDIS_OUT_VAR);
    }
    
    /**
     * 获取Node运行时命令
     * @param brief
     * @param kind
     * @return
     * @see [类、类#方法、类#成员]
     */
    public List<String> getJobNodeRunParams(TaskNodeBrief brief, String kind)
    {
        Example example = new Example(NodeParam.class);
        example.setOrderByClause("seq asc");
        example.createCriteria()
            .andEqualTo("jobCode", brief.getJobCode())
            .andEqualTo("nodeCode", brief.getNodeId())
            .andEqualTo("kind", kind);
        
        List<NodeParam> params = nodeParamDao.selectByExample(example);
        
        if (CollectionUtils.isEmpty(params))
        {
            return null;
        }
        
        List<String> runCmds =
            params.stream()
                .filter(np -> StringUtils.isNotEmpty(np.getValue()))
                .map(NodeParam::getValue)
                .collect(Collectors.toList());
        
        return runCmds;
    }
    
    /**
     * 获取运行时sql参数
     * @param brief
     * @return
     * @see [类、类#方法、类#成员]
     */
    public List<String> getJobNodeRunSqlParams(TaskNodeBrief brief)
    {
        return this.getJobNodeRunParams(brief, RUN_SQL_TYPE);
    }
    
    /**
     * 获取运行时shell命令参数
     * @param brief
     * @return
     * @see [类、类#方法、类#成员]
     */
    public List<String> getJobNodeRunShellParams(TaskNodeBrief brief)
    {
        return this.getJobNodeRunParams(brief, RUN_SHELL_TYPE);
    }
    
}
