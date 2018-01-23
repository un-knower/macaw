package cn.migu.macaw.schedule.task.datasource;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import cn.migu.macaw.schedule.PlatformAttr;
import cn.migu.macaw.schedule.api.model.OfflineDataSource;
import cn.migu.macaw.schedule.api.model.NodeParam;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

import cn.migu.macaw.schedule.dao.OfflineDataSourceMapper;
import cn.migu.macaw.schedule.dao.NodeParamMapper;
import cn.migu.macaw.schedule.task.TaskNodeBrief;
import cn.migu.macaw.schedule.cache.JobTasksCache;
import cn.migu.macaw.schedule.util.ScheduleLogTrace;
import cn.migu.macaw.schedule.workflow.DataConstants;
import tk.mybatis.mapper.entity.Example;

/**
 * 数据源适配管理
 * 1.spark master数据源
 * 2.ht jdbc数据源
 * 
 * @author  zhaocan
 * @version  [版本号, 2016年11月2日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
@Component("dataSourceMgr")
public class DataSourceAdapter
{
    private final String SPARK_MASTER_URL = "spark_master_url";
    
    private final String SPARK_HISTORY_SERVER_PORT = "spark_history_server_port";
    
    private final String HDFS_URL = "hdfs_url";
    
    private final String HDFS_HA_CONF_JSON = "hdfs_ha_conf";
    
    private final String CROSSDATA_IP = "crossdata_ip";
    
    private final String CROSSDATA_PORT = "crossdata_port";
    
    private final String SPARK_DS_TYPE = "_datasource_spark";
    
    private final String HUGETABLE_DS_TYPE = "_datasource_hugetable";
    
    private final String ORACLE_DS_TYPE = "_datasource_oracle";
    
    private final String SSH_DS_TYPE = "_datasource_ssh";
    
    private final String FTP_DS_TYPE = "_datasource_ftp";
    
    private final String SFTP_DS_TYPE = "_datasource_sftp";
    
    private final String HDFS_DS_TYPE = "_datasource_hdfs";
    
    private final String GP_DS_TYPE = "_datasource_gp";
    
    private final String MYSQL_DS_TYPE = "_datasource_mysql";
    
    private final String KAFKA_DS_TYPE = "_datasource_kafka";
    
    private final String REDIS_DS_TYPE = "_datasource_redis";
    
    private final String T_HUGETABLE_DS_TYPE = "_datasource_hugetable_t";
    
    private final String T_GP_DS_TYPE = "_datasource_gp_t";
    
    private final String T_MYSQL_DS_TYPE = "_datasource_mysql_t";
    
    private final String T_HDFS_DS_TYPE = "_datasource_hdfs_t";
    
    private final String T_ORACLE_DS_TYPE = "_datasource_oracle_t";
    
    private final String T_FTP_DS_TYPE = "_datasource_ftp_t";
    
    private final String T_SFTP_DS_TYPE = "_datasource_sftp_t";
    
    private final String T_HIVE_DS_TYPE = "_datasource_hive_t";
    
    @Resource
    private OfflineDataSourceMapper dataSourceDao;
    
    @Resource
    private NodeParamMapper nodeParamDao;
    
    @Resource
    private JobTasksCache jobTasksCache;

    @Resource
    private PlatformAttr platformAttr;

    
    /**
     * spark master url
     * @param brief
     * @return
     * @see [类、类#方法、类#成员]
     */
    public String getSparkMasterUrl(TaskNodeBrief brief, DataSourceFlatAttr dsAttr)
    {
        return (null == dsAttr || StringUtils.isEmpty(dsAttr.getSparkMaster()))
            ? platformAttr.getSparkMaster()
            : dsAttr.getSparkMaster();
    }
    

    
    /**
     * hdfs集群地址
     * @param brief
     * @return
     * @see [类、类#方法、类#成员]
     */
    public String getHdfsUrl(TaskNodeBrief brief, DataSourceFlatAttr dsAttr)
    {
        return (null == dsAttr || StringUtils.isEmpty(dsAttr.getHdfsPrefixSchema()))
            ? platformAttr.getHdfsPrefixSchema()
            : dsAttr.getHdfsPrefixSchema();
        
    }
    
    /**
     * crossdata server ip
     * @param brief
     * @return
     * @see [类、类#方法、类#成员]
     */
    public String getCrossdataIp(TaskNodeBrief brief)
    {
        return platformAttr.getCrossdataIp();
    }
    
    /**
     * crossdata server port
     * @param brief
     * @return
     * @see [类、类#方法、类#成员]
     */
    public String getCrossdataPort(TaskNodeBrief brief)
    {
        return platformAttr.getCrossdataPort();
    }
    
    /**
     * hdfs HA配置
     * @param brief
     * @param dsAttr
     * @return
     * @see [类、类#方法、类#成员]
     */
    public String getHdfsHaConf(TaskNodeBrief brief, DataSourceFlatAttr dsAttr)
    {
        return (null == dsAttr || StringUtils.isEmpty(dsAttr.getHdfsHaConfJsonStr()))
            ? platformAttr.getHdfsHaConf()
            : dsAttr.getHdfsHaConfJsonStr();
    }
    
    /**
     * 同步job中的数据源配置信息到redis中
     * @param jobCode
     * @see [类、类#方法、类#成员]
     */
    public void synDataSourceInfoToRedis(String jobCode)
    {
        if (StringUtils.isEmpty(jobCode))
        {
            return;
        }
        
        Example exam = new Example(NodeParam.class);
        exam.createCriteria().andLike("pkey", "_datasource_%").andEqualTo("jobCode", jobCode);
        
        List<NodeParam> nps = nodeParamDao.selectByExample(exam);
        
        if (CollectionUtils.isEmpty(nps))
        {
            return;
        }
        
        OfflineDataSource dsEntity = new OfflineDataSource();
        
        //转换按节点名称分组统计记录
        //数据结构为nodeId-[(dsid1,dstype1),(dsid2,dstype2),...]
        Map<String, Set<Object>> dsSet = nps.stream()
            .filter(np -> StringUtils.isNotEmpty(np.getPkey()) && StringUtils.isNotEmpty(np.getValue()))
            .collect(Collectors.groupingBy(NodeParam::getNodeCode,
                Collectors.mapping(np -> Pair.of(np.getValue(), np.getPkey()), Collectors.toSet())));
        
        ScheduleLogTrace.scheduleInfoLog(jobCode, "*", "*", dsSet.toString());
        
        for (Map.Entry<String, Set<Object>> entry : dsSet.entrySet())
        {
            String nodeCode = entry.getKey();
            
            Set<Object> dsInfo = entry.getValue();
            
            DataSourceFlatAttr dsAttr = new DataSourceFlatAttr();
            
            for (Object ds : dsInfo)
            {
                @SuppressWarnings("unchecked")
                Pair<String, String> idTypePair = (Pair<String, String>)ds;
                
                String dsId = idTypePair.getLeft();
                
                String dsType = idTypePair.getRight();
                
                this.setDataSourceAttr(dsId, dsType, dsEntity, dsAttr);
                
                //默认值
                this.setDefaultValue(dsAttr, dsType, jobCode);
            }
            
            setDsInfoToRedis(jobCode, nodeCode, dsAttr);
            
        }
        
    }
    
    /**
     * 获取节点数据源配置属性
     * @param jobCode
     * @param nodeCode
     * @return
     * @see [类、类#方法、类#成员]
     */
    public DataSourceFlatAttr getNodeDataSourceConf(String jobCode, String nodeCode)
    {
        DataSourceFlatAttr dsAttr = null;
        
        String dsJsonStr = jobTasksCache.get(jobCode, nodeCode, DataConstants.NODE_DATASOURCE_INFO);
        
        dsAttr = JSON.parseObject(dsJsonStr, DataSourceFlatAttr.class);
        
        return dsAttr;
    }
    
    /**
     * 根据主键查询数据源
     * @param dsEntity
     * @return
     * @see [类、类#方法、类#成员]
     */
    private OfflineDataSource getDataSource(OfflineDataSource dsEntity)
    {
        if (StringUtils.isNotEmpty(dsEntity.getObjId()))
        {
            return dataSourceDao.selectOne(dsEntity);
        }
        
        return null;
    }
    
    /**
     * 驱动适配
     * @param type
     * @param dsAttr
     * @see [类、类#方法、类#成员]
     */
    private void setDataSourceType(String type, DataSourceFlatAttr dsAttr)
    {
        switch (type)
        {
            case HUGETABLE_DS_TYPE:
                dsAttr.setDsType("hugetable");
                dsAttr.setDriverClass("com.chinamobile.cmss.ht.Driver");
                break;
            case ORACLE_DS_TYPE:
                dsAttr.setDsType("oracle");
                dsAttr.setDriverClass("oracle.jdbc.driver.OracleDriver");
                break;
            case GP_DS_TYPE:
                dsAttr.setDsType("gp");
                dsAttr.setDriverClass("org.postgresql.Driver");
                break;
            case MYSQL_DS_TYPE:
                dsAttr.setDsType("mysql");
                dsAttr.setDriverClass("com.mysql.jdbc.Driver");
                break;
            case HDFS_DS_TYPE:
                dsAttr.setDsType("hugetable");
                break;
            case FTP_DS_TYPE:
            case SFTP_DS_TYPE:
                dsAttr.setDsType("ftp");
                break;
            case SSH_DS_TYPE:
                dsAttr.setDsType("ssh");
                break;
            case REDIS_DS_TYPE:
                dsAttr.setDsType("redis");
                break;
            case T_HUGETABLE_DS_TYPE:
                dsAttr.setTargetDsType("hugetable");
                dsAttr.setTargetDriverClass("com.chinamobile.cmss.ht.Driver");
                break;
            case T_ORACLE_DS_TYPE:
                dsAttr.setTargetDsType("oracle");
                dsAttr.setTargetDriverClass("oracle.jdbc.driver.OracleDriver");
                break;
            case T_GP_DS_TYPE:
                dsAttr.setTargetDsType("gp");
                dsAttr.setTargetDriverClass("org.postgresql.Driver");
                break;
            case T_MYSQL_DS_TYPE:
                dsAttr.setTargetDsType("mysql");
                dsAttr.setTargetDriverClass("com.mysql.jdbc.Driver");
                break;
            case T_FTP_DS_TYPE:
            case T_SFTP_DS_TYPE:
                dsAttr.setTargetDsType("ftp");
                break;
            case T_HDFS_DS_TYPE:
                dsAttr.setTargetDsType("hugetable");
                break;
            case T_HIVE_DS_TYPE:
                dsAttr.setTargetDsType("hive");
                break;
            default:
                break;
        }
        
    }
    
    /**
     * 数据源属性
     * @param dsId
     * @param dataSourceType
     * @param dsEntity
     * @param dsAttr
     * @see [类、类#方法、类#成员]
     */
    private void setDataSourceAttr(String dsId, String dataSourceType, OfflineDataSource dsEntity,
        DataSourceFlatAttr dsAttr)
    {
        dsEntity.setObjId(dsId.trim());

        OfflineDataSource _ds = this.getDataSource(dsEntity);
        
        switch (dataSourceType)
        {
            case SPARK_DS_TYPE:
                dsAttr.setSparkDs(true);
                dsAttr.setSparkMaster(_ds.getAddress());
                dsAttr.setSparkHistoryServerIp(
                    StringUtils.split(StringUtils.substringAfter(_ds.getAddress(), "spark://"), ":")[0]);
                dsAttr.setSparkHistoryServerPort(_ds.getPort().toString());
                if (StringUtils.isEmpty(dsAttr.getHdfsPrefixSchema()) && StringUtils.isNotEmpty(_ds.getReserve4()))
                {
                    dsAttr.setHdfsPrefixSchema(_ds.getReserve4());
                }
                
                break;
            case HDFS_DS_TYPE:
                dsAttr.setHdfsPrefixSchema(_ds.getAddress());
                dsAttr.setHdfsHaConfJsonStr(_ds.getReserve1());
                break;
            case HUGETABLE_DS_TYPE:
            case ORACLE_DS_TYPE:
            case GP_DS_TYPE:
            case MYSQL_DS_TYPE:
            case FTP_DS_TYPE:
            case SFTP_DS_TYPE:
                dsAttr.setConnectAddr(_ds.getAddress());
                dsAttr.setUsername(_ds.getUsername());
                dsAttr.setPassword(_ds.getPassword());
                break;
            case SSH_DS_TYPE:
                dsAttr.setSshHost(_ds.getAddress());
                dsAttr.setSshUser(_ds.getUsername());
                dsAttr.setSshPassword(_ds.getPassword());
                break;
            case KAFKA_DS_TYPE:
                dsAttr.setKafkaHttpUrl(_ds.getAddress());
                dsAttr.setKafkaZkUrl(_ds.getReserve2());
                dsAttr.setUsername(_ds.getUsername());
                dsAttr.setPassword(_ds.getPassword());
                dsAttr.setKafkaBrokeUrl(_ds.getReserve3());
                break;
            case REDIS_DS_TYPE:
                dsAttr.setRedisHost(_ds.getAddress());
                dsAttr.setRedisPasswd(_ds.getPassword());
                dsAttr.setPort(_ds.getPort().intValue());
                dsAttr.setDbIndex(Integer.valueOf(_ds.getReserve1()));
                break;
            case T_HUGETABLE_DS_TYPE:
            case T_ORACLE_DS_TYPE:
            case T_GP_DS_TYPE:
            case T_MYSQL_DS_TYPE:
            case T_FTP_DS_TYPE:
            case T_SFTP_DS_TYPE:
                dsAttr.setTargetconnectAddr((_ds.getAddress()));
                dsAttr.setTargetUsername(_ds.getUsername());
                dsAttr.setTargetPassword(_ds.getPassword());
                break;
            case T_HDFS_DS_TYPE:
                dsAttr.setTargetHdfsPrefixSchema(_ds.getAddress());
                break;
            case T_HIVE_DS_TYPE:
                dsAttr.setTargetconnectAddr(_ds.getAddress());
                break;
            default:
                break;
        }
        
        this.setDataSourceType(dataSourceType, dsAttr);
        
    }
    
    /**
     * 设置默认值
     * @param dsAttr
     * @param dsType
     * @param jobCode
     * @see [类、类#方法、类#成员]
     */
    private void setDefaultValue(DataSourceFlatAttr dsAttr, String dsType, String jobCode)
    {
        if (StringUtils.equals(dsType, SPARK_DS_TYPE))
        {
            if (StringUtils.isEmpty(dsAttr.getSparkMaster()))
            {
                dsAttr.setSparkMaster(platformAttr.getSparkMaster());
            }

            if (StringUtils.isEmpty(dsAttr.getHdfsPrefixSchema()))
            {
                dsAttr.setHdfsPrefixSchema(platformAttr.getHdfsPrefixSchema());
            }
            
        }
        
        if (StringUtils.equals(dsType, HDFS_DS_TYPE))
        {
            if (StringUtils.isEmpty(dsAttr.getHdfsPrefixSchema()))
            {
                dsAttr.setHdfsPrefixSchema(platformAttr.getHdfsPrefixSchema());
            }
            
            if (StringUtils.isEmpty(dsAttr.getHdfsHaConfJsonStr()))
            {
                dsAttr.setHdfsHaConfJsonStr(platformAttr.getHdfsHaConf());
            }
        }
        
    }
    
    /**
     * 
     * 写节点数据源信息至redis
     * @param jobCode
     * @param nodeCode
     * @param dsAttr
     * @see [类、类#方法、类#成员]
     */
    private void setDsInfoToRedis(String jobCode, String nodeCode, DataSourceFlatAttr dsAttr)
    {
        jobTasksCache.put(jobCode, nodeCode, DataConstants.NODE_DATASOURCE_INFO, JSON.toJSONString(dsAttr));
    }

}
