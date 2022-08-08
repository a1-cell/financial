package io.renren.pay.config;

import java.io.FileWriter;
import java.io.IOException;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *修改日期：2017-04-05
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

public class AlipayConfig {

//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    public static String app_id = "2021000120609605";

    // 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCQFDTg17DVF+B7gbhNuDpJvabkMpT8r8MRSfo1F3/nmSwbqjxCVG6/yy4QTowN7LhaTijYQ0wriVLJ4e6G2pUNMnoiASQ3R9FgpIwtZ0/bjG71YYVwJ1xqACaEtH80+cvgTzWRYjVoZ1/2wd8wgQrtwbcbEOV/D7sk3X+7l7Ro2tsOtoeUnpjA+cCZQ0sPfQABkylFvhTtSoZHolgs+oMtwyYiS4dNjEdCApNhoqI3ZXuY6fA8IHkcApVIVVgw3aJF/JQbwOBtTmF6Gi7tYBhqCkiWBD6h0P4oTMjAITEFCeazXyRDe1cgM7iHZyxxmdVy9I8cU5Mnrd0+T90wDVKfAgMBAAECggEAfP+q6K74kcfHUB+44GTG3RcW5cAklh1+5U8DLnCW3T18kPvej8Vk2CzUZ/1dcO9A+cf5k7a/LL5U/yVupbyZt/tlDn9jBjreojPgEbwiAl4SHf1foW/ks2oyAMv/2NwnTxIpbVqA73VQQmrs0c4Cx0dbLrY4BUaGnHM20G/La3O3VtaQpX+vt1tTRuM56GiNDX5ibTCuxiCwwyBmQwYgRMqJ4LP74z5PhXMJSM86gQFK6IYz72RKSG//6ZL+TW/hSpmaBOATz1JoxB8nKeWFywIZsYGuhV2U47K4n8JQZynNlL4VHccJJncEfrePA+FqB3y095MuyLLtLWvta8GnwQKBgQDGB9teFIKkjvMBSJPaDO92F8e1HbNDVtBq623c8eEvT7L61SyDtT6+giKT70hJte/iWSC91n3YbZG3QGRpZgforiRLUbghNxjKmhDBeA/aKuu73uPx99Mc1L/kYDRVhf2gwW2/1movVRd38MMu3nYSyxwCC/I9AwRjejwhBGB30QKBgQC6QUe+0bgQ8X4dAEdE8RKEL5pp2QT06QuTrOIyi8WhXk6iD6eGh42JmNhk9/ApxO8Vf9wWSRtUply7FcAlFLN+fey9m4O0ojsizwk0ppN76y0dzctWlSwFJF3Sc+u9dVSrT4hCxoC9ZyMnnC2okjXTGDJT5lFVTBrPzlsPGRovbwKBgEbXAK5DAAkF6gAHinxyLCc+kn5DxaDNKXlWBtMG49sc05XPNEFZULytqNjqvB1CLDH4WDIQzKvjZkD7k+/wQ9wEj48yCIA5SXEQc/YBkQomWHW4Yipo0Oh7Yrm7gH0nlXcwOqZpeUq9UcviILBffDV02uwQgfM5ZRSA4YmeGzRBAoGBAJP4fOlKb/N2B/0+VWCeLVm6//ALq/ZulvXxqZsClDYJWGtECJHGa1nfhjthBvoILPFlrRI/bmkn6g/vzcEn2xopLMXOISqGoHidBvE/TXhrdGJzNpJI3LoRFEC4O5c++c9H1jdGwMy4faNgXp3H2t8kIAeruP4NAlgJ1yn9hWIdAoGBAIkf5ZiqfFtDbdDoxSOcdRjS15pUH94ApmwpTd/bW0mXgAg+4+9HWY66hHBB3mkasctEsRqhxWE+F8wzcCtkNClVcSpJMCbD35Nhh0Mdv3nq6he2VLYVU3JndEUrH2vg9OdwOBpy2hLqNMoed5K4ZQ/az+t6HTOXTZBGEJOSDfIR";
    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjuUwAx5IrlWhIF57y/rqYV2mwIwRm947/lOv6G+0JH78wDADPSw2OyipYiZKARno93EvlNK06JyDelf/Otpcox34opj8bp8o21SC4AZ86d9CkP1bjfapu2/Omv92bvICPuJo7chEF/tcL9U9HSKOh3nWZ5dea5gSaVTlXQJHM6KzFQSdYOUwbPthY5Ti5T7w5yvBxxBkmXDeTY5PKozsY9exKNcNCcqCagcS8F5Y8zxfQIOmbWBat+1qeIVIdmBynUA9CUDJ+jiXlh0xPZBseowfg+d5oMwiYD8TL0qClycYBAAnvBrqTQkNwfGUzyUiRRnFe+X/G/QDpbgE81W4twIDAQAB";

    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "http://localhost:9995/pay/alipay/notify";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    //同步回调路径
    public static String return_url = "http://localhost:9995/pay/alipay/return";

    // 签名方式
    public static String sign_type = "RSA2";

    // 字符编码格式
    public static String charset = "utf-8";

    // 支付宝网关dev    ******沙箱域名+dev
    public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

    // 支付宝网关
    public static String log_path = "C:\\";


//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    /**
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis()+".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

