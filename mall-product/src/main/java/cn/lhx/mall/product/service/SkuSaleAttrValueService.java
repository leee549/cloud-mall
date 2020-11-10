package cn.lhx.mall.product.service;

import cn.lhx.mall.product.vo.SkuItemVo;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.lhx.common.utils.PageUtils;
import cn.lhx.mall.product.entity.SkuSaleAttrValueEntity;

import java.util.List;
import java.util.Map;

/**
 * sku销售属性&值
 *
 * @author lhx
 * @email 1193106371@qq.com
 * @date 2020-08-30 16:31:48
 */
public interface SkuSaleAttrValueService extends IService<SkuSaleAttrValueEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<SkuItemVo.SkuItemSaleAttrVo> getSaleAttrsBySpuId(Long spuId);
}

