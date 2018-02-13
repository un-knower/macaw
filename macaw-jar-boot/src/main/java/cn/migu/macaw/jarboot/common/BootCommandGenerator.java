package cn.migu.macaw.jarboot.common;

import cn.migu.macaw.jarboot.api.model.OnlineDataSource;
import cn.migu.macaw.jarboot.dao.OnlineDataSourceMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.migu.macaw.jarboot.JarBootConfigAttribute;
import cn.migu.macaw.jarboot.api.model.OfflineDataSource;
import cn.migu.macaw.jarboot.api.model.Server;
import cn.migu.macaw.jarboot.dao.OfflineDataSourceMapper;
import cn.migu.macaw.jarboot.model.JarConfParam;

/**
 * 启动命令创建器
 *
 * @author soy
 */
@Service
public class BootCommandGenerator
{
    @Autowired
    private JarBootConfigAttribute jarBootConfigAttribute;
    
    @Autowired
    private OfflineDataSourceMapper offlineDataSourceDao;

    @Autowired
    private OnlineDataSourceMapper onlineDataSourceDao;
    
    /**
     * 自定义jar启动命令
     * @param param 部署服务配置参数
     * @param hostInfo 部署主机信息
     * @return String - 启动命令
     */
    public String customEtlJarBoot(JarConfParam param, Server hostInfo)
    {
        StringBuilder bootShell = new StringBuilder(hostInfo.getNote());
        bootShell.append("/bin/java -jar -Dfile.encoding=UTF-8 ").append(param.getPath());
        bootShell.append(param.getName()).append(" ").append(param.getAppId()).append(" ");
        bootShell.append(param.getServerId()).append(" ").append(param.getObjId()).append(" ");
        bootShell.append(param.getDealUser()).append(" ").append(param.getPort()).append(" ");
        bootShell.append(jarBootConfigAttribute.getSqlQueryUrl()).append(" ");
        bootShell.append(jarBootConfigAttribute.getSqlExecuteUrl()).append(" ");
        bootShell.append(param.getNote());
        
        return bootShell.toString();
    }
    
    /**
     * 自定义jar启动命令(带外部函数功能)
     * @param param 部署服务配置参数
     * @param hostInfo 部署主机信息
     * @return String - 启动命令
     */
    public String customEtlJarWithExtFuncBoot(JarConfParam param, Server hostInfo)
    {
        StringBuilder bootShell = new StringBuilder(hostInfo.getNote());
        bootShell.append("/bin/java -Dfile.encoding=UTF-8 -cp ").append(param.getPath());
        bootShell.append(param.getName()).append(":").append(param.getPath());
        bootShell.append(jarBootConfigAttribute.getCustomJarName()).append(" ");
        bootShell.append(jarBootConfigAttribute.getCustomJarMainClass()).append(" ");
        bootShell.append(param.getAppId()).append(" ").append(param.getServerId()).append(" ");
        bootShell.append(param.getObjId()).append(" ").append(param.getDealUser()).append(" ");
        bootShell.append(param.getPort()).append(" ");
        bootShell.append(jarBootConfigAttribute.getSqlQueryUrl()).append(" ");
        bootShell.append(jarBootConfigAttribute.getSqlExecuteUrl()).append(" ");
        bootShell.append(param.getNote());
        
        return bootShell.toString();
        
    }

    /**
     * clean jar启动命令
     * @param param 部署服务配置参数
     * @param hostInfo 部署主机信息
     * @return String - 启动命令
     */
    public String cleanJarBoot(JarConfParam param, Server hostInfo)
    {
        OnlineDataSource ods = onlineDataSourceDao.selectByPrimaryKey(param.getInSource());
        String address = null;
        if(null != ods)
        {
            address = StringUtils.substringBeforeLast(ods.getAddress(),"/");
        }

        StringBuilder bootShell = new StringBuilder(hostInfo.getNote());
        bootShell.append("/bin/java -Xms");
        if(StringUtils.isNotEmpty(param.getNowJvm()))
        {
            bootShell.append(param.getNowJvm()).append("m -Xmx");
        }
        else
        {
            bootShell.append(2048).append("m -Xmx");
        }

        if(StringUtils.isNotEmpty(param.getMaxJvm()))
        {
            bootShell.append(param.getMaxJvm()).append("m -jar ");
        }
        else
        {
            bootShell.append(4096).append("m -jar");
        }

        bootShell.append(param.getPath()).append("clean.jar ").append(param.getAppId()).append(" ");
        bootShell.append(param.getServerId()).append(" ").append(param.getPort()).append(" ").append(param.getObjId());
        bootShell.append(" ").append(address).append(" ").append(param.getName());

        return bootShell.toString();
    }
    
