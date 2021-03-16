package cn.lhx.mall.seckill.feign;

import cn.lhx.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author lee549
 * @date 2021/3/7 16:04
 */
@FeignClient("mall-coupon")
public interface CouponFeignService {

    @GetMapping("/coupon/seckillsession/future3DaysSession")
    R getFuture3DaysSession();

}
