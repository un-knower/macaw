package cn.migu.macaw.jarboot.common;

import java.util.ArrayList;
import java.util.List;

import cn.migu.macaw.common.OnlineDataSourceType;
import cn.migu.macaw.common.log.LogUtils;
import cn.migu.macaw.jarboot.model.DataFileCollectConf;
import cn.migu.macaw.jarboot.model.JarConfParam;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;


import cn.migu.macaw.jarboot.model.ZkInfo;

/**
 *
 *
 * @author xxx
 *
 */
@Service
public class ZkUtils implements ZkNodeInfo
{
    private static Logger logger = Logger.getLogger(ZkUtils.class);
    
    public static String shareMemory = "$SOURCE.channels.$H.type = memory\n" + "$SOURCE.channels.$H.capacity = 100000\n"
        + "$SOURCE.channels.$H.transactionCapacity = 1000\n"
        + "$SOURCE.channels.$H.keep-alive=3\n";

    /**
     * 保存zk节点信息
     * @param param
     * @param list
     * @param zkAddress
     * @param rollInterval
     * @return
     */
    public boolean saveZk(JarConfParam param, List<DataFileCollectConf> list, String zkAddress, String rollInterval)
    {
        ZookeeperCli cli = null;
        try
        {
            ZkInfo zkInfo = new ZkInfo();
            cli = new ZookeeperCli();
            ZookeeperCli.startCli(zkAddress);
            /**
             * 配置头，也是节点名称, 这个是数据库的自增ID
             */
            String name = param.getObjId();
            String zkPath = FLUME_CONFIG + "/" + name;
            cli.deleteNode(zkInfo, zkPath, zkAddress);
            /**
             * 根据数据库拼出Flume配置文件头,顺序与List<ZkInfo>  保持一致
             */
            String source = "$SOURCE.sources = " + returnSource(list) + "\n";
            /**
             * 如果没有hdfs，则不需要共享内存,c1,c2,c3... 这种方式表示kafka—channel
             */
            String channels = "$SOURCE.channels = " + returnChannels(list) + "\n";
            /**
             * 因为kaka没有sink，所有只有一个k1
             */
            String sinks = "$SOURCE.sinks = " + returnSinks(list) + "\n";
            
            String flimeHead = source + sinks + channels;
            flimeHead = flimeHead.replace("$SOURCE", name);
            List<ZkInfo> res = returnZkInfo(list, rollInterval, zkAddress);
            String flumeConf = flimeHead + getConf(name, res);
            cli.createNode(zkInfo, flimeHead, name, res, zkPath, zkAddress, flumeConf);

            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LogUtils.runLogError(e);
        }
        finally
        {
            if(null != cli)
            {
                cli.closeZk();
            }
        }
        return false;
    }

    /**
     * 创建zk对象
     * @param list
     * @param rollInterval
     * @param zkAddress
     * @return
     */
    public List<ZkInfo> returnZkInfo(List<DataFileCollectConf> list, String rollInterval, String zkAddress)
    {
        List<ZkInfo> res = new ArrayList<>();
        for (DataFileCollectConf jarSource : list)
        {
            ZkInfo zkHdfs = new ZkInfo();
            ZkInfo zkKafka = new ZkInfo();
            if (jarSource.getKind() == OnlineDataSourceType.HDFS.ordinal())
            {
                //"hdfs://172.18.111.3:9000/sgj/%y-%m-%d"
                zkHdfs = new ZkInfo(HDFS_TYPE, jarSource.getCollectPath(), jarSource.getAddress(), rollInterval, String.valueOf(jarSource.getSplitNum()),
                    null, null, jarSource.getObjId() + "r1", jarSource.getObjId() + "h1", jarSource.getObjId() + "k1",
                    null,String.valueOf(jarSource.getWaitTime()));
            }
            else if (jarSource.getKind() == OnlineDataSourceType.IDOT_KAFKA.ordinal())
            {
                //"172.18.111.3:9092,172.18.111.4:9092,172.18.111.5:9092"
                zkKafka = new ZkInfo(KAFKA_TYPE, jarSource.getCollectPath(), null, null, null, jarSource.getConnectUrl(),
                    jarSource.getTopic(), jarSource.getObjId() + "r2", jarSource.getObjId() + "c1", null, zkAddress,null);
            }
            
            /**
             * java8 自动类型推断,<>解放了
             */
            res.add(zkHdfs);
            res.add(zkKafka);
        }
        return res;
    }

    /**
     * 输入源/采集路径
     * @param list
     * @return
     */
    public String returnSource(List<DataFileCollectConf> list)
    {
        StringBuffer kafkaSource = new StringBuffer();
        StringBuffer hdfsSource = new StringBuffer();
        StringBuffer allSource = new StringBuffer();
        for (DataFileCollectConf unifyJarSource : list)
        {
            if (unifyJarSource.getKind()== OnlineDataSourceType.IDOT_KAFKA.ordinal())
            {
                kafkaSource.append(unifyJarSource.getObjId() + "r2 ");
            }
            else if (unifyJarSource.getKind() == OnlineDataSourceType.HDFS.ordinal())
            {
                hdfsSource.append(unifyJarSource.getObjId() + "r1 ");
            }
        }
        return allSource.append(kafkaSource).append(hdfsSource).toString().trim();
    }

    /**
     * 管道信息
     * @param list
     * @return
     */
    public String returnChannels(List<DataFileCollectConf> list)
    {
        StringBuffer kafkaChannel = new StringBuffer();
        StringBuffer hdfsChannel = new StringBuffer();
        StringBuffer allChannel = new StringBuffer();
        for (DataFileCollectConf unifyJarSource : list)
        {
            if (unifyJarSource.getKind()== OnlineDataSourceType.IDOT_KAFKA.ordinal())
            {
                kafkaChannel.append(unifyJarSource.getObjId() + "c1 ");
            }
            else if (unifyJarSource.getKind() == OnlineDataSourceType.HDFS.ordinal())
            {
                hdfsChannel.append(unifyJarSource.getObjId() + "h1 ");
            }
        }
        return allChannel.append(kafkaChannel).append(hdfsChannel).toString().trim();
    }

