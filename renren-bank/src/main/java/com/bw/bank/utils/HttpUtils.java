package com.bw.bank.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpUtils {

    private static final String DEFAULT_CHARSET = "UTF-8";


    private static PoolingHttpClientConnectionManager connectionManager;

    private static HttpClient client;

    private HttpUtils() {
    }

    static {
        try {
            SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build();
            SSLConnectionSocketFactory sslSf = new SSLConnectionSocketFactory(sslContext,
                    SSLConnectionSocketFactory.getDefaultHostnameVerifier());
            Registry<ConnectionSocketFactory> socketFactoryRegistry =
                    RegistryBuilder.<ConnectionSocketFactory>create().register("https", sslSf).register("http",
                            PlainConnectionSocketFactory.getSocketFactory()).build();
            connectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            connectionManager.setMaxTotal(50);
            connectionManager.setDefaultMaxPerRoute(25);
            client = getSSLHttpClient();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static CloseableHttpClient getHttpClient() {
        return HttpClients.custom().setConnectionManager(connectionManager).build();
    }

    public static CloseableHttpClient getSSLHttpClient() {
        SSLContext sslContext = null;
        try {
            sslContext = SSLContextBuilder.create().loadTrustMaterial(new TrustSelfSignedStrategy()).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        HostnameVerifier allowAllHosts = new NoopHostnameVerifier();
        SSLConnectionSocketFactory connectionFactory = new SSLConnectionSocketFactory(sslContext, allowAllHosts);
        return HttpClients.custom().setSSLSocketFactory(connectionFactory).build();
    }

    public static String get(String uri, Map<String, String> headers, Map<String, Object> data) {
        HttpClient client = getHttpClient();
        List<NameValuePair> params = new ArrayList<>();
        if (data != null) {
            for(Map.Entry entry : data.entrySet()) {
                params.add(new BasicNameValuePair(entry.getKey() + "", entry.getValue() + ""));
            }
        }
        try {
            String str = EntityUtils.toString(new UrlEncodedFormEntity(params, DEFAULT_CHARSET));
            HttpGet get = new HttpGet(uri + "?" + str);
            if(headers != null) {
                for(Map.Entry<String, String> entry : headers.entrySet()) {
                    get.addHeader(entry.getKey(), entry.getValue());
                }
            }
            HttpResponse response = client.execute(get);
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity, "UTF-8").trim();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String postForm(String uri, Map<String, String> headers, Map<String, Object> data) {
        HttpClient client = getHttpClient();
        HttpPost post = new HttpPost(uri);
        List<NameValuePair> form = new ArrayList<>();
        for(Map.Entry entry : data.entrySet()) {
            form.add(new BasicNameValuePair(entry.getKey() + "", entry.getValue() + ""));
        }
        if(headers != null) {
            for(Map.Entry<String, String> entry : headers.entrySet()) {
                post.addHeader(entry.getKey(), entry.getValue());
            }
        }
        try {
            post.setEntity(new UrlEncodedFormEntity(form, DEFAULT_CHARSET));
            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity, "UTF-8").trim();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String postData(String uri, Map<String, String> headers, String content) {
        HttpClient client = getHttpClient();
        HttpPost post = new HttpPost(uri);
        post.setEntity(new StringEntity(content, DEFAULT_CHARSET));
        if(headers != null) {
            for(Map.Entry<String, String> entry : headers.entrySet()) {
                post.addHeader(entry.getKey(), entry.getValue());
            }
        }
        return postCommon(client, post);
    }

    public static String postSSLData(String uri, Map<String, String> headers, String content) {
        HttpPost post = new HttpPost(uri);
        post.setEntity(new StringEntity(content, DEFAULT_CHARSET));
        if(headers != null) {
            for(Map.Entry<String, String> entry : headers.entrySet()) {
                post.addHeader(entry.getKey(), entry.getValue());
            }
        }
        return postCommon(client, post);
    }

    private static String postCommon(HttpClient client, HttpPost post) {
        try {
            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity, "UTF-8").trim();
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
