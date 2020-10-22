package cn.lhx.mall.search.feign;

import cn.lhx.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author lee549
 * @date 2020/10/21 15:35
 */
@FeignClient("mall-product")
public interface ProductFeignService {

    @GetMapping("/product/attr/info/{attrId}")
    R attrInfo(@PathVariable("attrId") Long attrId);

    @RequestMapping("/product/brand/infos")
    R brandsInfos(@RequestParam("brandIds") List<Long> brandIds);

    @RequestMapping("/product/category/infos")
    R catalogInfos(@RequestParam("catId") Long catId);
}
