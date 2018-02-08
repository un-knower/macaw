package cn.migu.macaw.jarboot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 服务配置数据
 * @author soy
 */
@Configuration
public class JarBootConfigAttribute
{
    @Value("${spark.master-url}")
    private String sparkMaster;

    @Value("${hdfs.schema-prefix}")
    private String hdfsPrefixSchema;

    @Value("${macaw.custom-etl-jar-name}")
    private String customJarName;

    @Value("${macaw.custom-etl-class-name}")
    private String customJarHandleClassName;

    @Value("${macaw.custom-etl-jar-path}")
    private String customJarLocalPath;

    public String getSparkMaster()
    {
        return sparkMaster;
    }

    public void setSparkMaster(String sparkMaster)
    {
        this.sparkMaster = sparkMaster;
    }

    public String getHdfsPrefixSchema()
    {
        return hdfsPrefixSchema;
    }

    public void setHdfsPrefixSchema(String hdfsPrefixSchema)
    {
        this.hdfsPrefixSchema = hdfsPrefixSchema;
    }

    public String getCustomJarName()
    {
        return customJarName;
    }

    public void setCustomJarName(String customJarName)
    {
        this.customJarName = customJarName;
    }

    public String getCustomJarLocalPath()
    {
        return customJarLocalPath;
    }

    public void setCustomJarLocalPath(String customJarLocalPath)
    {
        this.customJarLocalPath = customJarLocalPath;
    }

    public String getCustomJarHandleClassName()
    {
        return customJarHandleClassName;
    }

    public void setCustomJarHandleClassName(String customJarHandleClassName)
    {
        this.customJarHandleClassName = customJarHandleClassName;
    }
}