    /**
     * 输出源信息
     * @param list
     * @return
     */
    public String returnSinks(List<DataFileCollectConf> list)
    {
        StringBuffer hdfsSink = new StringBuffer();
        for (DataFileCollectConf unifyJarSource : list)
        {
            if (unifyJarSource.getKind() == OnlineDataSourceType.HDFS.ordinal())
            {
                hdfsSink.append(unifyJarSource.getObjId() + "k1 ");
            }
        }
        return hdfsSink.toString().trim();
    }

    /**
     * 消息格式
     * @param name
     * @param zkInfo
     * @return
     */
    public String getConf(String name, List<ZkInfo> zkInfo)
    {
        
        //StringBuilder sb = new StringBuilder();
        String conf = "";
        try
        {
            for (ZkInfo zk : zkInfo)
            {
                if (HDFS_TYPE.equals(zk.getType()))
                {
                    conf += "$SOURCE.sources.$R.type = spooldir\n" + "$SOURCE.sources.$R.maxBackoff = 10000\n" 
                        + "$SOURCE.sources.$R.spoolDir = $spoolDir\n"
                        + "$SOURCE.sources.$R.fileHeader = true\n" + "$SOURCE.sources.$R.channels = $H\n"
                        + "$SOURCE.sources.$R.ignorePattern = ^(.)*\\.(log|tmp|filepart|gz|tar|zip)$\n" + shareMemory
                        + "$SOURCE.sinks.$K.type=hdfs\n" + "$SOURCE.sinks.$K.channel = $H\n"
                        + "$SOURCE.sinks.$K.hdfs.path = $hdfsPath\n" + "$SOURCE.sinks.$K.hdfs.round = true\n"
                        + "$SOURCE.sinks.$K.hdfs.rollInterval = $rollInterval\n" + "$SOURCE.sinks.$K.hdfs.threadsPoolSize = 10\n"
                        + "$SOURCE.sinks.$K.hdfs.idleTimeout = $idleTimeout\n" + "$SOURCE.sinks.$K.hdfs.rollSize = 0\n"
                        + "$SOURCE.sinks.$K.hdfs.rollCount = $rollCount\n" + "$SOURCE.sinks.$K.hdfs.batchSize = 1000\n"
                        + "$SOURCE.sinks.$K.hdfs.roundValue = 1\n" + "$SOURCE.sinks.$K.hdfs.roundUnit = minute\n"
                        + "$SOURCE.sinks.$K.hdfs.useLocalTimeStamp = true\n"
                        + "$SOURCE.sinks.$K.hdfs.fileType = DataStream\n" + "$SOURCE.sinks.$K.hdfs.callTimeout=60000\n"
                        + "$SOURCE.sinks.$K.hdfs.minBlockReplicas = 1\n";
                    
                    conf = conf.replace("$SOURCE", name)
                        .replace("$R", zk.getSource())
                        .replace("$K", zk.getSink())
                        .replace("$H", zk.getChannel())
                        .replace("$spoolDir", zk.getSpoolDir())
                        .replace("$hdfsPath", zk.getHdfsPath())
//                        .replace("$rollInterval", zk.getRollInterval())
                        .replace("$rollCount",
                            (null != zk.getRollCount() && !"".equals(zk.getRollCount())) ? zk.getRollCount()
                                : "100000")
                        .replace("$rollInterval",     (null != zk.getWaitTime() && !"".equals(zk.getWaitTime())) ? zk.getWaitTime()
                                : "300")
                        .replace("$idleTimeout",     (null != zk.getWaitTime() && !"".equals(zk.getWaitTime())) ? String.valueOf(Integer.parseInt(zk.getWaitTime())+5)
                            : "305");
                    
                }
                else if (KAFKA_TYPE.equals(zk.getType()))
                {
                    conf += "$SOURCE.sources.$R.type = spooldir\n" + "$SOURCE.sources.$R.spoolDir = $spoolDir\n"
                        + "$SOURCE.sources.$R.fileHeader = false\n" + "$SOURCE.sources.$R.channels = $C\n"
                        + "$SOURCE.sources.$R.ignorePattern = ^(.)*\\.(log|tmp|filepart|gz|tar|zip)$\n"
                        + "$SOURCE.channels.$C.type = org.apache.flume.channel.kafka.KafkaChannel\n"
                        + "$SOURCE.channels.$C.capacity = 10000\n" + "$SOURCE.channels.$C.transactionCapacity = 1000\n"
                        + "$SOURCE.channels.$C.brokerList = $brokerList\n"
                        + "$SOURCE.channels.$C.parseAsFlumeEvent = true\n" + "$SOURCE.channels.$C.topic = $topic\n"
                        + "$SOURCE.channels.$C.zookeeperConnect = $zkAddress\n"
                        + "$SOURCE.channels.$C.auto.commit.enable=false\n";
                    conf = conf.replace("$SOURCE", name)
                        .replace("$R", zk.getSource())
                        .replace("$C", zk.getChannel())
                        .replace("$spoolDir", zk.getSpoolDir())
                        .replace("$brokerList", zk.getBrokerList())
                        .replace("$topic", zk.getTopic())
                        .replace("$zkAddress", zk.getZkAddress());
                    
                }
            }
        }
        catch (Exception e)
        {
            LogUtils.runLogError(e);
        }
        return conf;
    }
    
}
