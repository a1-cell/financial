package com.renren;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.renren.mapper")
@EnableFeignClients
public class ProductApplication9105 {
    public static void main(String[] args) {
        SpringApplication.run(ProductApplication9105.class);
    }
}
