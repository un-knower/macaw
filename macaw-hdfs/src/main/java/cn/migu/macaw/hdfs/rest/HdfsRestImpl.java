package cn.migu.macaw.hdfs.rest;

import cn.migu.macaw.hdfs.api.service.HdfsService;
import cn.migu.macaw.hdfs.common.HaConf;
import cn.migu.macaw.hdfs.service.IHdfsService;
import org.apache.hadoop.conf.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${hdfs.default-user}")
    private String hdfsDefaultUser;

    @Autowired
    private Configuration hdfsHaConf;

    @Autowired
    private IHdfsService hdfsService;

    @Override
    public boolean deleteWithConf(@RequestParam("conf") String conf,
        @RequestParam("hdfsPath") String hdfsPath, @RequestParam("user") String user)
    {
        Configuration haConf = HaConf.createConf(conf);

        return hdfsService.delete(haConf,hdfsPath,user);
    }

    @Override
    public boolean deleteWithConf(@RequestParam("conf") String conf,
        @RequestParam("hdfsPath") String hdfsPath)
    {
        return deleteWithConf(conf,hdfsPath,hdfsDefaultUser);
    }

    @Override
    public boolean copyToLocalWithConf(@RequestParam("conf") String conf, @RequestParam("hdfsFile") String hdfsFile,
        @RequestParam("localFile") String localFile)
    {
        Configuration haConf = HaConf.createConf(conf);

        return hdfsService.copyToLocal(haConf,hdfsFile,localFile);
    }

    @Override
    public boolean delete(@RequestParam("hdfsPath") String hdfsPath, @RequestParam("user") String user)
    {
        return hdfsService.delete(hdfsHaConf,hdfsPath,user);
    }

    @Override
    public boolean delete(@RequestParam("hdfsPath") String hdfsPath)
    {
        return delete(hdfsPath,hdfsDefaultUser);
    }

    @Override
    public boolean copyToLocal(@RequestParam("hdfsFile") String hdfsFile,
        @RequestParam("localFile") String localFile)
    {
        return hdfsService.copyToLocal(hdfsHaConf,hdfsFile,localFile);
    }
}
