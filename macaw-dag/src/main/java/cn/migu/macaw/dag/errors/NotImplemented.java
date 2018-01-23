package cn.migu.macaw.dag.errors;

/**
 * 方法未实现异常
 * 
 * @author  zhaocan
 * @version  [版本号, 2016年5月27日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class NotImplemented extends ErrorMixin
{
    private static final long serialVersionUID = 1L;
    
    public NotImplemented()
    {
        super();
    }
    
    public NotImplemented(String template, Object... parts)
    {
        super(template, parts);
    }
}
