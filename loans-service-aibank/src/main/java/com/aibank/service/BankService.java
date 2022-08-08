package com.aibank.service;

import com.aibank.entity.*;
import com.aibank.mapper.BankMapper;
import com.aibank.utils.SignUtil;
import com.aibank.utils.SmsUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class BankService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired(required = false)
    private BankMapper bankMapper;

    @Autowired
    private ObjectMapper objectMapper;

    public Map register(RequestGatewayEntity requestGatewayEntity) {
        Map result = new HashMap();
        //校验接收到的数据是否来源于合作方
        //判断商户是否存在
        Platform platform = bankMapper.findPlatform(requestGatewayEntity.getPlatformNo());
        if(platform==null){
            result.put("msg","平台编号有误");
            return result;
        }
        //验签
        List<String> secretList = bankMapper.findSecret(requestGatewayEntity.getPlatformNo());

        //根据签名规则和接收到的数据以及查询到的密钥生成签名
        //将所有发出的参数按照键的首字母进行字典序排序，拼接key1=value1&key2=value2....的形式。
        String reqData = requestGatewayEntity.getReqData();
        String sign = requestGatewayEntity.getSign();
        String secret = secretList.get(requestGatewayEntity.getKeySerial()-1);
        System.out.println(SignUtil.checkSign(reqData,sign,secret));
        //if(SignUtil.checkSign(reqData,sign,secret)){
            result.put("data",reqData);
            return result;
       // }
        //result.put("msg","验签失败");
        //return result;
    }

    public Boolean sendSms(String phone) {
        String code = "";
        Random random = new Random();
        for(int i=0;i<6;i++){
            code += random.nextInt(10);
        }
        redisTemplate.opsForValue().set(phone,code,1, TimeUnit.MINUTES);
        String result = SmsUtil.sendSms(phone,code);
        System.out.println(result);
        return result.equals("OK");
    }

    public String createUser(CreateUserEntity user) throws Exception {

        //取出P2P传递的reqData
        String json = user.getData();
        Map map = null;
        try {
            //从reqData里取出需要入库的数据存入entity
            map = objectMapper.readValue(json, Map.class);
            user.setPlatformUserNo( map.get("platformUserNo")+"");
            user.setRole((String) map.get("userRole"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //执行入库
        //校验验证码是否一致
        Boolean flag = true;
        String randomCode = (String) redisTemplate.opsForValue().get(user.getPhone());
        if(!randomCode.equals(user.getRandomCode())) {
            flag = false;
        }

        //入库
        int i = 0;
        if(flag){
            i = bankMapper.createUser(user);
        }

        String serviceName="PERSONAL_REGISTER_EXPAND";
        String platformNo=user.getPlatformNo();
        String keySerial="1";

        List<String> secretList = bankMapper.findSecret(user.getPlatformNo());

        Map respData = new HashMap();
        respData.put("requestNo",map.get("requestNo"));
        if(i>0){
            respData.put("code",0);
            respData.put("status","SUCCESS");
            respData.put("platformUserNo",user.getPlatformUserNo());
            respData.put("realname",user.getRealname());
        }else{
            respData.put("code",1);
            respData.put("status","INIT");
            respData.put("errorCode",100010);
            respData.put("errorMessage","账户不可用");
        }

        String respJson = objectMapper.writeValueAsString(respData);
        String sign = SignUtil.createSign(respJson,secretList.get(Integer.parseInt(keySerial)-1));
        respJson = URLEncoder.encode(respJson,"UTF-8");
        String result = "serviceName="+serviceName+"&platformNo="+platformNo+"&keySerial="+keySerial+"&respData="+respJson+"&sign="+sign;
        return (String)map.get("redirectUrl")+"?"+result;
    }

    public Map testLoan(RequestServiceEntity requestServiceEntity) {
        Map result = new HashMap();
        Platform platform = bankMapper.findPlatform(requestServiceEntity.getPlatformNo());
        if(platform==null){
            result.put("msg","平台编号有误");
            return result;
        }
        //验签
        List<String> secretList = bankMapper.findSecret(requestServiceEntity.getPlatformNo());

        Map respData = new HashMap();
        Map reqData = requestServiceEntity.getReqData();
        respData.put("platformUserNo",reqData.get("platformUserNo"));
        respData.put("code",0);
        respData.put("status","SUCCESS");
        respData.put("requestNo",reqData.get("requestNo"));
        respData.put("amoint",2000000.00);
        respData.put("projectNo",reqData.get("projectNo"));
        //放款到银行卡的方式
        respData.put("loanWay","NORMAL");
        //卡号
        respData.put("bankcardNo","1231323131");
        //银行编码
        respData.put("bankcode","ABOC");
        respData.put("transactionTime",new Date());
        respData.put("remitType","NORMAL");
        respData.put("loanForm","IMMEDIATE");
        System.out.println("放款");

        return respData;
    }
}
