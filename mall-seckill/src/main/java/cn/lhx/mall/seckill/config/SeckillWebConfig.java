package cn.lhx.mall.seckill.config;

import cn.lhx.mall.seckill.inteceptor.LoginUserInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author lee549
 * @date 2021/3/11 16:36
 */
@Configuration
public class SeckillWebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginUserInterceptor()).addPathPatterns("/**");
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
