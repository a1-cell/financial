package io.renren.modules.check;

//import org.redisson.Redisson;
//import org.redisson.api.RedissonClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
@ComponentScan("io.renren")
public class CheckApplication {
    public static void main(String[] args) {
        SpringApplication.run(CheckApplication.class);
    }
//    @Bean
//    RedissonClient init(){
//        return Redisson.create();
//    }
}
