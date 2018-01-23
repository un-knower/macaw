package cn.migu.macaw.dag.errors;

/**
 * 有向图存在环异常
 * 
 * @author  zhaocan
 * @version  [版本号, 2016年5月27日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class DagCannotHaveCycle extends ErrorMixin
{
    private static final long serialVersionUID = 1L;
    
    public DagCannotHaveCycle()
    {
        super();
    }
    
    public DagCannotHaveCycle(String template, Object... parts)
    {
        super(template, parts);
    };
}