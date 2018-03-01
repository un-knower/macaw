package cn.migu.macaw.hdfs.service.impl;

import java.io.IOException;
import java.net.URI;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;
import org.springframework.stereotype.Service;

import cn.migu.macaw.hdfs.service.IHdfsService;

/**
 * hdfs文件操作
 *
 * @author soy
 */
@Service
public class HdfsServiceImpl implements IHdfsService
{
    private static final Log logger = LogFactory.getLog(HdfsServiceImpl.class);

    /**
     * 删除hdfs文件
     * @param conf ha配置
     * @param hdfsPath hdfs文件路径
     * @param user 用户
     * @return boolean - 是否操作成功
     */
    @Override
    public boolean delete(Configuration conf, String hdfsPath, String user)
    {
        Path path = new Path(hdfsPath);
        FileSystem fs = null;
        try
        {
            fs = FileSystem.get(URI.create(conf.get("fs.defaultFS")), conf, user);
            
            if (fs != null)
            {
                fs.deleteOnExit(path);
            }
        }
        catch (InterruptedException e1)
        {
            return false;
        }
        catch (IOException e2)
        {
            e2.printStackTrace();
            return false;
        }
        finally
        {
            try
            {
                if (null != fs)
                {
                    fs.close();
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return true;
    }
    
    /**
     * 拷贝hdfs文件至本地
     * @param conf ha配置
     * @param hdfsFile hdfs文件路径
     * @param localFile 本地文件路径
     * @return boolean - 操作是否成功
     */
    @Override
    public boolean copyToLocal(Configuration conf, String hdfsFile, String localFile)
    {
        boolean result = false;
        
        FileSystem dstFS = null;
        
        FileSystem srcFS = null;
        
        try
        {
            dstFS = FileSystem.getLocal(conf);
            srcFS = FileSystem.get(URI.create(conf.get("fs.defaultFS")), conf);
            
            Path dstpath = new Path(localFile);
            Path srcpath = new Path(hdfsFile);
            
            result = FileUtil.copy(srcFS, srcpath, dstFS, dstpath, false, conf);
            
        }
        catch (Exception e)
        {
            logger.error("",e);
            result = false;
        }
        finally
        {
            try
            {
                if (null != dstFS)
                {
                    dstFS.close();
                }
                
                if (null != srcFS)
                {
                    srcFS.close();
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        
        return result;
        
    }
}
