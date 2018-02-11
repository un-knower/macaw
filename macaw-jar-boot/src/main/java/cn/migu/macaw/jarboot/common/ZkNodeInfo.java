package cn.migu.macaw.jarboot.common;

/**
 * zk业务节点类型
 *
 * @author soy
 */
public interface ZkNodeInfo
{
    /**
     * hdfs类型
     */
    String HDFS_TYPE = "1";

    /**
     * kafka类型
     */
    String KAFKA_TYPE = "2";

    /**
     * flume config节点名
     */
    String FLUME_CONFIG = "/flumeconfig";
}
