package cn.migu.macaw.hadoop;

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

    public String getCrossdataDefaultFtpDir()
    {
        return crossdataDefaultFtpDir;
    }

    public void setCrossdataDefaultFtpDir(String crossdataDefaultFtpDir)
    {
        this.crossdataDefaultFtpDir = crossdataDefaultFtpDir;
    }
}
