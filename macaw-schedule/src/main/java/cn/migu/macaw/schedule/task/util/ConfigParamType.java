package cn.migu.macaw.schedule.task.util;

/**
 * 配置运行参数类型
 *
 * @author soy
 */
public interface ConfigParamType
{
    /**
     * 系统类型
     */
    String SYS_TYPE = "1";

    /**
     * 自定义SQL标签替换类型
     */
    String SQL_LABEL_TYPE = "2";

    /**
     * 运行sql
     */
    String RUN_SQL_TYPE = "3";

    /**
     * 运行shell命令
     */
    String RUN_SHELL_TYPE = "4";

    /**
     * 存储过程变量
     */
    String PROC_VAR = "5";

    /**
     * 输出redis变量
     */
    String REDIS_OUT_VAR = "6";
}
