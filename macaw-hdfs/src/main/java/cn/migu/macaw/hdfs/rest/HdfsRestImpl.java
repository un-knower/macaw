package cn.migu.macaw.hdfs.rest;

import cn.migu.macaw.hdfs.api.service.HdfsService;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * hdfs服务
 *
 * @author soy
 */
@RestController
public class HdfsRestImpl implements HdfsService
{

    @Override
    public boolean deleteWithConf(@RequestParam("conf") String conf,
        @RequestParam("hdfsPath") String hdfsPath, @RequestParam("user") String user)
    {
        return false;
    }

    @Override
    public boolean deleteWithConf(@RequestParam("conf") String conf,
        @RequestParam("hdfsPath") String hdfsPath)
    {
        return false;
    }

    @Override
    public boolean copyToLocalWithConf(@RequestParam("conf") String conf, @RequestParam("hdfsFile") String hdfsFile,
        @RequestParam("localFile") String localFile)
    {
        return false;
    }

    @Override
    public boolean delete(@RequestParam("hdfsPath") String hdfsPath, @RequestParam("user") String user)
    {
        return false;
    }

    @Override
    public boolean delete(@RequestParam("hdfsPath") String hdfsPath)
    {
        return false;
    }

    @Override
    public boolean copyToLocal(@RequestParam("hdfsFile") String hdfsFile,
        @RequestParam("localFile") String localFile)
    {
        return false;
    }
}
