package io.renren.pay.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.renren.common.borrow.Borrow;
import io.renren.common.result.Result;
import io.renren.pay.config.AlipayConfig;
import io.renren.pay.service.ApliService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/pay")
@CrossOrigin
public class AliPayController {
    @Autowired
    private ApliService checkService;
    @RequestMapping("/alipay")
    @ResponseBody
    public String alipay(HttpServletRequest request, Integer id) throws Exception {
        //根据id查询订单
        //根据id查询订单
        Borrow list=checkService.findBorrowList(id);
        ObjectMapper m1 = new ObjectMapper();
        String json = m1.writeValueAsString(list);
        Map data = m1.readValue(json, Map.class);
        //获得初始化的AlipayClient对象
        AlipayClient alipayClient = new DefaultAlipayClient(
                AlipayConfig.gatewayUrl,
                AlipayConfig.app_id,
                AlipayConfig.merchant_private_key,
                "json", AlipayConfig.charset,
                AlipayConfig.alipay_public_key,
                AlipayConfig.sign_type);

        //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        //同步回调路径
        alipayRequest.setReturnUrl(AlipayConfig.return_url);
        //异步回调路径
        alipayRequest.setNotifyUrl(AlipayConfig.notify_url);

        //商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = id+"";
        //付款金额，必填
        String total_amount = data.get("borrowMoney")+"";
        //订单名称，必填
        String subject = new String("LiuyHYYDS");
        //商品描述，可空
        String body = new String("bwie");

        alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
                + "\"total_amount\":\""+ total_amount +"\","
                + "\"subject\":\""+ subject +"\","
                + "\"body\":\""+ body +"\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");


        //请求
        String result = alipayClient.pageExecute(alipayRequest).getBody();

        return result;
    }
    /**
     * 同步回调
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/alipay/return")
    public String returnUrl(HttpServletRequest request) throws Exception {

        //获取支付宝GET过来反馈信息
        Map<String,String> params = new HashMap<String,String>();
        Map<String,String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }

        boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type); //调用SDK验证签名

        //——请在这里编写您的程序（以下代码仅作参考）——
        if(signVerified) {
            //商户订单号
            String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");

            //支付宝交易号
            String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");

            //付款金额
            String total_amount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"),"UTF-8");

            System.out.println("trade_no:"+trade_no+"<br/>out_trade_no:"+out_trade_no+"<br/>total_amount:"+total_amount);

            //根据订单编号total_amount查询订单信息

            //判断订单信息中的金额  和用户实际支付金额是否一致
            //如果一致 修改订单状态
            //如果不一致 订单标位异常  发短信....


            return "redirect:/success.html";
        }else {
            return "redirect:/error.html";
        }
    }


    @GetMapping("/findAll")
    @ResponseBody
    public Result findAll(){
        List<Borrow> list=checkService.findAll();
        return new Result(true,"选择列表查询成功",list);
    }

}
