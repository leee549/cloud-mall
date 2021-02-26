package cn.lhx.mall.ware.feign;

import cn.lhx.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author lee549
 * @date 2021/2/25 17:02
 */
@FeignClient("mall-order")
public interface OrderFeignService {

    @GetMapping("/order/order/status/{orderSn}")
    public R getOrderStatus(@PathVariable("orderSn") String orderSn);
}
