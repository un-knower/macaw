package cn.migu.macaw.crossdata.service.impl;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.chinamobile.cmss.crossdata.client.CrossDataClient;
import com.chinamobile.cmss.crossdata.config.Column;
import com.chinamobile.cmss.crossdata.config.FileInfo;
import com.chinamobile.cmss.crossdata.config.JobConfig;
import com.chinamobile.cmss.crossdata.ipc.JobInfo;
import com.chinamobile.cmss.crossdata.ipc.JobStatus;

import cn.migu.common.redis.StringRedisService;
import cn.migu.macaw.common.*;
import cn.migu.macaw.common.log.InterfaceLogBean;
import cn.migu.macaw.common.log.LogUtils;
import cn.migu.macaw.common.log.ReqRespLog;
import cn.migu.macaw.common.message.CrossDataResult;
import cn.migu.macaw.common.message.Entity;
import cn.migu.macaw.crossdata.PlatformAttribute;
import cn.migu.macaw.crossdata.common.DataSourceType;
import cn.migu.macaw.crossdata.common.DataSynMode;
import cn.migu.macaw.crossdata.common.DbTool;
import cn.migu.macaw.crossdata.model.CrossDataParam;
import cn.migu.macaw.crossdata.service.IDataSyncService;

/**
 * 数据同步服务实现
 *
 * @author soy
 */
@Service
public class DataSyncServiceImpl implements IDataSyncService
{
    private static final Log crossDataLog = LogFactory.getLog("crossdata");

    @Autowired
    private DbTool dbTool;
    
    @Autowired
    private PlatformAttribute platformAttri;

    @Autowired
    private ReqRespLog reqRespLog;

    @Autowired
    private Environment env;

    @Autowired
    private StringRedisService redisService;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public ReturnCode dataSyncStart(HttpServletRequest request,Entity entity,InterfaceLogBean logBean)
        throws Exception
    {
        CrossDataParam params = parseDataSyncParam(request);
        if (null == params)
        {
            return ReturnCode.DATA_SYNC_PARAM_PARSE_ERROR;
        }

        reqRespLog.otherLog(crossDataLog,logBean,params.toString());
        
        //特殊同步功能,暂不支持
        if (StringUtils.isNotBlank(params.getTransferType()))
        {
            return ReturnCode.DATA_SYNC_FUNC_NOT_SUPPORT;
        }
        
        CrossDataClient client = createCrossDataClient();
        if (null == client)
        {
            return ReturnCode.DATA_SYNC_CLIENT_CREATE_FAILED;
        }
        
        //源数据类型
        DataSourceType sData = params.getDataSource();
        //目标数据类型
        DataSourceType tData = params.getT_dataSource();
        
        JobConfig config = null;
        
        switch (sData)
        {
            case HIVE:
                switch (tData)
                {
                    case ORACLE:
                    case MYSQL:
                        config = genJobConfigForHiveToDb(params);
                        break;
                    case FTP:
                        config = genJobConfigForHdfsToFtp(params);
                        break;
                    default:
                        break;
                }
                break;
            case FTP:
                switch (tData)
                {
                    case HIVE:
                        config = genJobConfigForFtpToHdfs(params);
                        break;
                    default:
                        config = genJobConfigForFtpToHive(params);
                        break;
                }
                break;
            default:
                if (StringUtils.isBlank(params.getTableName()))
                {
                    config = genJobConfigForSqlToHdfs(params);
                }
                else if (DataSourceType.HIVE == tData)
                {
                    config = genJobConfigForDbToHive(params);
                }
                else
                {
                    config = genJobConfigForDbToDb(params);
                }
                break;
        }
        
        if (null != config)
        {
            Map<String, String> baseConf = Maps.newHashMap();
            baseConf.put("crossdata.job.user.name", platformAttri.getCrossdataJobUserName());
            baseConf.put("mapreduce.job.queuename", platformAttri.getCrossdataJobQueueName());
            
            String jobId = client.submit(config, baseConf);
            reqRespLog.otherLog(crossDataLog,logBean,String.format("jobCode=%s,nodeCode=%s,crossDatajobId=%s",params.getJobCode(),params.getNodeId(),jobId));
            crossClientClientAddress(jobId);
            try
            {
                while (true)
                {
                    JobInfo info = client.getJobInfo(jobId);
                    JobStatus status = info.getStatus();
                    if (status == JobStatus.SUCCESS || status == JobStatus.FAILED || status == JobStatus.KILLED)
                    {
                        entity.setAppid(jobId);

                        if (JobStatus.SUCCESS == status)
                        {
                            String format = "yyyy-MM-dd HH:mm:ss";
                            CrossDataResult rst = new CrossDataResult(DateUtil.tsToStr(info.getJobStartTime(),format),
                                DateUtil.tsToStr(info.getJobEndTime(),format),jobId,String.valueOf(status.getValue()),
                                info.getInputRecordNum(),info.outputRecordNum);
                            entity.setContent(JSON.toJSONString(rst));
                            if (StringUtils.isNotBlank(params.getLastSql()))
                            {
                                dbTool.execute(params.getT_dataSource().getDriverClass(),
                                    params.getT_connectUrl(),
                                    params.getT_auth()[0],
                                    params.getT_auth()[1],
                                    params.getLastSql().trim());
                            }
                        }
                        else
                        {
                            entity.setErrorStack(String.format("error_code=%s,error_info=%s",info.getErrorCode(),info.getErrorInfo()));
                            if(JobStatus.FAILED == status)
                            {
                                return ReturnCode.DATA_SYNC_JOB_FAILED;
                            }
                            else
                            {
                                return ReturnCode.DATA_SYNC_JOB_KILLED;
                            }
                        }
                        break;
                    }
                }
            }
            finally
            {
                removeCrossDataCache(jobId);
                client.close();
            }
        }
        else
        {
            return ReturnCode.DATA_SYNC_JOB_CONFIG_CREATE_FAILED;
        }
        
        return ReturnCode.SUCCESS;
        
    }

