package cn.migu.macaw.sparkdrivermgr;

import cn.migu.macaw.common.ApplicationContextProvider;
import cn.migu.macaw.sparkdrivermgr.cache.SparkJobContext;
import cn.migu.macaw.sparkdrivermgr.manager.DataSheetHandler;
import cn.migu.macaw.sparkdrivermgr.manager.RemoteLaunchJar;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import cn.migu.macaw.common.PackageInfo;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * 任务调度
 * @author soy
 */
//@EnableFeignClients
//@EnableDiscoveryClient
@SpringBootApplication
@MapperScan(basePackages = PackageInfo.SPARK_DRIVER_MGR_DAO_BASE)
@ComponentScan(PackageInfo.COMPONENT_SCAN_BASE)
public class SparkDriverManagerApplicaiton implements CommandLineRunner
{

    @Autowired
    private RemoteLaunchJar remoteJar;

    @Autowired
    private SparkJobContext ctxCache;

    @Autowired
    private Environment env;

    @LoadBalanced
    @Bean(value = "restTemplateForLoadBalance")
    public RestTemplate restTemplateForLoadBalance()
    {
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setConnectionRequestTimeout(1800000);
        httpRequestFactory.setConnectTimeout(1800000);
        httpRequestFactory.setReadTimeout(1800000);

        return new RestTemplate(httpRequestFactory);
    }

    @Bean(value = "restTemplate")
    public RestTemplate restTemplate()
    {
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setConnectionRequestTimeout(1800000);
        httpRequestFactory.setConnectTimeout(1800000);
        httpRequestFactory.setReadTimeout(1800000);

        return new RestTemplate(httpRequestFactory);
    }

    public static void main(String[] args)
    {
        SpringApplication.run(SparkDriverManagerApplicaiton.class, args);
    }

    @Override
    public void run(String... strings)
        throws Exception
    {
        System.out.println("spark driver manager init");
        ctxCache.clear();

        boolean isRestartDrivers = env.getProperty("spark-driver.restart-all",Boolean.class);
        remoteJar.stopAllDrivers(isRestartDrivers);
        remoteJar.startAllDrivers(isRestartDrivers);

    }
}
