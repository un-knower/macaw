package cn.migu.macaw.jarboot.common;

/**
 * jar监控值格式
 *
 * @author soy
 */
public enum JarMonitorFormat
{
    /**
     * 键值对格式
     */
    MAP(1),
    /**
     * 字符串格式
     */
    STRING(2);
    private int value;

    JarMonitorFormat(int value)
    {
        this.value = value;
    }
}
