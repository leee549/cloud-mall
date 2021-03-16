package cn.lhx.mall.member.feign;

import cn.lhx.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @author lee549
 * @date 2021/3/3 14:31
 */
@FeignClient("mall-order")
public interface OrderFeignService {

    @PostMapping("/order/order/listWithItem")
    R listWithItem(@RequestBody Map<String, Object> params);
}
