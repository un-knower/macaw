package cn.migu.macaw.crossdata;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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
@SpringBootApplication
@ComponentScan(PackageInfo.COMPONENT_SCAN_BASE)
public class CrossDataServiceApplication implements CommandLineRunner
{

    @Bean
    public RestTemplate restTemplate()
    {
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setConnectionRequestTimeout(300000);
        httpRequestFactory.setConnectTimeout(300000);
        httpRequestFactory.setReadTimeout(300000);

        return new RestTemplate(httpRequestFactory);
    }

    public static void main(String[] args)
    {
        SpringApplication.run(CrossDataServiceApplication.class, args);
    }

    @Override
    public void run(String... strings)
        throws Exception
    {

    }
}
