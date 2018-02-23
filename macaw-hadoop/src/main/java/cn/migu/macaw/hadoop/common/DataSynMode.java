package cn.migu.macaw.hadoop.common;

/**
 * 数据同步方式
 *
 * @author soy
 */
public enum DataSynMode
{
    /**
     * 覆盖
     */
    OVERWRITE(0),
    /**
     * 追加
     */
    APPEND(1);

    DataSynMode(int value)
    {
        this.value = value;
    }

    private int value;
}
