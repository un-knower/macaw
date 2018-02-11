package cn.migu.macaw.jarboot.model;

/**
 * 数据文件采集配置
 *
 * @author soy
 */
public class DataFileCollectConf
{
    private String objId;

    private String collectPath;

    private Integer kind;

    private String address;

    private String topic;

    private String connectUrl;

    private Integer splitNum;

    private String fileCode;

    private String fileName;

    private Long waitTime;

    public String getObjId()
    {
        return objId;
    }

    public void setObjId(String objId)
    {
        this.objId = objId;
    }

    public String getCollectPath()
    {
        return collectPath;
    }

    public void setCollectPath(String collectPath)
    {
        this.collectPath = collectPath;
    }

    public Integer getKind()
    {
        return kind;
    }

    public void setKind(Integer kind)
    {
        this.kind = kind;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getTopic()
    {
        return topic;
    }

    public void setTopic(String topic)
    {
        this.topic = topic;
    }

    public String getConnectUrl()
    {
        return connectUrl;
    }

    public void setConnectUrl(String connectUrl)
    {
        this.connectUrl = connectUrl;
    }

    public Integer getSplitNum()
    {
        return splitNum;
    }

    public void setSplitNum(Integer splitNum)
    {
        this.splitNum = splitNum;
    }

    public String getFileCode()
    {
        return fileCode;
    }

    public void setFileCode(String fileCode)
    {
        this.fileCode = fileCode;
    }

    public String getFileName()
    {
        return fileName;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    public Long getWaitTime()
    {
        return waitTime;
    }

    public void setWaitTime(Long waitTime)
    {
        this.waitTime = waitTime;
    }
}
