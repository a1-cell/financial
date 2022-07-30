package com.bw.bank.controller;

import com.alibaba.fastjson.JSONObject;
import com.bw.bank.entity.Bank;
import com.bw.bank.entity.CreateUserEntity;
import com.bw.bank.entity.RequestEnity;
import com.bw.bank.entity.RequestEntity;
import com.bw.bank.service.BankService;
import com.bw.bank.utils.RSAUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.renren.common.result.Result;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RequestMapping("/bha-neo-app/lanmaotech")
@Controller
@CrossOrigin
public class BankController {

    @Value("${key.publicKey}")
    private String publicKey;

    @Value("${key.privateKey}")
    private String privateKey;

    @Autowired
    BankService bankService;

    //爬虫测试
    @RequestMapping("/spider/{name}")
    @ResponseBody
    public Result spider(@PathVariable String name) throws Exception {
        //根据小区名称获取小区id
        Document document1 = Jsoup.connect("https://bj.lianjia.com/ershoufang/rs"+name+"/").get();
        Elements select = document1.select("[class=clear LOGVIEWDATA LOGCLICKDATA]");
        if(select.size()==0){
            return new Result(false,"社区名称有误!","");
        }
        String id = select.attr("data-lj_action_resblock_id");

        //根据id爬取社区详情 扣出价格
        String url="https://bj.lianjia.com/xiaoqu/"+id;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        String html = EntityUtils.toString(entity);
        //System.out.println(html);
        //利用Jsoup将源代码装成douument对象
        Document document = Jsoup.parse(html);
        Element element = document.select(".xiaoquUnitPrice").first();
        String price = element.html();
        return new Result(true,"成功",price);
    }
    //开户
    @RequestMapping("/gateway")
    public String gateway(RequestEntity requestEntity, HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {
        if(requestEntity.getServiceName().isEmpty()||
                requestEntity.getPlatformNo()==null||
                requestEntity.getKeySerial()==null||
                requestEntity.getReqData().isEmpty()||
                requestEntity.getSign().isEmpty()){
            request.setAttribute("msg","参数不合法");
            return "redirect:/error.html";
        }
        String reqData = requestEntity.getReqData();
        ObjectMapper mapper = new ObjectMapper();
        Map map = mapper.readValue(reqData, Map.class);
        if(requestEntity.getServiceName().equals("PERSONAL_REGISTER_EXPAND")){
            Map result = bankService.register(requestEntity);

            if(result.get("msg")==null){
                String json = (String) result.get("data");
                json = json.replaceAll("\"", "&quot;");
                request.setAttribute("data",json);
                request.setAttribute("platformNo",requestEntity.getPlatformNo());
                return "redirect:/register.html";
            }
            request.setAttribute("msg",result.get("msg"));
            return "redirect:/error.html";
        }

        if(requestEntity.getServiceName().equals("ENTERPRISE_REGISTER")){
            return "redirect:/enterpriseRegister.html";
        }

        request.setAttribute("msg","未找到符合要求的网关接口");
        return "redirect:/error.html";
    }


    @PostMapping("/initParam")
    @ResponseBody
    public RequestEnity initPersonRegisterParam() throws Exception {
        RequestEnity requestEnity = new RequestEnity();
        requestEnity.setServiceName("PERSONAL_REGISTER_EXPAND");
        requestEnity.setPlatformNo("2236568989");
        requestEnity.setKeySerial(1);
        Map map = new HashMap();
        map.put("platformUserNo","123456");
        map.put("requestNo", UUID.randomUUID().toString());
        map.put("userRole","BORROWERS");
        map.put("checkType","LIMIT");
        map.put("redirectUrl","http://localhost:9113/bha-neo-app/lanmaotech/test");
        String reqData = JSONObject.toJSONString(map);
        requestEnity.setReqData(reqData);
        requestEnity.setSign(RSAUtils.encrypt(reqData.substring(0,50),publicKey));
        return requestEnity;
    }
    @GetMapping("/test")
    public String test(){
        return "redirect:/success.html";
    }
    @RequestMapping("/createUser")
    public String createUser(Bank bank, HttpServletRequest request) throws Exception {
        bankService.createUser(bank);
        return "redirect:"+"http://localhost:9113/bha-neo-app/lanmaotech/test";

    }


//    @RequestMapping("/sign")
//    public String setSign(RequestEntity requestEntity, HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {
//        if (requestEntity.getServiceName().isEmpty() ||
//                requestEntity.getPlatformNo() == null ||
//                requestEntity.getKeySerial() == null ||
//                requestEntity.getReqData().isEmpty() ||
//                requestEntity.getSign().isEmpty()) {
//            request.setAttribute("msg", "参数不合法");
//            return "redirect:/error.html";
//        }
//        String reqData = requestEntity.getReqData();
//        ObjectMapper mapper = new ObjectMapper();
//        Map map = mapper.readValue(reqData, Map.class);
//        if(requestEntity.getServiceName().equals("ESTABLISH_PROJECT")){
//            Map result = bankService.register(requestEntity);
//
//
//        }
//        return "ok";
//    }
}
