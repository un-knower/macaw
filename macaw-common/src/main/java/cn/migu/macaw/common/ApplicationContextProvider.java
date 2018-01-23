package cn.migu.macaw.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * spring bean获取器
 * @author soy
 */
@Component
public class ApplicationContextProvider implements ApplicationContextAware
{
    /**
     * spring上下文实例
     */
    private static ApplicationContext applicationContext;
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
        throws BeansException
    {
        synchronized (this)
        {
            ApplicationContextProvider.applicationContext = applicationContext;
        }
    }

    /**
     * 通过bean名称获取对象
     * @param name
     * @return
     */
    public static Object getBean(String name)
    {
        return applicationContext.getBean(name);
    }
    
    /**
     * 通过bean类型获取对象
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> clazz)
    {
        return applicationContext.getBean(clazz);
    }
    
    /**
     * 通过bean名称和类型获取对象
     * @param name
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(String name, Class<T> clazz)
    {
        return applicationContext.getBean(name, clazz);
    }
    
}
