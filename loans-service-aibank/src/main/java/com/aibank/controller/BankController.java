package com.aibank.controller;

import com.aibank.entity.CreateUserEntity;
import com.aibank.entity.RequestGatewayEntity;
import com.aibank.entity.RequestServiceBack;
import com.aibank.entity.RequestServiceEntity;
import com.aibank.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/lanmaotech")
public class BankController {

    @Autowired
    private BankService bankService;

    @RequestMapping("/test")
    public String test(){
        return "test";
    }

    @RequestMapping("/service")
    @ResponseBody
    public RequestServiceBack service(@RequestBody RequestServiceEntity requestServiceEntity, HttpServletRequest request) {
        String serviceName = requestServiceEntity.getServiceName();
        RequestServiceBack back = new RequestServiceBack();
        Map map = new HashMap();
        if (serviceName.equals("DIRECT_LOAN")) {
            System.out.println("已成功接受信息准备放款");
            Map test = bankService.testLoan(requestServiceEntity);
            back.setRespData(test);
            back.setSign(requestServiceEntity.getSign());
            return back;
        }
        map.put("code","1");
        map.put("liyou","请求方式不存在");
        back.setRespData(map);
        back.setSign(requestServiceEntity.getSign());
        return back;
    }

    @RequestMapping("/gateway")
    public String gateway(RequestGatewayEntity requestGatewayEntity, HttpServletRequest request){
        if(requestGatewayEntity.getServiceName().isEmpty()||
            requestGatewayEntity.getPlatformNo()==null||
            requestGatewayEntity.getKeySerial()==null||
            requestGatewayEntity.getReqData().isEmpty()||
            requestGatewayEntity.getSign().isEmpty()){
            request.setAttribute("msg","参数不合法");
            return "error";
        }

        //验证签名

        if(requestGatewayEntity.getServiceName().equals("PERSONAL_REGISTER_EXPAND")){
            Map result = bankService.register(requestGatewayEntity);

            if(result.get("msg")==null){
                String json = (String) result.get("data");
                json = json.replaceAll("\"", "&quot;");
                request.setAttribute("data",json);
                request.setAttribute("platformNo", requestGatewayEntity.getPlatformNo());
                return "register";
            }
            request.setAttribute("msg",result.get("msg"));
            return "error";
        }

        if(requestGatewayEntity.getServiceName().equals("ENTERPRISE_REGISTER")){



            return "enterpriseRegister";
        }

        request.setAttribute("msg","未找到符合要求的网关接口");
        return "error";
    }

    @RequestMapping("/sendSms")
    @ResponseBody
    public Boolean sendSms(String phone){
        return bankService.sendSms(phone);
    }

    @RequestMapping("/createUser")
    public String createUser(CreateUserEntity user, HttpServletRequest request) throws Exception {
        String url = bankService.createUser(user);
        return "redirect:"+url;

    }
}
