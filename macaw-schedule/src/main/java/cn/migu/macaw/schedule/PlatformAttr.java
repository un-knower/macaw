package cn.migu.macaw.schedule;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 平台属性
 * @author soy
 */
@Component
public class PlatformAttr
{
    @Value("${spark.master-url}")
    private String sparkMaster;

    @Value("${hdfs.schema-prefix}")
    private String hdfsPrefixSchema;

    @Value("${hdfs.ha-conf}")
    private String hdfsHaConf;

    @Value("${crossdata.address}")
    private String crossdataIp;

    @Value("${crossdata.port}")
    private String crossdataPort;

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

    public String getHdfsHaConf()
    {
        return hdfsHaConf;
    }

    public void setHdfsHaConf(String hdfsHaConf)
    {
        this.hdfsHaConf = hdfsHaConf;
    }

    public String getCrossdataIp()
    {
        return crossdataIp;
    }

    public void setCrossdataIp(String crossdataIp)
    {
        this.crossdataIp = crossdataIp;
    }

    public String getCrossdataPort()
    {
        return crossdataPort;
    }

    public void setCrossdataPort(String crossdataPort)
    {
        this.crossdataPort = crossdataPort;
    }
}