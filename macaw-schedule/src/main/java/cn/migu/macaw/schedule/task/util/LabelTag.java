package cn.migu.macaw.schedule.task.util;

/**
 * 标签信息
 *
 * @author soy
 */
public interface LabelTag
{
    /**
     * 自定义标签最小长度
     */
    int LABEL_MIN_LEN = 4;

    /**
     * 自定义标签开始字符
     */
    String LABEL_PREFIX = "${";

    /**
     * 自定义标签结束字符
     */
    String LABEL_SUFFIX = "}";
}
