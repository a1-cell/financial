package com.renren.controller;

import com.renren.service.OverService;
import com.xxl.job.core.handler.annotation.XxlJob;
import io.renren.common.ocerdue.Overdue;
import io.renren.common.result.Result;
import oracle.jdbc.proxy.annotation.Post;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/v1")
@CrossOrigin
@Component
public class OverdueController {
    @Autowired
    private OverService overService;

    @GetMapping("/list")
    public Result getOverList(){
        List<Overdue> list=overService.getList();
        return new Result(true,"查询成功",list);
    }


    /**
     * 手动催收
     * @param overdue
     * @return
     * @throws IOException
     */
    @PostMapping("/cui")
    public Result sendEmail(@RequestBody Overdue overdue) throws IOException {
        HttpClient client = new HttpClient();
        PostMethod post = new PostMethod("https://utf8api.smschinese.cn/");
        post.addRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=utf-8");//在头文件中设置转码
        NameValuePair[] data ={ new NameValuePair("Uid", "zhengyisky2008"),new NameValuePair("Key", "zaq12wsx1904"),new NameValuePair("smsMob",overdue.getTel()),new NameValuePair("smsText","你好，你已过期"+overdue.getOverday()+"天，请及时归还")};
        post.setRequestBody(data);

        client.executeMethod(post);
        Header[] headers = post.getResponseHeaders();
        int statusCode = post.getStatusCode();
        System.out.println("statusCode:"+statusCode); //HTTP状态码
        for(Header h : headers){
            System.out.println(h.toString());
        }
        String result = new String(post.getResponseBodyAsString().getBytes("utf-8"));
        System.out.println(result);  //打印返回消息状态

        post.releaseConnection();

        return new Result(true,"短信催收成功","");
    }

    @XxlJob("job-min")
    public void sendAll() throws IOException {
        System.out.println("=======1");
//        List<Overdue> list=overService.getList();
//        for (Overdue overdue : list) {
//            HttpClient client = new HttpClient();
//            PostMethod post = new PostMethod("https://utf8api.smschinese.cn/");
//            post.addRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=utf-8");//在头文件中设置转码
//            NameValuePair[] data ={ new NameValuePair("Uid", "zhengyisky2008"),new NameValuePair("Key", "zaq12wsx1904"),new NameValuePair("smsMob",overdue.getTel()),new NameValuePair("smsText","你好，你已过期"+overdue.getOverday()+"天，请及时归还")};
//            post.setRequestBody(data);
//            client.executeMethod(post);
//            Header[] headers = post.getResponseHeaders();
//            int statusCode = post.getStatusCode();
//            System.out.println("statusCode:"+statusCode); //HTTP状态码
//            for(Header h : headers){
//                System.out.println(h.toString());
//            }
//            String result = new String(post.getResponseBodyAsString().getBytes("utf-8"));
//            System.out.println(result);  //打印返回消息状态
//
//            post.releaseConnection();
//        }
    }

}
