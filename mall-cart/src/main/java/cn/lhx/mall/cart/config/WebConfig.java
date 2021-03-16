package cn.lhx.mall.cart.config;

import cn.lhx.mall.cart.interceptor.CartInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author lee549
 * @date 2020/11/20 22:36
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CartInterceptor()).addPathPatterns("/**");
    }
    // @Override
    // public void addCorsMappings(CorsRegistry registry) {
    //
    //     registry.addMapping("/**")
    //
    //             .allowedOrigins("*")
    //
    //             .allowCredentials(true)
    //
    //             .allowedMethods("GET", "POST", "DELETE", "PUT")
    //
    //             .maxAge(3600);
    //
    // }
}
