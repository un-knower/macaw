package cn.migu.macaw.hdfs.service;

import org.apache.hadoop.conf.Configuration;

/**
 * hdfs文件操作
 *
 * @author soy
 */
public interface IHdfsService
{

    /**
     * 删除hdfs文件
     * @param conf ha配置
     * @param hdfsPath hdfs文件路径
     * @param user 用户
     * @return boolean - 是否操作成功
     */
    boolean delete(Configuration conf,String hdfsPath,String user);

    /**
     * 拷贝hdfs文件至本地
     * @param conf ha配置
     * @param hdfsFile hdfs文件路径
     * @param localFile 本地文件路径
     * @return boolean - 操作是否成功
     */
    boolean copyToLocal(Configuration conf,String hdfsFile, String localFile);
}
