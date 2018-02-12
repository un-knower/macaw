package cn.migu.macaw.jarboot.common;

/**
 * 通用状态值
 *
 * @author soy
 */
public enum StatusValue
{
    /**
     * 正常
     */
    VALID(1),
    /**
     * 异常
     */
    INVALID(2);
    private int value;

    StatusValue(int value)
    {
        this.value = value;
    }
}
