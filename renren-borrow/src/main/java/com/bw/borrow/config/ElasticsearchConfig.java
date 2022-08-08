package com.bw.borrow.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchConfig {
    @Value("${es.url}")
    private String url;

    @Bean
    public RestHighLevelClient restHighLevelClient(){
        String[] split = url.split(":");
    //    System.out.println(split);
        HttpHost httpHost = new HttpHost(split[0],Integer.parseInt(split[1]),"http");
        RestClientBuilder builder = RestClient.builder(httpHost);
        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(builder);
        return restHighLevelClient;
    }

}
