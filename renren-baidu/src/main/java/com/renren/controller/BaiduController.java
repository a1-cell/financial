package com.renren.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class BaiduController {

    @RequestMapping("/baidu")
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
}
