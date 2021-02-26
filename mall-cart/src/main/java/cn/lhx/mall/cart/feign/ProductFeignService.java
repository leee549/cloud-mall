package cn.lhx.mall.cart.feign;

import cn.lhx.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author lee549
 * @date 2020/11/21 19:58
 */
@FeignClient("mall-product")
public interface ProductFeignService {

    @RequestMapping("/product/skuinfo/info/{skuId}")
    R getSkuInfo(@PathVariable("skuId") Long skuId);

    /**
     * 根据skuId获取销售属性组合
     * @param skuId
     * @return
     */
    @GetMapping("/product/skusaleattrvalue/stringlist/{skuId}")
    List<String> skuSaleAttrValues(@PathVariable("skuId") Long skuId);

    /**
     * 获取当前sku的最新价格
     * @param skuId
     * @return
     */
    @GetMapping("/product/skuinfo/{skuId}/price")
    R getSkuPrice(@PathVariable("skuId") Long skuId);

}
