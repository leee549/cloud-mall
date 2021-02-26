package cn.lhx.mall.order.feign;

import cn.lhx.mall.order.vo.OrderItemVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author lee549
 * @date 2020/12/7 13:51
 */
@FeignClient("mall-cart")
public interface CartFeignService {

    @GetMapping("/currentUserCartItems")
    List<OrderItemVo> getCurrentUserCartItems();

}
