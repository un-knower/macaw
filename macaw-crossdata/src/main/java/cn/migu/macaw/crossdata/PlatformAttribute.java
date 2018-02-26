package cn.migu.macaw.crossdata;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 服务配置数据
 * @author soy
 */
@Configuration
public class PlatformAttribute
{
    @Value("${crossdata.default-ftp-path}")
    private String crossdataDefaultFtpDir;

    @Value("${crossdata.job-user-name}")
    private String crossdataJobUserName;

    @Value("${crossdata.job-queuename}")
    private String crossdataJobQueueName;

    public String getCrossdataDefaultFtpDir()
    {
        return crossdataDefaultFtpDir;
    }

    public void setCrossdataDefaultFtpDir(String crossdataDefaultFtpDir)
    {
        this.crossdataDefaultFtpDir = crossdataDefaultFtpDir;
    }

    public String getCrossdataJobUserName()
    {
        return crossdataJobUserName;
    }

    public void setCrossdataJobUserName(String crossdataJobUserName)
    {
        this.crossdataJobUserName = crossdataJobUserName;
    }

    public String getCrossdataJobQueueName()
    {
        return crossdataJobQueueName;
    }

    public void setCrossdataJobQueueName(String crossdataJobQueueName)
    {
        this.crossdataJobQueueName = crossdataJobQueueName;
    }
}
