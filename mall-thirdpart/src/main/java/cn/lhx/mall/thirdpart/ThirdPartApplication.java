package cn.lhx.mall.thirdpart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author lee549
 * @date 2020/9/12 22:14
 */
@EnableDiscoveryClient
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class ThirdPartApplication {
    public static void main(String[] args) {
        SpringApplication.run(ThirdPartApplication.class, args);
    }
}
