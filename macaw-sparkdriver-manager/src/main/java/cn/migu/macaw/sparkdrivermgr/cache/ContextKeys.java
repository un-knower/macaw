package cn.migu.macaw.sparkdrivermgr.cache;

/**
 * redis缓存中的系统上下文键定义
 * @author soy
 */
public interface ContextKeys
{
    /**
     * spark job的信息
     */
    String SPARK_JOB_CTX = "spark-job-ctx";
}
