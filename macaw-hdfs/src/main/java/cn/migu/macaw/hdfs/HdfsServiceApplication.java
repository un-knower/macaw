package cn.migu.macaw.hdfs;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import cn.migu.macaw.common.PackageInfo;

/**
 * 数据同步
 * @author soy
 */
//@EnableDiscoveryClient
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class,HibernateJpaAutoConfiguration.class})
@ComponentScan(PackageInfo.COMPONENT_SCAN_BASE)
public class HdfsServiceApplication implements CommandLineRunner
{

    public static void main(String[] args)
    {
        SpringApplication.run(HdfsServiceApplication.class, args);
    }

    @Override
    public void run(String... strings)
        throws Exception
    {

    }
}
