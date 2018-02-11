package cn.migu.macaw.jarboot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 服务配置数据
 * @author soy
 */
@Configuration
public class JarBootConfigAttribute
{
    @Value("${spark.master-url}")
    private String sparkMaster;

    @Value("${hdfs.schema-prefix}")
    private String hdfsPrefixSchema;

    @Value("${macaw.custom-etl-jar-name}")
    private String customJarName;

    @Value("${macaw.custom-etl-class-name}")
    private String customJarHandleClassName;

    @Value("${macaw.custom-etl-jar-path}")
    private String customJarLocalPath;

    @Value("${macaw.custom-etl-jar-main}")
    private String customJarMainClass;

    @Value("${macaw.sql-query-url}")
    private String sqlQueryUrl;

    @Value("${macaw.sql-execute-url}")
    private String sqlExecuteUrl;

    @Value("${macaw.zookeeper-url}")
    private String zkUrl;

    @Value("${macaw.process-add}")
    private String addProcessUrl;

    @Value("${macaw.data-file-log-update}")
    private String modFileLogUrl;

    @Value("${macaw.hdfs-log-add}")
    private String addHdfsLogUrl;

    @Value("${macaw.batch-sql-execute-url}")
    private String batchSqlExecuteUrl;

    @Value("${macaw.data-collect-roll-interval}")
    private String rollInterval;

    public String getSparkMaster()
    {
        return sparkMaster;
    }

    public void setSparkMaster(String sparkMaster)
    {
        this.sparkMaster = sparkMaster;
    }

    public String getHdfsPrefixSchema()
    {
        return hdfsPrefixSchema;
    }

    public void setHdfsPrefixSchema(String hdfsPrefixSchema)
    {
        this.hdfsPrefixSchema = hdfsPrefixSchema;
    }

    public String getCustomJarName()
    {
        return customJarName;
    }

    public void setCustomJarName(String customJarName)
    {
        this.customJarName = customJarName;
    }

    public String getCustomJarLocalPath()
    {
        return customJarLocalPath;
    }

    public void setCustomJarLocalPath(String customJarLocalPath)
    {
        this.customJarLocalPath = customJarLocalPath;
    }

    public String getCustomJarHandleClassName()
    {
        return customJarHandleClassName;
    }

    public void setCustomJarHandleClassName(String customJarHandleClassName)
    {
        this.customJarHandleClassName = customJarHandleClassName;
    }

    public String getCustomJarMainClass()
    {
        return customJarMainClass;
    }

    public void setCustomJarMainClass(String customJarMainClass)
    {
        this.customJarMainClass = customJarMainClass;
    }

    public String getSqlQueryUrl()
    {
        return sqlQueryUrl;
    }

    public void setSqlQueryUrl(String sqlQueryUrl)
    {
        this.sqlQueryUrl = sqlQueryUrl;
    }

    public String getSqlExecuteUrl()
    {
        return sqlExecuteUrl;
    }

    public void setSqlExecuteUrl(String sqlExecuteUrl)
    {
        this.sqlExecuteUrl = sqlExecuteUrl;
    }

    public String getZkUrl()
    {
        return zkUrl;
    }

    public void setZkUrl(String zkUrl)
    {
        this.zkUrl = zkUrl;
    }

    public String getAddProcessUrl()
    {
        return addProcessUrl;
    }

    public void setAddProcessUrl(String addProcessUrl)
    {
        this.addProcessUrl = addProcessUrl;
    }

    public String getModFileLogUrl()
    {
        return modFileLogUrl;
    }

    public void setModFileLogUrl(String modFileLogUrl)
    {
        this.modFileLogUrl = modFileLogUrl;
    }

    public String getAddHdfsLogUrl()
    {
        return addHdfsLogUrl;
    }

    public void setAddHdfsLogUrl(String addHdfsLogUrl)
    {
        this.addHdfsLogUrl = addHdfsLogUrl;
    }

    public String getBatchSqlExecuteUrl()
    {
        return batchSqlExecuteUrl;
    }

    public void setBatchSqlExecuteUrl(String batchSqlExecuteUrl)
    {
        this.batchSqlExecuteUrl = batchSqlExecuteUrl;
    }

    public String getRollInterval()
    {
        return rollInterval;
    }

    public void setRollInterval(String rollInterval)
    {
        this.rollInterval = rollInterval;
    }
}
