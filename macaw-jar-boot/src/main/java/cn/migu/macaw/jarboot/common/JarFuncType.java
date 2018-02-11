package cn.migu.macaw.jarboot.common;

/**
 * 功能jar类型
 *
 * @author soy
 */
public enum JarFuncType
{
    /**
     * 原始文件采集jar
     */
    FLUME(1),
    /**
     * spring boot
     */
    SPRINGBOOT(2),
    /**
     * 自定义jar
     */
    CUSTOM(3),
    /**
     * 一般spark streaming
     * 内存表每批次数据不累加
     */
    NORMAL_STREAMING(4),
    /**
     * 合并spark streaming
     * 内存表每批次数据累加
     */
    MERGE_STREAMING(5),
    /**
     * 清洗jar
     */
    CLEAN(6),
    /**
     * 自定义检验文件
     */
    CUSTOM_CHECK_FILE(7);

    private int value = 0;

    private JarFuncType(int value)
    {
        this.value = value;
    }

    public int value()
    {
        return this.value;
    }
}
