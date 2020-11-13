package cn.lhx.mall.product.web;

import cn.lhx.mall.product.service.SkuInfoService;
import cn.lhx.mall.product.vo.SkuItemVo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;

/**
 * @author lee549
 * @date 2020/10/29 10:18
 */
@Controller
public class ItemController {
    @Resource
    private SkuInfoService skuInfoService;

    @GetMapping("/{skuId}.html")
    public String skuItem(@PathVariable("skuId") Long skuId, Model model) throws ExecutionException, InterruptedException {
        SkuItemVo vo=skuInfoService.itemInfo(skuId);
        model.addAttribute("item",vo);
        return "item";
    }
}
