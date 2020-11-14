package cn.lhx.mall.auth.feign;

import cn.lhx.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author lee549
 * @date 2020/11/14 17:59
 */
@FeignClient("mall-thirdpart")
public interface ThirdPartFeignService {

    @GetMapping("/sms/sendcode")
    R SendCode(@RequestParam("phone") String phone, @RequestParam("code") String code );
}
