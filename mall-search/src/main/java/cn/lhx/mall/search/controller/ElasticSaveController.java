package cn.lhx.mall.search.controller;

import cn.lhx.common.exception.BizCodeEnum;
import cn.lhx.common.to.es.SkuEsModel;
import cn.lhx.common.utils.R;
import cn.lhx.mall.search.service.ProductSaveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * @author lee549
 * @date 2020/10/7 20:18
 */
@RestController
@RequestMapping("/search/save")
@Slf4j
public class ElasticSaveController {
    @Resource
    private ProductSaveService productSaveService;

    /**
     * 上架商品
     */
    @PostMapping("/product")
    public R productStatusUp(@RequestBody List<SkuEsModel> skuEsModels) {
        boolean b = false;
        try {
             b = productSaveService.productSaveService(skuEsModels);
        }catch (Exception e){
            log.error("es上架商品错误");
            return R.error(BizCodeEnum.PRODUCT_UP_EXCEPTION.code,BizCodeEnum.PRODUCT_UP_EXCEPTION.msg);
        }
        if (!b){
            return R.ok();
        }else {
            return R.error(BizCodeEnum.PRODUCT_UP_EXCEPTION.code,BizCodeEnum.PRODUCT_UP_EXCEPTION.msg);
        }
    }
}
