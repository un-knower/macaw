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
     * 数据同步服务实例
     */
    String CROSSDATA = "crossdata";

    /**
     * 系统服务jar启动实例
     */
    String JAR_BOOT = "jar-boot";

    /**
     * hugetable sql实例
     */
    String HUGETABLE_SQL = "hugetable-sql";
}