    @Override
    public ReturnCode dataSyncStop(String jobId)
    {
        String redirectAddress = getCrossDataJobClientAddress(jobId);
        if(StringUtils.equals(NetUtils.LOCAL_FLAG,redirectAddress))
        {
            CrossDataClient client = createCrossDataClient();
            try
            {
                client.kill(jobId);
                client.close();
            }
            catch (Exception e)
            {
                LogUtils.runLogError(e);

                return ReturnCode.DATA_SYNC_JOB_KILL_FAILED;
            }
        }
        else
        {
            Map<String,String> params = Maps.newHashMap();
            params.put(RequestKey.CROSS_DATA_JOB_ID,jobId);
            RestTemplateProvider.postFormForEntity(restTemplate,StringUtils.join(redirectAddress,"/crossdata-stop"),String.class,params);
        }



        return ReturnCode.SUCCESS;
    }

    /**
     * 根据任务id删除数据同步缓存
     * @param jobId
     */
    private void removeCrossDataCache(String jobId)
    {
        if(StringUtils.isNotBlank(jobId))
        {
            redisService.remove(jobId);
        }
    }

    /**
     * 获取数据同步提交客户端地址端口
     * @param jobId 任务id
     * @return String - 地址端口
     */
    public String getCrossDataJobClientAddress(String jobId)
    {
        String address = redisService.get(jobId);
        if(StringUtils.isNotEmpty(address))
        {
            try
            {
                String ipPort = makeIpPort();

                if(StringUtils.endsWith(address,ipPort))
                {
                    return NetUtils.LOCAL_FLAG;
                }

                return address;
            }
            catch (UnknownHostException e)
            {
                LogUtils.runLogError(e);

            }
        }
        return null;
    }

    /**
     * 保存当前job所在的服务器地址
     * @param jobId
     */
    private void crossClientClientAddress(String jobId)
    {
        try
        {
            String ipPort = makeIpPort();
            redisService.set(jobId,StringUtils.join("http://",ipPort));
        }
        catch (Exception e)
        {
            LogUtils.runLogError(e);
        }
    }

    /**
     * 构造地址端口
     * @return String - 地址端口
     * @throws UnknownHostException
     */
    private String makeIpPort()
        throws UnknownHostException
    {
        int port = env.getProperty("server.port",Integer.class);
        String ipPort = NetUtils.ipAddressAndPortToUrlString(InetAddress.getLocalHost(),port);

        return ipPort;
    }

