package io.renren.note.mq;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.xstream.XStream;

import java.net.URISyntaxException;
import java.text.SimpleDateFormat;

import com.xxl.job.core.handler.annotation.XxlJob;
import io.renren.note.client.CheckClient;
import io.renren.note.config.WxTextMessage;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import io.renren.common.borrow.Borrow;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.*;
@Component
@RabbitListener(queues = {"wxmq"})
@RequestMapping("/notemq")
@RestController
@CrossOrigin
public class NoteMq {

    @Autowired
    private CheckClient checkClient;
    @RabbitHandler
    public void outMq(Borrow borrow1) throws Exception {
            String borrowTime = borrow1.getBorrowTime();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = formatter.parse(borrowTime);
            String periods = borrow1.getPeriods();
            String s = periods.split("个")[0];
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            Date time = calendar.getTime();
            calendar.set(Calendar.MONTH,calendar.get(Calendar.MONTH)+Integer.parseInt(s));
            Date time1 = calendar.getTime();
            calendar.set(Calendar.DATE,calendar.get(Calendar.DATE)-7);
            Date time2 = calendar.getTime();
            Date date1 = new Date();
            //发送短信
            System.out.println(borrow1.getBorrowRen());
            if (time2.before(date1)){
                System.out.println("微信推送");
                wxPost();
                System.out.println("发短信get用户");

                HttpClient client = new HttpClient();
                PostMethod post = new PostMethod("https://utf8api.smschinese.cn/");
                post.addRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=utf-8");//在头文件中设置转码
                NameValuePair[] data ={ new NameValuePair("Uid", "zhengyisky2008"),new NameValuePair("Key", "zaq12wsx1904"),new NameValuePair("smsMob","16632006372"),new NameValuePair("smsText","尊敬的"+borrow1.getBorrowRen()+"用户您好,您有东西即将到期，请及时查收")};
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
                checkClient.updateBorrow(borrow1);

            }

    }


//    @GetMapping("/hh")
//    public Result hh(){
//        Result mqhh = checkClient.mqhh();
//        Map data = (Map)mqhh.getData();
//        return new Result(true,"串行",data);
//    }


//
//    @GetMapping("/wxs")
//    public String wxsGet(String signature,String timestamp,String nonce,String echostr){
//        System.out.println("成功!");
//        return echostr;
//    }
//
//
//    @PostMapping("/wxs")
//    public String kk(HttpServletRequest request) throws Exception {
//        String s=wxsPost(request);
//        return s;
//    }
//    private static String wxsPost(HttpServletRequest request) throws Exception {
//        ServletInputStream inputStream = request.getInputStream();
//        SAXReader saxReader = new SAXReader();
//        Document doc = saxReader.read(inputStream);
//        Element root = doc.getRootElement();
//        List<Element> elements = root.elements();
//        System.out.println(elements+"*******92531");
//        Map<String,String> requestMap = new HashMap<>();
//        for (Element child:elements) {
//            requestMap.put(child.getName(),child.getStringValue());
//        }
//        String message=requestMap.get("Content");
//        System.out.println(message);
//
//        //封装需要回复的XML报文
//        WxTextMessage wxTextMessage = new WxTextMessage();
//        wxTextMessage.setFromUserName(requestMap.get("ToUserName"));
//        wxTextMessage.setToUserName(requestMap.get("FromUserName"));
//        wxTextMessage.setContent(requestMap.get("Content"));
//        wxTextMessage.setMsgType("text");
//        wxTextMessage.setCreateTime(System.currentTimeMillis());
//
//        XStream xStream = new XStream();
//        xStream.alias("xml",WxTextMessage.class);
//        String responseXML = xStream.toXML(wxTextMessage);
//        return responseXML;
//
//    }


//    @XxlJob("jobLiu")
//    @GetMapping("/oo")
private static void wxPost() throws Exception {
//    public void wxPost() throws Exception {
        String access_token = wxsGet();
        String json="{\n" +
                "    \"touser\":\"o-cKZ5gFSLW2OL-Tv0VPoFJnQgVg\",\n" +
                "\n" +
                "    \"template_id\":\"PKtoWynnudooOq7C6y_0MjuSBLMTq3VB_Mv6-_nwlio\",\n" +
                "\t\"url\":\"http://weixin.qq.com/download\",\n" +
                "\n" +
                "    \"topcolor\":\"#FF0000\",\n" +
                "\n" +
                "    \"data\":{\n" +
                "\n" +
                "            \"User\": {\n" +
                "\n" +
                "                \"value\":\"不差钱先生\",\n" +
                "\n" +
                "                \"color\":\"#173177\"\n" +
                "\n" +
                "            },\n" +
                "\n" +
                "            \"Date\":{\n" +
                "\n" +
                "                \"value\":\"08月04日 15时17分\",\n" +
                "\n" +
                "                \"color\":\"#173177\"\n" +
                "\n" +
                "            },\n" +
                "\n" +
                "            \"CardNumber\": {\n" +
                "\n" +
                "                \"value\":\"0426\",\n" +
                "\n" +
                "                \"color\":\"#173177\"\n" +
                "\n" +
                "            },\n" +
                "\n" +
                "            \"Type\":{\n" +
                "\n" +
                "                \"value\":\"消费\",\n" +
                "\n" +
                "                \"color\":\"#173177\"\n" +
                "\n" +
                "            },\n" +
                "\n" +
                "            \"Money\":{\n" +
                "\n" +
                "                \"value\":\"人民币260.00元\",\n" +
                "\n" +
                "                \"color\":\"#173177\"\n" +
                "\n" +
                "            },\n" +
                "\n" +
                "            \"DeadTime\":{\n" +
                "\n" +
                "                \"value\":\"06月07日19时24分\",\n" +
                "\n" +
                "                \"color\":\"#173177\"\n" +
                "\n" +
                "            },\n" +
                "\n" +
                "            \"Left\":{\n" +
                "\n" +
                "                \"value\":\"6504.09\",\n" +
                "\n" +
                "                \"color\":\"#173177\"\n" +
                "\n" +
                "            }\n" +
                "\n" +
                "    }\n" +
                "\n" +
                "}";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String url="https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+access_token;
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Content-Type", "application/json");
        httpPost.setEntity(new StringEntity(json,"UTF-8")); //防止中文乱码
        CloseableHttpResponse execute = httpClient.execute(httpPost);
        //相应成功
        HttpEntity entity = execute.getEntity();
        String s = EntityUtils.toString(entity, "UTF-8");
        System.out.println(s);

        execute.close();
        httpClient.close();
    }


    private static String wxsGet() throws Exception {
        //1.创建httpclient对象
        CloseableHttpClient client= HttpClients.createDefault();
        //2.创建封装的URI对象，可以存放参数
        URIBuilder uri=new URIBuilder("https://api.weixin.qq.com/cgi-bin/token");
        //存放参数--可以参照地址栏：键值对方法
        uri.addParameter("grant_type","client_credential");
        uri.addParameter("appid","wx3f297b585de35e78");
        uri.addParameter("secret","01882fba313b979815721d7d785b0f87");
        //3.创建get对象:并放入URI
        HttpGet get=new HttpGet(uri.build());
        //4.执行get方法，并返回响应结果
        CloseableHttpResponse response = client.execute(get);
        System.out.println(response);
        HttpEntity entity = response.getEntity();
        String s = EntityUtils.toString(entity);
        ObjectMapper mapper = new ObjectMapper();
        Map map = mapper.readValue(s, Map.class);
//        String s = JSON.toJSONString(response);
        String access_token = (String) map.get("access_token");



        return access_token;
    }


}
