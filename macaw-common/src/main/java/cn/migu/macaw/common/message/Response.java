package cn.migu.macaw.common.message;

/**
 * 与unify module之间操作响应类
 * 
 * @author  zhaocan
 * @version  [版本号, 2016年3月25日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class Response
{
    private Entity response;
    
    public Response()
    {
        
    }
    
    public Response(Entity response)
    {
        this.response = response;
    }
    
    public Entity getResponse()
    {
        return response;
    }
    
    public void setResponse(Entity response)
    {
        this.response = response;
    }
    
}