    /**
     * 关系数据库数据同步到关系数据库配置
     * @param params 请求参数
     * @return JobConfig - hadoop job配置对象
     */
    private JobConfig genJobConfigForDbToDb(CrossDataParam params)
    {
        JobConfig config = null;
        
        Connection sConn = dbTool.createConnection(params.getDataSource().getDriverClass(),
            params.getConnectUrl(),
            params.getAuth()[0],
            params.getAuth()[1]);
        Connection tConn = dbTool.createConnection(params.getT_dataSource().getDriverClass(),
            params.getT_connectUrl(),
            params.getT_auth()[0],
            params.getT_auth()[1]);
        
        try
        {
            config = new JobConfig(JobConfig.ResourceType.DB, JobConfig.ResourceType.DB,
                dbTool.getColumnInfo(sConn, params.getTableName()),
                dbTool.getColumnInfo(tConn, params.getT_tableName()));
            //配置Input/output参数
            config.getDbInput()
                .setConnectUrl(params.getConnectUrl())
                .setPassword(params.getAuth()[1])
                .setUsername(params.getAuth()[0])
                .setDriver(params.getDataSource().getDriverClass())
                .setMapNum(params.getMapNum());
            
            //有筛选条件
            if (StringUtils.isNotBlank(params.getWhereSql()))
            {
                config.getDbInput().setWhere(params.getWhereSql().trim());
            }
            //目标数据源是oracle 则表名大写 其余情况小写
            if (DataSourceType.ORACLE == params.getDataSource())
            {
                config.getDbInput().setTableName(params.getTableName().toUpperCase());
            }
            else
            {
                config.getDbInput().setTableName(params.getTableName().toLowerCase());
            }
            
            if (StringUtils.isNotBlank(params.getPartition()))
            {
                config.getDbInput().setDbPartition(params.getPartition());
            }
            
            //目标数据
            config.getDbOutput()
                .setConnectUrl(params.getT_connectUrl())
                .setPassword(params.getT_auth()[1])
                .setUsername(params.getT_auth()[0])
                .setDriver(params.getT_dataSource().getDriverClass())
                .setRecordsPerStatement(params.getRecordPerStatement());

            //目标数据源是oracle 则表名大写 其余情况小写
            if (DataSourceType.ORACLE == params.getT_dataSource())
            {
                config.getDbOutput().setTableName(params.getT_tableName().toUpperCase());
            }
            else
            {
                config.getDbOutput().setTableName(params.getT_tableName().toLowerCase());
            }
            if (StringUtils.isNotBlank(params.getT_partition()))
            {
                config.getDbOutput().setDbPartition(params.getT_partition());
            }
            
            if (DataSynMode.OVERWRITE == params.getWriteType())
            {
                
                if (DataSourceType.ORACLE == params.getT_dataSource()
                    || DataSourceType.MYSQL == params.getT_dataSource())
                {
                    dbTool.truncate(tConn, params.getT_tableName(), params.getT_partition());
                }
                
                config.getDbOutput().setIsOverwrite(true);
            }
            else if (DataSynMode.APPEND == params.getWriteType())
            {
                config.getDbOutput().setIsOverwrite(false);
            }
        }
        finally
        {
            dbTool.close(sConn);
            dbTool.close(tConn);
        }
        
        return config;
    }
    
