package cn.migu.macaw.common.message;

/**
 * 数据同步结果
 *
 * @author soy
 */
public class CrossDataResult
{
    private String starttime;

    private String endtime;

    private String jobId;

    private String jobState;

    private long inputRecordNum;

    private long outputRecordNum;

    public CrossDataResult()
    {

    }

    public CrossDataResult(String starttime, String endtime, String jobId, String jobState, long inputRecordNum,
        long outputRecordNum)
    {
        this.starttime = starttime;
        this.endtime = endtime;
        this.jobId = jobId;
        this.jobState = jobState;
        this.inputRecordNum = inputRecordNum;
        this.outputRecordNum = outputRecordNum;
    }

    public String getStarttime()
    {
        return starttime;
    }

    public void setStarttime(String starttime)
    {
        this.starttime = starttime;
    }

    public String getEndtime()
    {
        return endtime;
    }

    public void setEndtime(String endtime)
    {
        this.endtime = endtime;
    }

    public String getJobId()
    {
        return jobId;
    }

    public void setJobId(String jobId)
    {
        this.jobId = jobId;
    }

    public String getJobState()
    {
        return jobState;
    }

    public void setJobState(String jobState)
    {
        this.jobState = jobState;
    }

    public long getInputRecordNum()
    {
        return inputRecordNum;
    }

    public void setInputRecordNum(long inputRecordNum)
    {
        this.inputRecordNum = inputRecordNum;
    }

    public long getOutputRecordNum()
    {
        return outputRecordNum;
    }

    public void setOutputRecordNum(long outputRecordNum)
    {
        this.outputRecordNum = outputRecordNum;
    }
}
