package cn.migu.macaw.hadoop;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

import cn.migu.macaw.common.PackageInfo;

/**
 * hadoop服务
 * @author soy
 */
//@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan(PackageInfo.COMPONENT_SCAN_BASE)
public class HadoopServiceApplication implements CommandLineRunner
{

    public static void main(String[] args)
    {
        SpringApplication.run(HadoopServiceApplication.class, args);
    }

    @Override
    public void run(String... strings)
        throws Exception
    {

    }
}
