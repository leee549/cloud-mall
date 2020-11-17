package cn.lhx.mall.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @author lee549
 * @date 2020/11/13 15:16
 */
@EnableRedisHttpSession //整合redis作为session的存储
@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class MallAuthServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(MallAuthServerApplication.class, args);
    }
}