    /**
     * 关系数据库数据同步到HIVE配置
     * @param params 请求参数
     * @return JobConfig - hadoop job配置对象
     */
    private JobConfig genJobConfigForDbToHive(CrossDataParam params)
    {
        //其他情况则为 DB-》HT
        JobConfig config = null;
        Connection sConn = dbTool.createConnection(params.getDataSource().getDriverClass(),
            params.getConnectUrl(),
            params.getAuth()[0],
            params.getAuth()[1]);
        Connection tConn = dbTool.createConnection(params.getT_dataSource().getDriverClass(),
            params.getT_connectUrl(),
            params.getT_auth()[0],
            params.getT_auth()[1]);
        
        try
        {
            config = new JobConfig(JobConfig.ResourceType.DB, JobConfig.ResourceType.HIVE,
                dbTool.getColumnInfo(sConn, params.getTableName()),
                dbTool.getColumnInfo(tConn, params.getT_tableName()));
            
            if (StringUtils.isNotBlank(params.getWhereSql()))
            {
                config.getDbInput().setWhere(params.getWhereSql().trim());
            }
            
            if (StringUtils.isNotBlank(params.getPartition()))
            {
                config.getDbInput().setDbPartition(params.getPartition());
            }
            
            //配置Input/output参数
            config.getDbInput()
                .setConnectUrl(params.getConnectUrl())
                .setPassword(params.getAuth()[1])
                .setUsername(params.getAuth()[0])
                .setMapNum(params.getMapNum());

            if (DataSourceType.ORACLE == params.getDataSource())
            {
                config.getDbInput().setTableName(params.getTableName().trim().toUpperCase());
            }
            else
            {
                config.getDbInput().setTableName(params.getTableName().toLowerCase());
            }
            
            config.getHiveOutput()
                .setHiveServer2Url(params.getT_connectUrl())
                .setConnectionUserName(params.getT_auth()[0])
                .setConnectionUserPassword(params.getT_auth()[1])
                .setDatabaseName(params.getT_databaseName())
                .setTableName(params.getT_tableName());
            
            if (DataSynMode.OVERWRITE == params.getWriteType())
            {
                config.getHiveOutput().setOverwrite(true);
            }
            else if (DataSynMode.APPEND == params.getWriteType())
            {
                config.getHiveOutput().setOverwrite(false);
            }
        }
        finally
        {
            dbTool.close(sConn);
            dbTool.close(tConn);
        }
        
        return config;
    }
    
    /**
     * sql加载数据同步到hdfs配置
     * @param params 请求参数
     * @return JobConfig - hadoop job配置对象
     */
    private JobConfig genJobConfigForSqlToHdfs(CrossDataParam params)
    {
        JobConfig config = new JobConfig(JobConfig.ResourceType.SQL, JobConfig.ResourceType.FILE,
            Lists.<Column> newArrayList(), Lists.<Column> newArrayList());
        
        //配置Intput参数
        config.getSqlInput()
            .setConnectUrl(params.getConnectUrl())
            .setPassword(params.getAuth()[1])
            .setUsername(params.getAuth()[0])
            .setSql(params.getSql())
            .setMapNum(params.getMapNum());
        
        //配置Output参数
        config.getFileOutput()
            .setFilesystem(FileInfo.FSType.HDFS)
            .setSerDeType(FileInfo.SerDeType.DELIMITED)
            .setFieldDelimiter(params.getFieldDelimiter())
            .setMakeDir(true)
            .setLocation(platformAttri.getCrossdataDefaultFtpDir());
        
        if (DataSynMode.OVERWRITE == params.getWriteType())
        {
            params.setLastSql(StringUtils.join("LOAD DATA INPATH '",
                platformAttri.getCrossdataDefaultFtpDir(),
                "' OVERWRITE  INTO TABLE ",
                params.getT_databaseName(),
                ".",
                params.getT_tableName()));
        }
        else if (DataSynMode.APPEND == params.getWriteType())
        {
            params.setLastSql(StringUtils.join("LOAD DATA INPATH '",
                platformAttri.getCrossdataDefaultFtpDir(),
                "'   INTO TABLE ",
                params.getT_databaseName(),
                ".",
                params.getT_tableName()));
        }
        
        return config;
    }
    
