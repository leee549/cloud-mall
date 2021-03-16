package cn.lhx.mall.member.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lee549
 * @date 2020/12/10 11:15
 */
@Configuration
public class MallFeignConfig {

    @Bean("requestInterceptor")
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor() {

            @Override
            public void apply(RequestTemplate requestTemplate) {
                //拿到刚进来的请求
                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                if (attributes != null) {
                    HttpServletRequest request = attributes.getRequest();//旧的请求
                    //同步丢失的cookie
                    String cookie = request.getHeader("Cookie");
                    //给新请求同步了cookie
                    requestTemplate.header("Cookie", cookie);
                }


            }
        };
    }
}
