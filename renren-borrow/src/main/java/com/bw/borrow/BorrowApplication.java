package com.bw.borrow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class BorrowApplication {
    public static void main(String[] args) {
        SpringApplication.run(BorrowApplication.class);
    }
}