    /**
     * FlumeJar启动命令
     * @param param 部署服务配置参数
     * @return String - 启动命令
     */
    public String flumeJarBoot(JarConfParam param)
    {
        int monitorPort = Integer.valueOf(param.getPort()) * 2;
        StringBuilder bootShell = new StringBuilder("cd ");
        bootShell.append(param.getPath()).append("bin;").append(param.getPath());
        bootShell.append("bin/flume-ng agent --conf ../conf --no-reload-conf -z ")
            .append(jarBootConfigAttribute.getZkUrl());
        bootShell.append(" -p ").append(ZkNodeInfo.FLUME_CONFIG).append(" -n ").append(param.getObjId());
        bootShell.append(" -Dflume.monitoring.type=http -Dfile.encoding=UTF-8 -Dflume.monitoring.port=")
            .append(monitorPort);
        bootShell.append(" -Dmigu.flume.rpc.type=http -Dmigu.flume.rpc.url.insert.pid=")
            .append(jarBootConfigAttribute.getAddProcessUrl());
        bootShell.append(" -Dmigu.flume.rpc.url.update.ctime=").append(jarBootConfigAttribute.getModFileLogUrl());
        bootShell.append(" -Dmigu.flume.rpc.hdfs.file.url.insert=").append(jarBootConfigAttribute.getAddHdfsLogUrl());
        bootShell.append(" -Dmigu.flume.rpc.hdfs.file.url.sqls=")
            .append(jarBootConfigAttribute.getBatchSqlExecuteUrl());
        bootShell.append(" -Dmigu.flume.rpc.sid=").append(param.getServerId());
        bootShell.append(" -Dmigu.flume.rpc.aid=").append(param.getAppId());
        bootShell.append("  -Dmigu.flume.rpc.jid=").append(param.getObjId());
        bootShell.append(" -Dmigu.flume.rpc.uid=").append(param.getDealUser());
        bootShell.append(
            " -Dflume.root.logger=INFO,console,LOGFILE -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=");
        bootShell.append(param.getJmxPort());
        bootShell.append(" -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false");
        
        return bootShell.toString();
    }

    /**
     * streaming启动命令
     * @param param
     * @return
     */
    public String streamingBoot(JarConfParam param)
    {
        String sparkMaster = jarBootConfigAttribute.getSparkMaster();
        String hdfsSchema = jarBootConfigAttribute.getHdfsPrefixSchema();

        OfflineDataSource ods = new OfflineDataSource();
        ods.setObjId(param.getOutSource());
        OfflineDataSource tOds = offlineDataSourceDao.selectOne(ods);
        if(null != tOds)
        {
            //spark master
            if(StringUtils.isNotEmpty(tOds.getAddress()))
            {
                sparkMaster = tOds.getAddress();
            }

            //hdfs schema prefix
            if(StringUtils.isNotEmpty(tOds.getReserve4()))
            {
                hdfsSchema = tOds.getReserve4();
            }
        }

        StringBuilder bootShell = new StringBuilder(param.getPath());
        bootShell.append("t1.sh ").append(param.getAppId()).append(" ");
        bootShell.append(param.getServerId()).append(" ");
        bootShell.append(param.getObjId()).append(" ");
        bootShell.append(sparkMaster).append(" ").append(hdfsSchema).append(" ");
        bootShell.append(param.getPort()).append(" ").append(param.getName()).append(" ");
        bootShell.append(param.getDealUser()).append(" ").append(param.getCpus()).append(" ");
        bootShell.append(param.getMemory()).append(" ").append(param.getDealNum()).append(" ");
        bootShell.append(param.getRefreshPeriod());

        return bootShell.toString();
    }
}