    /**
     * Hive表同步到关系型数据库配置
     * @param params 请求参数
     * @return JobConfig - hadoop job配置对象
     */
    private JobConfig genJobConfigForHiveToDb(CrossDataParam params)
    {
        JobConfig config = null;
        Connection sConn = dbTool.createConnection(params.getDataSource().getDriverClass(),
            params.getConnectUrl(),
            params.getAuth()[0],
            params.getAuth()[1]);
        Connection tConn = dbTool.createConnection(params.getT_dataSource().getDriverClass(),
            params.getT_connectUrl(),
            params.getT_auth()[0],
            params.getT_auth()[1]);
        try
        {
            config = new JobConfig(JobConfig.ResourceType.HIVE, JobConfig.ResourceType.DB,
                dbTool.getColumnInfo(sConn, params.getTableName()),
                dbTool.getColumnInfo(tConn, params.getT_tableName()));
            config.getHiveInput()
                .setMapNum(params.getMapNum())
                .setHiveServer2Url(params.getConnectUrl())
                .setConnectionUserPassword(params.getAuth()[1])
                .setConnectionUserName(params.getAuth()[0])
                .setDatabaseName(params.getDatabaseName())
                .setTableName(params.getTableName().toLowerCase());
            
            if (StringUtils.isNotBlank(params.getWhereSql()))
            {
                config.getHiveInput().setWhere(params.getWhereSql().trim());
            }
            config.getDbOutput()
                .setConnectUrl(params.getT_connectUrl())
                .setTableName(params.getT_tableName())
                .setPassword(params.getT_auth()[1])
                .setUsername(params.getT_auth()[0])
                .setRecordsPerStatement(params.getRecordPerStatement());
            
            if (DataSourceType.ORACLE == params.getDataSource())
            {
                config.getDbOutput().setTableName(params.getTableName().trim().toUpperCase());
            }
            else
            {
                config.getDbOutput().setTableName(params.getTableName().toLowerCase());
            }
            
            if (DataSynMode.OVERWRITE == params.getWriteType())
            {
                if (DataSourceType.ORACLE == params.getT_dataSource()
                    || DataSourceType.MYSQL == params.getT_dataSource())
                {
                    dbTool.truncate(tConn, params.getT_tableName(), params.getT_partition());
                }
                config.getDbOutput().setIsOverwrite(true);
            }
            else if (DataSynMode.APPEND == params.getWriteType())
            {
                config.getDbOutput().setIsOverwrite(false);
            }
            
            if (StringUtils.isNotBlank(params.getT_partition()))
            {
                config.getDbOutput().setDbPartition(params.getT_partition());
            }
        }
        finally
        {
            dbTool.close(sConn);
            dbTool.close(tConn);
        }
        
        return config;
    }
    
    /**
     * ftp文件同步到hive表配置
     * @param params 请求参数
     * @return JobConfig - hadoop job配置对象
     */
    private JobConfig genJobConfigForFtpToHive(CrossDataParam params)
    {
        //初始化JobConfig，四个参数是源数据源类型（有file和db两种），目的数据源类型（有file和db两种），输入列信息，输出列信息
        JobConfig config = new JobConfig(JobConfig.ResourceType.FILE, JobConfig.ResourceType.HIVE, null, null);
        
        String extName = StringUtils.substringAfter(params.getFileName(), ".");
        
        //配置Input参数（有FileInput和DbInput）
        config.getFileInput()
            .setFilesystem(FileInfo.FSType.FTP)
            .setCompression(false)
            .setLocation(params.getLocationFtp())
            .setFieldDelimiter(params.getDelimiterFtp())
            .setFilePattern(params.getFileName())
            .setEmptyWarn(true)
            .setRecursive(true)
            .setDelSourceFile(false);
        
        if (StringUtils.equalsIgnoreCase(extName, "csv"))
        {
            config.getFileInput().setSerDeType(FileInfo.SerDeType.CSV);
        }
        else
        {
            config.getFileInput().setSerDeType(FileInfo.SerDeType.DELIMITED);
        }
        
        //配置Output参数（有FileOutput和DbOutput）
        config.getHiveOutput()
            .setOverwrite(true)
            .setHiveServer2Url(params.getHiveServer2Url())
            .setDatabaseName(params.getDatabaseName())
            .setTableName(params.getTableName());
        
        return config;
    }
    
