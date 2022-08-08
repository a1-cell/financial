package io.renren.pay.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.xstream.XStream;
import io.renren.common.result.Result;
import io.renren.pay.config.Constants;
import io.renren.pay.entity.WxPayRequestParam;
import io.renren.pay.entity.WxQueryRequestParam;
import io.renren.pay.util.QRCodeUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;

@RestController
@RequestMapping("/pay")
@CrossOrigin
public class WxPayController {

    @GetMapping("/wxPay/{id}")
    public void payCode(@PathVariable String id, HttpServletRequest request, HttpServletResponse response) throws Exception {
        //根据订单ID查询订单信息 ：订单金额、订单名称、创建时间。。。。。
        //Result orderResult = orderClient.findById(id);
        //Map order=orderResult.getData();
        //genjvAPI  要求  准备所有必传参数
        WxPayRequestParam wxPayRequestParam = new WxPayRequestParam();
        //公共号id
        wxPayRequestParam.setAppid(Constants.APP_ID);
        //商户号
        wxPayRequestParam.setMch_id(Constants.MCH_ID);
        //随机数  32位
        String nonce_str = UUID.randomUUID().toString();
        nonce_str=nonce_str.replaceAll("-","");
        //随即字符串
        wxPayRequestParam.setNonce_str(nonce_str);
        wxPayRequestParam.setBody("botianjinrong");
//        wxPayRequestParam.setOut_trade_no(id);
        id = System.currentTimeMillis() + "";
        System.err.println("id:"+id);
        wxPayRequestParam.setOut_trade_no(id);
        //支付金额
        wxPayRequestParam.setTotal_fee(1);
        //请求用户的ip地址
        wxPayRequestParam.setSpbill_create_ip(request.getRemoteAddr());
        //通知地址
        wxPayRequestParam.setNotify_url("http://localhost:9995/wxResult");
        //交易类型
        wxPayRequestParam.setTrade_type("NATIVE");
        //将实体类转成map
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(wxPayRequestParam);
        Map paramMap=mapper.readValue(json,Map.class);
        paramMap.remove("sign");
        //将所有的key获取
        //无序的  我们想要有序的  转list
        Set set = paramMap.keySet();
        List list=new ArrayList(set);
        //排序
        Collections.sort(list);
        //定义字符串做支付拼接
        String tempString="";
        //字典序拼接  签名
        for (int i=0;i<list.size();i++){
            Object key = list.get(i);
            Object value = paramMap.get(key);
            tempString+=key+"="+value+"&";
        }
        //拼接秘钥
        tempString+="key="+ Constants.API_KEY;
        //将签名加密  并且转大写
        String sign = DigestUtils.md5Hex(tempString).toUpperCase();
        wxPayRequestParam.setSign(sign);
        //xstream工具实现报文
        XStream xStream = new XStream();
        //什么为跟标签  实体类转化成xml报文
        xStream.alias("xml",WxPayRequestParam.class);
        String xml = xStream.toXML(wxPayRequestParam);
        xml=xml.replaceAll("__","_");

        System.out.println(xml);


        //将报文发送给微信 得到相应结果
//        String url="https://api.mch.weixin.qq.com/pay/unifiedorder";
        //创建客户端 执行请求
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //创建请求接口
        HttpPost post = new HttpPost(Constants.UFDODER_URL);
        //得到请求的实体类
        HttpEntity requestEntity=new StringEntity(xml);
        post.setEntity(requestEntity);
        //实现请求
        CloseableHttpResponse httpResponse = httpClient.execute(post);
        //返回的实体
        HttpEntity responseEntity = httpResponse.getEntity();
        //得到返回的报文
        String respXML = EntityUtils.toString(responseEntity, "UTF-8");
        System.out.println(respXML);
        //从响应报文里得到codeURL
        SAXReader reader = new SAXReader();
        InputStream inputStream=new ByteArrayInputStream(respXML.getBytes());
        //获得文本文档
        Document document = reader.read(inputStream);
        Map xmlResp=new HashMap();
        Element rootElement = document.getRootElement();
        List<Element> elements = rootElement.elements();
        for (Element child : elements) {
            xmlResp.put(child.getName(),child.getStringValue());
        }

        String codeUrl="";
        if (xmlResp.get("return_code").equals("SUCCESS")){
            codeUrl= (String) xmlResp.get("code_url");
        }
        System.out.println(codeUrl);
        //将codeURL转成二维码图片
        QRCodeUtil.createQRCode(response,codeUrl);
    }
    @GetMapping("/wxQuery/{id}")
    public Result wxQuery(@PathVariable String id) throws Exception {

        WxQueryRequestParam wxQueryRequestParam = new WxQueryRequestParam();
        wxQueryRequestParam.setAppid(Constants.APP_ID);
        wxQueryRequestParam.setMch_id(Constants.MCH_ID);
        //随机数  32位
        String nonce_str = UUID.randomUUID().toString();
        nonce_str=nonce_str.replaceAll("-","");
        wxQueryRequestParam.setNonce_str(nonce_str);
        wxQueryRequestParam.setOut_trade_no(id);

        //将实体类转成map
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(wxQueryRequestParam);
        Map paramMap=mapper.readValue(json,Map.class);
        paramMap.remove("sign");
        //将所有的key获取
        //无序的  我们想要有序的  转list
        Set set = paramMap.keySet();
        List list=new ArrayList(set);
        //排序
        Collections.sort(list);
        String tempString="";
        //字典序拼接  签名
        for (int i=0;i<list.size();i++){
            Object key = list.get(i);
            Object value = paramMap.get(key);
            tempString+=key+"="+value+"&";
        }
        tempString+="key="+ Constants.API_KEY;
        //将签名加密  并且大写
        String sign = DigestUtils.md5Hex(tempString).toUpperCase();
        wxQueryRequestParam.setSign(sign);

        //xstream工具实现报文
        XStream xStream = new XStream();
        xStream.alias("xml",WxPayRequestParam.class);
        String xml = xStream.toXML(wxQueryRequestParam);
        xml=xml.replaceAll("__","_");

        System.out.println(xml);


        //将报文发送给微信 得到相应结果
//        String url="https://api.mch.weixin.qq.com/pay/unifiedorder";
        //创建客户端
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //创建请求接口
        HttpPost post = new HttpPost(Constants.CHECK_URL);
        //得到请求的实体类
        HttpEntity requestEntity=new StringEntity(xml);
        post.setEntity(requestEntity);
        //实现请求
        CloseableHttpResponse httpResponse = httpClient.execute(post);
        //返回的实体
        HttpEntity responseEntity = httpResponse.getEntity();
        //得到返回的报文
        String respXML = EntityUtils.toString(responseEntity, "UTF-8");
        System.out.println(respXML);


        //从响应报文里得到codeURL
        SAXReader reader = new SAXReader();
        InputStream inputStream=new ByteArrayInputStream(respXML.getBytes());
        //获得文本文档
        Document document = reader.read(inputStream);
        Map xmlResp=new HashMap();
        Element rootElement = document.getRootElement();
        List<Element> elements = rootElement.elements();
        for (Element child : elements) {
            xmlResp.put(child.getName(),child.getStringValue());
        }
        System.out.println(respXML);
        return new Result(true,"查询支付成功",null);
    }
}
