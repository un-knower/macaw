package cn.migu.macaw.schedule.task.util;

/**
 * 需要关闭的数据服务键信息
 *
 * @author soy
 */
public interface FieldKey
{
    /**
     * 是否有数据同步任务在运行
     */
    String CROSSDATA_JOB_EXISTED = "has_crossdata_job";

    /**
     * 是否有jdbc连接hugetable任务在运行
     */
    String HUGETABLE_JOB_EXISTED = "has_hugetable_job";
    
}
