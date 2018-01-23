package cn.migu.macaw.common;

import cn.migu.macaw.common.log.LogUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * rest template封装
 * @author soy
 */
public class RestTemplateProvider
{

    public static <T> T postFormForEntity(RestTemplate restTemplate,String url,Class<T> clazz, String... kvs)
    {
        if(null == kvs || 0 == kvs.length || 0 != kvs.length % 2)
        {
            return null;
        }

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

        for(int i = 0; i < kvs.length - 1;i += 2)
        {
            map.add(kvs[i],kvs[i+1]);
        }

        return postFormForEntity(restTemplate,url,map,clazz);

    }

    public static <T> T postFormForEntity(RestTemplate restTemplate,String url,Class<T> clazz, Map<String,String> kvs)
    {
        if(null == kvs || 0 == kvs.size())
        {
            return null;
        }

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();

        kvs.forEach((k,v) ->{
            map.add(k,v);
        });

        return postFormForEntity(restTemplate,url,map,clazz);

    }

    public static <T> T postFormForEntity(RestTemplate restTemplate,String url,MultiValueMap<String, String> map,Class<T> clazz)
    {
        try
        {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(
                url,
                request,
                String.class);

            return JSON.parseObject(response.getBody(),clazz, Feature.InitStringFieldAsEmpty);
        }
        catch (Exception e)
        {
            LogUtils.runLogError(e);
        }

        return null;
    }
}
