package cn.migu.macaw.schedule;


import cn.migu.macaw.common.PackageInfo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * 任务调度
 * @author soy
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
@MapperScan(basePackages = PackageInfo.SCHEDULE_DAO_BASE)
@ComponentScan(PackageInfo.COMPONENT_SCAN_BASE)
public class ScheduleApplicaiton implements CommandLineRunner
{

    @LoadBalanced
    @Bean(name = "restTemplateForLoadBalance")
    public RestTemplate restTemplateForLoadBalance()
    {
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setConnectionRequestTimeout(86400000);
        httpRequestFactory.setConnectTimeout(86400000);
        httpRequestFactory.setReadTimeout(86400000);

        return new RestTemplate(httpRequestFactory);
    }

    @Bean(name = "restTemplate")
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
        SpringApplication.run(ScheduleApplicaiton.class, args);
    }

    @Override public void run(String... strings)
        throws Exception
    {
        System.out.println("服务启动完成!");
    }
}
