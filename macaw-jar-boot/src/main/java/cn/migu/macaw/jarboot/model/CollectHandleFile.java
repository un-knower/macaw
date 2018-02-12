package cn.migu.macaw.jarboot.model;

/**
 * 自定义jar处理文件
 *
 * @author soy
 */
public class CollectHandleFile
{
    private String fileId;

    private String fileCode;

    private String fileName;

    private Integer period;

    private Integer scope;

    public String getFileId()
    {
        return fileId;
    }

    public void setFileId(String fileId)
    {
        this.fileId = fileId;
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

    public Integer getPeriod()
    {
        return period;
    }

    public void setPeriod(Integer period)
    {
        this.period = period;
    }

    public Integer getScope()
    {
        return scope;
    }

    public void setScope(Integer scope)
    {
        this.scope = scope;
    }
}
