package cn.migu.macaw.sparkdrivermgr.common;

/**
 * 进程状态
 *
 * @author soy
 */
public interface ProcessStatus
{
    /**
     * 进程等待状态
     */
    int PROCESS_WAITING = 0;

    /**
     * 进程运行状态
     */
    int PROCESS_RUNNING = 1;

    /**
     * 进程准备状态
     */
    int PROCESS_READY = 2;
}
