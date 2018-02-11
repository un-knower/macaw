package cn.migu.macaw.jarboot.model;

/**
 * 
 * @author soy
 *
 */
public class ZkInfo
{
    
    private String type;
    
    private String spoolDir;
    
    private String hdfsPath;
    
    private String rollInterval;
    
    private String rollCount;
    
    private String brokerList;
    
    private String topic;
    
    private String source;
    
    private String channel;
    
    private String sink;
    
    private String name;
    
    private String sources;
    
    private String channels;
    
    private String sinks;
    
    private String zkAddress;
    
    private String waitTime;
    
    public ZkInfo()
    {
        
    }
    
    public ZkInfo(String type, String spoolDir, String hdfsPath, String rollInterval, String rollCount,
        String brokerList, String topic, String source, String channel, String sink, String zkAddress,String waitTime)
    {
        super();
        this.type = type;
        this.spoolDir = spoolDir;
        this.hdfsPath = hdfsPath;
        this.rollInterval = rollInterval;
        this.rollCount = rollCount;
        this.brokerList = brokerList;
        this.topic = topic;
        this.source = source;
        this.channel = channel;
        this.sink = sink;
        this.zkAddress = zkAddress;
        this.waitTime=waitTime;
    }
    
    public String getSource()
    {
        return source;
    }
    
    public void setSource(String source)
    {
        this.source = source;
    }
    
    public String getZkAddress()
    {
        return zkAddress;
    }

    public void setZkAddress(String zkAddress)
    {
        this.zkAddress = zkAddress;
    }

    public String getChannel()
    {
        return channel;
    }
    
    public void setChannel(String channel)
    {
        this.channel = channel;
    }
    
    public String getSink()
    {
        return sink;
    }
    
    public void setSink(String sink)
    {
        this.sink = sink;
    }
    
    public String getType()
    {
        return type;
    }
    
    public void setType(String type)
    {
        this.type = type;
    }
    
    public String getSpoolDir()
    {
        return spoolDir;
    }
    
    public void setSpoolDir(String spoolDir)
    {
        this.spoolDir = spoolDir;
    }
    
    public String getHdfsPath()
    {
        return hdfsPath;
    }
    
    public void setHdfsPath(String hdfsPath)
    {
        this.hdfsPath = hdfsPath;
    }
    
    public String getRollInterval()
    {
        return rollInterval;
    }
    
    public void setRollInterval(String rollInterval)
    {
        this.rollInterval = rollInterval;
    }

    public String getRollCount()
    {
        return rollCount;
    }

    public void setRollCount(String rollCount)
    {
        this.rollCount = rollCount;
    }

    public String getBrokerList()
    {
        return brokerList;
    }
    
    public void setBrokerList(String brokerList)
    {
        this.brokerList = brokerList;
    }
    
    public String getTopic()
    {
        return topic;
    }
    
    public void setTopic(String topic)
    {
        this.topic = topic;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getSources()
    {
        return sources;
    }

    public void setSources(String sources)
    {
        this.sources = sources;
    }

    public String getChannels()
    {
        return channels;
    }

    public void setChannels(String channels)
    {
        this.channels = channels;
    }

    public String getSinks()
    {
        return sinks;
    }

    public void setSinks(String sinks)
    {
        this.sinks = sinks;
    }

    public String getWaitTime()
    {
        return waitTime;
    }

    public void setWaitTime(String waitTime)
    {
        this.waitTime = waitTime;
    }
    
    
    
}
