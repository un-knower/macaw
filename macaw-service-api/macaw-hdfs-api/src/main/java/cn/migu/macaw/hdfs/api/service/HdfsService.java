package cn.migu.macaw.hdfs.api.service;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * hdfs文件操作
 *
 * @author soy
 */
@RequestMapping("/hdfs")
public interface HdfsService
{
    /**
     * 删除hdfs文件
     * @param conf hadoop ha配置
     * @param hdfsPath hdfs文件路径
     * @param user 用户
     * @return boolean - 是否成功
     */
    @PostMapping("/delete-by-user-with-conf")
    boolean deleteWithConf(@RequestParam("conf") String conf,@RequestParam("hdfsPath") String hdfsPath, @RequestParam("user")String user);

    /**
     * 删除hdfs文件
     * @param conf hadoop ha配置
     * @param hdfsPath hdfs文件路径
     * @return boolean - 是否成功
     */
    @PostMapping("/delete-with-conf")
    boolean deleteWithConf(@RequestParam("conf") String conf,@RequestParam("hdfsPath") String hdfsPath);

    /**
     * 把文件从hdfs拷贝到本地
     * @param conf hadoop ha配置
     * @param hdfsFile hdfs文件路径
     * @param localFile 本地文件路径
     * @return boolean - 是否成功
     */
    @PostMapping("/copy-to-local-with-conf")
    boolean copyToLocalWithConf(@RequestParam("conf") String conf,@RequestParam("hdfsFile") String hdfsFile, @RequestParam("localFile") String localFile);


    /**
     * 删除hdfs文件
     * @param hdfsPath hdfs文件路径
     * @param user 用户
     * @return boolean - 是否成功
     */
    @PostMapping("/delete-by-user")
    boolean delete(@RequestParam("hdfsPath") String hdfsPath, @RequestParam("user")String user);

    /**
     * 删除hdfs文件
     * @param hdfsPath hdfs文件路径
     * @return boolean - 是否成功
     */
    @PostMapping("/delete")
    boolean delete(@RequestParam("hdfsPath") String hdfsPath);

    /**
     * 把文件从hdfs拷贝到本地
     * @param hdfsFile hdfs文件路径
     * @param localFile 本地文件路径
     * @return boolean - 是否成功
     */
    @PostMapping("/copy-to-local")
    boolean copyToLocal(@RequestParam("hdfsFile") String hdfsFile, @RequestParam("localFile") String localFile);
}
