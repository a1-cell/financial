package com.renren.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.renren.common.utils.Result;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class BaiduController {

    @Value("${baidu.apiKey}")
    private String apiKey;

    @Value("${baidu.secretKey}")
    private String secretKey;

    @Value("${baidu.redirectUri}")
    private String redirectUri;

    @RequestMapping("/baidu111")
    public String baiduLogin(String code) throws Exception {
        System.out.println(code);

        String tokenUrl = "https://openapi.baidu.com/oauth/2.0/token";
        HttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost(tokenUrl);
        List<NameValuePair> tokenParams = new ArrayList<>();
        tokenParams.add(new BasicNameValuePair("grant_type", "authorization_code"));
        tokenParams.add(new BasicNameValuePair("code", code));
        tokenParams.add(new BasicNameValuePair("client_id", "0KarGjdTCIDKlbifVwmSLG1s"));
        tokenParams.add(new BasicNameValuePair("client_secret", "IbPzGZEbXmOMbi8y6vSnKA6IWXIiDzxz"));
        tokenParams.add(new BasicNameValuePair("redirect_uri", "http://localhost/baidu"));
        HttpEntity tokenRequestEntity = new UrlEncodedFormEntity(tokenParams);
        post.setEntity(tokenRequestEntity);
        HttpResponse tokenResponse = httpClient.execute(post);
        String tokenResult = EntityUtils.toString(tokenResponse.getEntity());
        System.out.println(tokenResult);

        ObjectMapper mapper = new ObjectMapper();
        Map map = mapper.readValue(tokenResult, Map.class);
        String access_token = (String) map.get("access_token");

        String infoUrl = "https://openapi.baidu.com/rest/2.0/passport/users/getLoggedInUser?";
        HttpPost post2 = new HttpPost(infoUrl);
        List<NameValuePair> infoParams = new ArrayList<>();
        infoParams.add(new BasicNameValuePair("access_token", access_token));
        HttpEntity infoRequestEntity = new UrlEncodedFormEntity(infoParams);
        post2.setEntity(infoRequestEntity);
        HttpResponse infoResponse = httpClient.execute(post2);
        String infoResult = EntityUtils.toString(infoResponse.getEntity());
        System.out.println(infoResult);

        //根据baiduId查询用户信息表，看是否有一个用户和这个百度账号关联
        return "OK";
    }


    @GetMapping("/baidu")
    public String baidu(String code) throws IOException {
        System.out.println(code);
        //接收code授权码后，用code换取用户的信息
        String tokenUrl = "https://openapi.baidu.com/oauth/2.0/token?grant_type=authorization_code&code="+code+"&client_id="+apiKey+"&client_secret="+secretKey+"&redirect_uri="+redirectUri;

        HttpClient httpClient = HttpClients.createDefault();
        HttpGet get = new HttpGet(tokenUrl);
        HttpResponse response = httpClient.execute(get);
        String json = EntityUtils.toString(response.getEntity(), "UTF-8");
        System.out.println(json);

        Map tokenResult = JSONObject.parseObject(json, Map.class);
        String access_token = (String) tokenResult.get("access_token");
        System.out.println(access_token);

        //根据token获取用户信息
        String infoUrl = "https://openapi.baidu.com/rest/2.0/passport/users/getLoggedInUser";
        HttpPost post = new HttpPost(infoUrl);
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair("access_token",access_token));
        //表单数据UrlEncodedFormEntity   JSON数据StringEntity
        HttpEntity entity = new UrlEncodedFormEntity(list);
        post.setEntity(entity);
        response  = httpClient.execute(post);
        json = EntityUtils.toString(response.getEntity(), "UTF-8");
        Map infoResult = JSONObject.parseObject(json, Map.class);
        String baiduId = (String) infoResult.get("uid");
        System.out.println(infoResult);
        System.out.println(baiduId);

        return "redirect:/success.html";
    }
}
