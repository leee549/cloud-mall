package cn.lhx.mall.product.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author lee549
 * @date 2020/11/12 15:17
 */
@ConfigurationProperties(prefix = "mall.thread")
@Data
@Component
public class ThreadPoolConfigProperties {
    private Integer coreSize;
    private Integer maxSize;
    private Integer keepAliveTime;

}
