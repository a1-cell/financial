package io.renren.pay.config;

/**
 * 常量类
 * @author maming
 * @date 2017/11/29
 */
public class Constants {

    //微信服务号
    public static String APP_ID = "wxab8acb865bb1637e";
    public static String APP_SECRET = "2ab9071b06b9f739b950ddb41db2690d";
    //商户号和密钥
    public static String MCH_ID = "11473623";
    public static String API_KEY = "2ab9071b06b9f739b950ddb41db2690d";


    public static String NOTIFY_URL = "http://389e5929.ngrok.io/wechat-pay/renotify";

    public static String UFDODER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    public static String CHECK_URL = "https://api.mch.weixin.qq.com/pay/orderquery";


    public static String DEVICE_INFO = "WEB";
    public static String TRADE_TYPE = "NATIVE";
    public static String FEE_TYPE = "CNY";
    public static String SIGN_TYPE="HMAC-SHA256";

}
