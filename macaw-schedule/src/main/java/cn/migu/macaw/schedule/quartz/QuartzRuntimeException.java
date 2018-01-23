package cn.migu.macaw.schedule.quartz;

import org.quartz.SchedulerException;

/**
 * quartz运行时异常
 * @author soy
 */
public class QuartzRuntimeException extends RuntimeException
{
    
    private static final long serialVersionUID = 1L;
    
    private SchedulerException schedulerException;
    
    public SchedulerException getSchedulerException()
    {
        return schedulerException;
    }
    
    public QuartzRuntimeException(SchedulerException schedulerException)
    {
        this.schedulerException = schedulerException;
    }
    
    public QuartzRuntimeException()
    {
    }
    
    public QuartzRuntimeException(String message)
    {
        super(message);
    }
    
    public QuartzRuntimeException(Throwable cause)
    {
        super(cause);
    }
    
    public QuartzRuntimeException(String message, Throwable cause)
    {
        super(message, cause);
    }
    
    @Override
    public String toString()
    {
        if (schedulerException != null)
        {
            return schedulerException.toString();
        }
        return super.toString();
    }
}
