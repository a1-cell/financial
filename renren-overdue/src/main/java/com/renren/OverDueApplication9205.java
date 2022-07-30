package com.renren;

import io.renren.common.ocerdue.Overdue;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.renren.mapper")
public class OverDueApplication9205 {
    public static void main(String[] args) {
        SpringApplication.run(OverDueApplication9205.class);
    }
}
