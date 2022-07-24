package com.renren;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.renren.mapper")
public class ProductApplication9105 {
    public static void main(String[] args) {
        SpringApplication.run(ProductApplication9105.class);
    }
}
