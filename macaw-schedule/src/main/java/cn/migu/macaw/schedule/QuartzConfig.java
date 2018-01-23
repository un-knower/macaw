package cn.migu.macaw.schedule;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.google.common.collect.Maps;

import cn.migu.macaw.schedule.service.IJobTasksService;

/**
 * quartz配置
 * @author soy
 */
@Configuration
public class QuartzConfig
{
    @Autowired
    private IJobTasksService jobTasksService;
    
    @Autowired
    private ApplicationContext applicationContext;
    
    @Bean
    public Properties quartzProperties()
        throws IOException
    {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
        propertiesFactoryBean.afterPropertiesSet();
        return propertiesFactoryBean.getObject();
    }
    
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean()
        throws Exception
    {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        
        DataSource dataSource = applicationContext.getBean(DataSource.class);
        
        factory.setDataSource(dataSource);
        factory.setApplicationContextSchedulerContextKey("applicationContextKey");
        
        factory.setQuartzProperties(quartzProperties());
        
        Map<String, Object> ctxMap = Maps.newHashMap();
        ctxMap.put("jobTasksService", jobTasksService);
        
        factory.setSchedulerContextAsMap(ctxMap);
        
        return factory;
    }
}
