/*
 * 文 件 名:  ZookeeperCli.java
 * 版    权:  Copyright 2015 咪咕互动娱乐有限公司,  All rights reserved
 * 描    述:  <描述>
 * 版    本： <版本号> 
 * 创 建 人:  史国俊
 * 创建时间:  2016年9月21日
 
 */
package cn.migu.macaw.jarboot.common;

import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import cn.migu.macaw.jarboot.model.ZkInfo;

/**
 * zookeeper客户端
 * 
 * @author  xxx
 */
public class ZookeeperCli
{
    private static CuratorFramework cli;

    /**
     * 启动客户端
     * @param sysZk
     * @throws Exception
     */
    public static void startCli(String sysZk) throws Exception
    {
        cli = CuratorFrameworkFactory.builder()
            .connectString(sysZk)
            .sessionTimeoutMs(5000)
            .retryPolicy(new ExponentialBackoffRetry(1000, 3))
            .build();
        cli.start();
    }

    /**
     * 删除zk节点
     * @param zkInfo
     * @param zkPath
     * @param sysZk
     * @throws Exception
     */
    public void deleteNode(ZkInfo zkInfo,String zkPath,String sysZk) throws Exception
    {
        if(isConnected())
        {
            if (cli.checkExists().forPath(zkPath) != null)
            {
                cli.delete().guaranteed().forPath(zkPath);
            }
        }
    }

    /**
     * 创建zk节点
     * @param zkInfo
     * @param flimeHead
     * @param name
     * @param res
     * @param zkPath
     * @param zkAddress
     * @param flumeConf
     * @throws Exception
     */
    public void createNode(ZkInfo zkInfo,String flimeHead,String name, List<ZkInfo> res,String zkPath,String zkAddress,String flumeConf) throws Exception 
    {
        cli.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(zkPath, flumeConf.getBytes());

        Stat stat = new Stat();
        String s = new String(cli.getData().storingStatIn(stat).forPath(zkPath));
    }

    /**
     *  判断连接是否存在
     * @return
     */
    public boolean isConnected()
    {
        if(cli == null)
        {
            return false;
        }
        else
        {
            return cli.getState() != CuratorFrameworkState.STOPPED;
        }
    }

    /**
     * 关闭zk连接
     */
    public void closeZk()
    {
        if(isConnected())
        {
            cli.close();
        }
    }
}
