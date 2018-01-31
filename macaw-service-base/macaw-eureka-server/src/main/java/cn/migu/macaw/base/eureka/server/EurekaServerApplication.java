package cn.migu.macaw.base.eureka.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * EurekaServer
 * @author soy zhaocancsu@163.com
 */
@EnableEurekaServer
@SpringBootApplication
public class EurekaServerApplication
{
    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class, args);
    }
}
