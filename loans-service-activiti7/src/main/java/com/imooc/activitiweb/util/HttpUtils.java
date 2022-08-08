package com.imooc.activitiweb.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class HttpUtils {

    /**
     * @param url    请求路径
     * @param params 参数
     * @Title: doGet
     * @Description: get方式
     */
    public static String doGet(String url, Map<String, String> params) {

        // 返回结果
        String result = "";
        // 创建HttpClient对象
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet httpGet = null;
        try {
            // 拼接参数,可以用URIBuilder,也可以直接拼接在？传值，拼在url后面，如下--httpGet = new
            // HttpGet(uri+"?id=123");
            URIBuilder uriBuilder = new URIBuilder(url);
            if (null != params && !params.isEmpty()) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    uriBuilder.addParameter(entry.getKey(), entry.getValue());
                    // 或者用
                    // 顺便说一下不同(setParameter会覆盖同名参数的值，addParameter则不会)
                    // uriBuilder.setParameter(entry.getKey(), entry.getValue());
                }
            }
            URI uri = uriBuilder.build();
            // 创建get请求
            httpGet = new HttpGet(uri);
            log.info("访问路径：" + uri);
            HttpResponse response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {// 返回200，请求成功
                // 结果返回
                result = EntityUtils.toString(response.getEntity());
                log.info("请求成功！，返回数据：" + result);
            } else {
                log.info("请求成功，但是返回错误码,response:{}", response);
                //todo 自己处理业务，可以选择抛出自定义异常
            }
        } catch (IllegalArgumentException ie) {
            log.error("http请求参数出现异常，ie:{}", ie.getMessage());
            //todo 自己处理业务，可以选择抛出自定义异常
        } catch (HttpHostConnectException he) {
            log.error("连接IP超时,he:", he);
            //todo 自己处理业务，可以选择抛出自定义异常
        } catch (Exception e) {
            log.error("请求异常，e:", e);
            //todo 自己处理业务，可以选择抛出自定义异常
        } finally {
            // 释放连接
            if (null != httpGet) {
                httpGet.releaseConnection();
            }
        }
        return result;
    }

    /**
     * @param url
     * @param params
     * @Title: doPost
     * @Description: post请求
     */
    public static String doPost(String url, Map<String, String> params) {
        String result = "";
        // 创建httpclient对象
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = null;
        try { // 参数键值对
            httpPost = new HttpPost(url);
            if (null != params && !params.isEmpty()) {
                List<NameValuePair> pairs = new ArrayList<NameValuePair>();
                NameValuePair pair = null;
                for (String key : params.keySet()) {
                    pair = new BasicNameValuePair(key, params.get(key));
                    pairs.add(pair);
                }
                // 模拟表单
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(pairs);
                httpPost.setEntity(entity);
            }
            HttpResponse response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                result = EntityUtils.toString(response.getEntity(), "utf-8");
                log.info("返回数据：>>>" + result);
            } else {
                log.info("请求成功，但是返回错误码,response:{}", response);
                //todo 自己处理业务，可以选择抛出自定义异常
            }
        } catch (IllegalArgumentException ie) {
            log.error("http请求参数出现异常，ie:{}", ie.getMessage());
            //todo 自己处理业务，可以选择抛出自定义异常
        } catch (HttpHostConnectException he) {
            log.error("连接IP超时,he:", he);
            //todo 自己处理业务，可以选择抛出自定义异常
        } catch (Exception e) {
            log.error("请求异常，e:", e);
            //todo 自己处理业务，可以选择抛出自定义异常
        } finally {
            if (null != httpPost) {
                // 释放连接
                httpPost.releaseConnection();
            }
        }
        return result;
    }

    /**
     * @param url
     * @param params
     * @Title: sendJsonStr
     * @Description: post发送json字符串
     */
    public static String sendJsonStr(String url, String params) {
        String result = "";

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(url);
        try {
            httpPost.addHeader("Content-type", "application/json; charset=utf-8");
            httpPost.setHeader("Accept", "application/json");
            if (StringUtils.isNotBlank(params)) {
                httpPost.setEntity(new StringEntity(params, Charset.forName("UTF-8")));
            }
            HttpResponse response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                result = EntityUtils.toString(response.getEntity());
                log.info("返回数据：" + result);
            } else {
                log.info("请求成功，但是返回错误码,response:{}", response);
                //todo 自己处理业务，可以选择抛出自定义异常
            }
        } catch (IllegalArgumentException ie) {
            log.error("http请求参数出现异常，ie:{}", ie.getMessage());
            //todo 自己处理业务，可以选择抛出自定义异常
        } catch (HttpHostConnectException he) {
            log.error("连接IP超时,he:", he);
            //todo 自己处理业务，可以选择抛出自定义异常
        } catch (Exception e) {
            log.error("请求异常，e:", e);
            //todo 自己处理业务，可以选择抛出自定义异常
        }
        return result;
    }

    /**
     * //todo 可以把多个参数当成map传进来，保证更好的通用性，因为使用情况不多，所以写死了
     *
    @Title:doPostFile
     *
    @Description:post请求
     *
    @param
    url url
     *
    @param
    param1 请求参数
     *
    @param
    param2 请求参数
     *
    @param
    file 请求参数文件
     */

    public static String doPostFile(String url, String param1, String param2, MultipartFile file) {
        String result = "";
        // 创建httpclient对象
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = null;
        try { // 参数键值对
            httpPost = new HttpPost(url);
            // 模拟表单
            InputStream is = new ByteArrayInputStream(file.getBytes());
            InputStreamBody fileBody = new InputStreamBody(is, ContentType.MULTIPART_FORM_DATA, file.getOriginalFilename());

            StringBody param1Comment = new StringBody(param1, ContentType.APPLICATION_JSON);
            StringBody param2Comment = new StringBody(param2, ContentType.APPLICATION_JSON);
            HttpEntity reqEntity = MultipartEntityBuilder.create()
                    .addPart("file", fileBody)
                    .addPart("param1 ", param1Comment)
                    .addPart("param2 ", param2Comment)
                    .build();
            httpPost.setEntity(reqEntity);

            HttpResponse response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                result = EntityUtils.toString(response.getEntity(), "utf-8");
                System.out.println(result);
                log.info("返回数据：>>>" + result);
            } else {
                log.info("请求成功，但是返回错误码,response:{}", response);
                //todo 自己处理业务，可以选择抛出自定义异常
            }
        } catch (IllegalArgumentException ie) {
            log.error("http请求参数出现异常，ie:{}", ie.getMessage());
            //todo 自己处理业务，可以选择抛出自定义异常
        } catch (HttpHostConnectException he) {
            log.error("连接IP超时,he:", he);
            //todo 自己处理业务，可以选择抛出自定义异常
        } catch (Exception e) {
            log.error("请求异常，e:", e);
            //todo 自己处理业务，可以选择抛出自定义异常
        } finally {
            if (null != httpPost) {
                // 释放连接
                httpPost.releaseConnection();
            }
        }
        return result;
    }
}
