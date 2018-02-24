package cn.migu.macaw.common;

import cn.migu.macaw.common.log.LogUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * BeanUtilsEx
 *
 * @author soy
 */
public class BeanUtilsEx
{
    public final static void populate(Object bean, Map<String, String> properties)
    {
        properties.entrySet().stream().forEach(e ->{
            try
            {
                MethodUtils.invokeMethod(bean, StringUtils.join("set", WordUtils.capitalize(e.getKey())),e.getValue());
            }
            catch (Exception ex)
            {
                LogUtils.runLogError(ex);
                return;
            }
        });
    }
}
