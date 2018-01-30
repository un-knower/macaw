package cn.migu.macaw.common;

import java.util.Map;

import cn.migu.macaw.common.log.LogUtils;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;

/**
 * rest template封装
 * @author soy
 */
public class RestTemplateProvider
{
    
    public static <T> T postFormForEntity(RestTemplate restTemplate, String url, Class<T> clazz,
        Map<String, String> kvs)
    {
        if (null == kvs || 0 == kvs.size())
        {
            return null;
        }
        
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        
        kvs.forEach((k, v) -> {
            map.add(k, v);
        });
        
        return postFormForEntity(restTemplate, url, map, clazz);
        
    }
    
    public static <T> T postFormForEntity(RestTemplate restTemplate, String url, MultiValueMap<String, String> map,
        Class<T> clazz)
    {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        if(response.getStatusCode() != HttpStatus.OK)
        {
            LogUtils.runLogError(String.format("post request:%s,return status error:%d",url,response.getStatusCodeValue()));
        }
        
        return JSON.parseObject(response.getBody(), clazz, Feature.InitStringFieldAsEmpty);
    }

    public static <T> T getForEntity(RestTemplate restTemplate, String url,Class<T> clazz)
    {
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        if(response.getStatusCode() != HttpStatus.OK)
        {
            LogUtils.runLogError(String.format("get request:%s,return status error:%d",url,response.getStatusCodeValue()));
        }
        return JSON.parseObject(response.getBody(), clazz, Feature.InitStringFieldAsEmpty);
    }
}
