package cn.migu.macaw.jarboot;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

import cn.migu.macaw.common.PackageInfo;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * jar服务管理
 *
 * @author soy
 */
//@EnableDiscoveryClient
@SpringBootApplication
@MapperScan(basePackages = PackageInfo.JAR_BOOT_DAO_BASE)
@ComponentScan(PackageInfo.COMPONENT_SCAN_BASE)
public class JarBootApplication implements CommandLineRunner
{

    public static void main(String[] args)
    {
        SpringApplication.run(JarBootApplication.class, args);
    }

    @Override
    public void run(String... strings)
        throws Exception
    {


    }
}
