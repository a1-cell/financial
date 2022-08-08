package com.bw.bank.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.util.*;

public class SignUtil {

    public static String createSign(String respData, String secret){
        ObjectMapper objectMapper = new ObjectMapper();
        Map reqDataMap = null;
        try {
            reqDataMap = objectMapper.readValue(respData, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //将Map中的多组简直对进行字典序排序再拼接
        Set set = reqDataMap.keySet();
        List<String> keys = new ArrayList(set);
        Collections.sort(keys);
        String s = "";
        for(String key:keys){
            s += "&"+key+"="+reqDataMap.get(key);
        }
        s = s.substring(1);
        s+="&secret="+secret;
        return DigestUtils.md5Hex(s).toUpperCase();
    }

    public static Boolean checkSign(String reqData,String sign, String secret){
        ObjectMapper objectMapper = new ObjectMapper();
        Map reqDataMap = null;
        try {
            reqDataMap = objectMapper.readValue(reqData, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //将Map中的多组简直对进行字典序排序再拼接
        Set set = reqDataMap.keySet();
        List<String> keys = new ArrayList(set);
        Collections.sort(keys);
        String s = "";
        for(String key:keys){
            s += "&"+key+"="+reqDataMap.get(key);
        }
        s = s.substring(1);
        //System.out.println(s);
        s+="&secret="+secret;
        String ourSign = DigestUtils.md5Hex(s).toUpperCase();
        sign = sign.toUpperCase();
        System.out.println(sign);
        System.out.println(ourSign);

        return ourSign.equals(sign);
    }
}