    /**
     * ftp文件同步到hugetable中的hdfs文件配置
     * @param params 请求参数
     * @return JobConfig - hadoop job配置对象
     */
    private JobConfig genJobConfigForFtpToHdfs(CrossDataParam params)
    {
        JobConfig config = new JobConfig(JobConfig.ResourceType.FILE, JobConfig.ResourceType.FILE, null, null);
        
        config.getFileInput()
            .setFilesystem(FileInfo.FSType.SFTP)
            .setCompression(false)
            //sftp路径,sftp://testftp:testftp@192.168.129.150/apps/testftp
            .setLocation(params.getLocationFtp())
            .setSerDeType(FileInfo.SerDeType.DELIMITED)
            //分隔符
            .setFieldDelimiter(params.getDelimiterFtp())
            //匹配文件名
            .setFilePattern(params.getFileName())
            //没有文件会告警
            .setEmptyWarn(true)
            //迭代子目录文件
            .setRecursive(true)
            //删除ftp源文件
            .setDelSourceFile(true)
            //acsii传输方式
            .setTransmissionType("ascii");
        
        //配置Output参数（有FileOutput和DbOutput）
        config.getFileOutput()
            .setFilesystem(FileInfo.FSType.HDFS)
            .setCompression(false)
            .setSerDeType(FileInfo.SerDeType.DELIMITED)
            //hdfs文件系统分隔符
            .setFieldDelimiter(params.getDelimiterHdfs())
            //指定的hdfs目录不存在则自动创建
            .setMakeDir(true)
            .setLocation(params.getLocationHdfs());
        
        return config;
    }
    
    /**
     * hugetable中的hdfs文件同步到ftp文件配置
     * @param params 请求参数
     * @return JobConfig - hadoop job配置对象
     */
    private JobConfig genJobConfigForHdfsToFtp(CrossDataParam params)
    {
        //定义输入和输出列信息
        List<Column> inputColumns = Lists.newArrayList();
        List<Column> outputColumns = Lists.newArrayList();
        
        inputColumns.add(new Column("1x", "int"));
        outputColumns.add(new Column("1x", "int"));
        
        JobConfig config =
            new JobConfig(JobConfig.ResourceType.FILE, JobConfig.ResourceType.FILE, inputColumns, outputColumns);
        
        //ht2ftp 是hdfs文件与ftp文件之间的同步
        config.getFileInput()
            .setFilesystem(FileInfo.FSType.HDFS)
            .setCompression(false)
            .setSerDeType(FileInfo.SerDeType.DELIMITED)
            //字段分隔符
            .setFieldDelimiter(params.getDelimiterHdfs())
            //遍历
            .setRecursive(true)
            //acsii方式全量数据拷贝
            .setTransmissionType("acsii")
            //hive 表在hdfs的路径,如hdfs://hadoop/apps/hugetable/warehouse/hbase_test.db/qcc_test_hbase
            .setLocation(params.getLocationHdfs());
        
        //配置Output参数（有FileOutput和DbOutput）
        config.getFileOutput()
            .setFilesystem(FileInfo.FSType.SFTP)
            .setCompression(false)
            //路径,sftp://testftp:testftp@192.168.129.150/apps/testftp
            .setLocation(params.getLocationFtp())
            .setSerDeType(FileInfo.SerDeType.DELIMITED)
            //文件名匹配表达式,在hdfs上生成的文件名以ht_hive-开头，如ht_hive-000,[例子ht_hive2-%id%]
            .setFileNamePattern(params.getFileName())
            .setFileNameIdLength(3)
            //字段分隔符
            .setFieldDelimiter(params.getDelimiterFtp());
        
        return config;
    }
    
    /**
     * 创建数据同步客户端
     * @return CrossDataClient - 客户端对象
     */
    private CrossDataClient createCrossDataClient()
    {
        CrossDataClient client = null;
        try
        {
            client = new CrossDataClient(null, 0);
        }
        catch (Exception e)
        {
            LogUtils.runLogError(e);
        }
        
        return client;
    }
    
    /**
     * 解析数据同步参数
     * @param request http请求对象
     * @return CrossDataParam - 数据同步参数
     */
    private CrossDataParam parseDataSyncParam(HttpServletRequest request)
    {
        try
        {
            CrossDataParam bean = new CrossDataParam();
            List<String> keys = BeanUtils.describe(bean)
                .entrySet()
                .stream()
                .map(e -> e.getKey())
                .filter(k -> !StringUtils.equals("class", k))
                .collect(Collectors.toList());
            Map<String, String> paramMap = request.getParameterMap()
                .entrySet()
                .stream()
                .filter(e -> StringUtils.isNotBlank(e.getKey()) && keys.contains(e.getKey()))
                .filter(e -> null != e.getValue() && e.getValue().length > 0)
                .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()[0]));

            BeanUtilsEx.populate(bean, paramMap);

            
            return bean;
        }
        catch (Exception e)
        {
            LogUtils.runLogError(e);
        }
        
        return null;
        
    }
}
