package com.bw.bank.utils;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;

public class SmsUtil {

    public static String sendSms(String phone, String code){
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "LTAI8IOvDCaQwwl2", "0hha7nGCvjmJTolgG1gxkvAWEG64nP");
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", "zhengyisky");
        request.putQueryParameter("TemplateCode", "SMS_126875864");
        request.putQueryParameter("TemplateParam", "{\"code\":\""+code+"\"}");
        String resultCode = null;
        try {
            CommonResponse response = client.getCommonResponse(request);
            ObjectMapper mapper = new ObjectMapper();
            Map map = mapper.readValue(response.getData(), Map.class);
            resultCode = (String) map.get("Code");
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultCode;
    }
}
