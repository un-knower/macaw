package cn.migu.macaw.jarboot.common;

/**
 * 监控值类型
 *
 * @author soy
 */
public enum JarMonitorValueType
{
    /**
     * 原始目录
     */
    ORIGIANL(1),
    /**
     * 自定义
     */
    CUSTOM(2),
    /**
     * flume
     */
    FLUME(3),
    /**
     * clean
     */
    CLEAN(4),
    /**
     * streaming
     */
    STREAMING(5);

    private int value;

    JarMonitorValueType(int value)
    {
        this.value = value;
    }
}
