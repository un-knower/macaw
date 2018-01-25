package cn.migu.macaw.schedule.task.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * 正则表达式实现字符串替换
 * 可分组进行子串逐一替换
 * 
 * @author  soy
 */
public abstract class AbstractRegxReplaceByGroup
{
    private Pattern pattern;
    
    private Matcher matcher;
    
    private String dstObj;
    
    /**
     * 构造函数,初始化编译正则表达式
     */
    public AbstractRegxReplaceByGroup(String regex, String dstObj)
    {
        this.pattern = Pattern.compile(regex);
        
        this.dstObj = dstObj;
    }
    
    /**
     * 返回获取的组
     */
    public String group(int i)
    {
        return matcher.group(i);
    }

    /**
     * 抽象方法,用户实现需要替换的组方式
     * @return 替换后的字符串
     */
    public abstract String replacement();
    
    /**
     * 返回替换后的结果
     */
    public String rewrite(CharSequence original)
    {
        this.matcher = pattern.matcher(original);
        StringBuffer result = new StringBuffer(original.length());
        while (matcher.find())
        {
            matcher.appendReplacement(result, "");
            result.append(replacement());
        }
        matcher.appendTail(result);
        return result.toString();
    }
    
    public String getDstObj()
    {
        return dstObj;
    }
    
    public static void main(String... args)
        throws Exception
    {
        String str = "select xx from yy where msisdn=${msisdn}";
        
        // anonymous subclass
        AbstractRegxReplaceByGroup tripler = new AbstractRegxReplaceByGroup("([$][{]msisdn[}])", "13912952636")
        {
            @Override
            public String replacement()
            {
                return this.getDstObj();
            }
        };
        System.out.println(tripler.rewrite(str));

        
    }
}
