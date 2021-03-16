package cn.lhx.mall.order.feign;

import cn.lhx.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author lee549
 * @date 2020/12/22 14:50
 */
@FeignClient("mall-product")
public interface ProductFeignService {

    @GetMapping("/product/spuinfo/skuId/{id}")
    R getSpuInfoBySkuId(@PathVariable("id") Long skuId);

    @GetMapping("/product/skusaleattrvalue/stringlist/{skuId}")
    List<String> skuSaleAttrValues(@PathVariable("skuId") Long skuId);

    @RequestMapping("/product/skuinfo/info/{skuId}")
    //@RequiresPermissions("product:skuinfo:info")
    R getSkuInfoBySkuId(@PathVariable("skuId") Long skuId);

}
