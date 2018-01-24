package cn.migu.macaw.common;

/**
 * 服务名称
 * 注意:服务名称中不能有下划线
 * @author soy
 */
public interface ServiceName
{
    /**
     * 任务调度服务实例
     */
    String JOB_SCHEDULE = "job-schedule";

    /**
     * spark driver管理器实例
     */
    String SPARK_DRIVER_RES_MGR = "spark-driver-manager";

    /**
     * CROSSDATA-HUGETABLE服务实例
     */
    String DATA_SYN_AND_HT = "data-syn-ht";
}
