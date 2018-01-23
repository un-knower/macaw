package cn.migu.macaw.dag.errors;

/**
 * 存在不期望的顶点集异常
 * 
 * @author  zhaocan
 * @version  [版本号, 2016年5月27日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class GraphHadUnexpectedIds extends ErrorMixin
{
    private static final long serialVersionUID = 1L;
    
    public GraphHadUnexpectedIds()
    {
        super();
    }
    
    public GraphHadUnexpectedIds(String template, Object... parts)
    {
        super(template, parts);
    }
}