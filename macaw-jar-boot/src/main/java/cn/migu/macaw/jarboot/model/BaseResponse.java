package cn.migu.macaw.jarboot.model;

/**
 *
 * @author xxx
 */
public class BaseResponse
{
    private BaseResponseEntity response;
    
    public BaseResponse()
    {
    }
    
    public BaseResponse(BaseResponseEntity response)
    {
        this.response = response;
    }
    
    public BaseResponseEntity getResponse()
    {
        return response;
    }
    
    public void setResponse(BaseResponseEntity response)
    {
        this.response = response;
    }
    
}