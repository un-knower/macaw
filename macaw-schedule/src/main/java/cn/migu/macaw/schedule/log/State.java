package cn.migu.macaw.schedule.log;

/**
 * 定时任务/节点状态
 * @author soy
 */
public enum State
{
    /**
     * 未初始化
     */
    NOINIT,
    /**
     * 正常结束
     */
    TERMINATION,
    /**
     * 运行中
     */
    RUNNING,
    /**
     * 异常结束
     */
    EXCEPTION
}
