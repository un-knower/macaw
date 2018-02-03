package cn.migu.macaw.common;

/**
 * 服务地址
 *
 * @author soy
 */
public class ServiceUrlProvider implements ServiceName,ServiceModule
{
    /**
     *
     * @param service
     * @return
     */
    public final static String sparkJobMgrService(String service)
    {
        return String.format("http://%s/%s/%s",SPARK_DRIVER_RES_MGR,RESOURCE,service);
    }

    public final static String jobScheduleService(String service)
    {
        return String.format("http://%s/%s/%s",JOB_SCHEDULE,SCHEDULE,service);
    }

    public final static String hadoopService(String service)
    {
        return String.format("http://%s/%s/%s",HADOOP_SERVICE,HADOOP,service);
    }


}
