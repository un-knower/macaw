package cn.migu.macaw.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 * 
 * @author soy
 */
public class DateUtil
{
    
    /**
     * 获取当天日期
     * @param format
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static String getCurrentDate(String format)
    {
        
        Date currentDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String specifiedDay = sdf.format(currentDate);
        
        return specifiedDay;
    }
    
    /**
     * 指定日期字符串
     * @param specDate
     * @param format
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static String getSpecDate(Date specDate, String format)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String specifiedDay = sdf.format(specDate);
        
        return specifiedDay;
    }
    
    /**
     * 获取日期对象
     * @param format
     * @param dateStr
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static Date getDateFormat(String dateStr, String format)
    {
        Date date = null;
        try
        {
            date = new SimpleDateFormat(format).parse(dateStr);
        }
        catch (ParseException e)
        {
            return null;
        }
        
        return date;
    }
    
    /**
     * 获取日期前n天
     * @param n
     * @param format
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static String getBeforeNDay(int n, String format)
    {
        Calendar c = Calendar.getInstance();
        Date date = null;
        try
        {
            date = new SimpleDateFormat(format).parse(getCurrentDate(format));
            
        }
        catch (ParseException e)
        {
            e.printStackTrace();
            return null;
        }
        c.setTime(date);
        int _day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, _day - n);
        
        String dayBefore = new SimpleDateFormat(format).format(c.getTime());
        return dayBefore;
    }
    
    /**
     * 当天日期退后n天
     * @param n
     * @param format
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static String getAfterNDay(int n, String format)
    {
        Calendar c = Calendar.getInstance();
        Date date = null;
        try
        {
            date = new SimpleDateFormat(format).parse(getCurrentDate(format));
        }
        catch (ParseException e)
        {
            e.printStackTrace();
            return null;
        }
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day + n);
        
        String dayAfter = new SimpleDateFormat(format).format(c.getTime());
        return dayAfter;
    }
    
    public static String getDateString(Date date, String pattern)
    {
        DateFormat format = new SimpleDateFormat(pattern);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return format.format(c.getTime());
    }
    
    public static String getToday(String pattern)
    {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(new Date());
    }
    
    /**
     * 输入一个日期字符串和格式,和当前日期比较
     * @param dateStr 比较日期字符串
     * @param format 格式
     * @return
     * @throws ParseException
     * @see [类、类#方法、类#成员]
     */
    public static int compareCurrentDate(String dateStr, String format)
        throws ParseException
    {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date compareDate = sdf.parse(dateStr);
        Date currentDate = new Date();
        
        if (compareDate.after(currentDate))
        {
            return 1;
        }
        
        if (compareDate.before(currentDate))
        {
            return -1;
        }
        
        return 0;
        
    }
    
}