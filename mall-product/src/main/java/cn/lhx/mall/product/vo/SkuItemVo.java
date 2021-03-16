package cn.lhx.mall.product.vo;

import cn.lhx.mall.product.entity.SkuImagesEntity;
import cn.lhx.mall.product.entity.SkuInfoEntity;
import cn.lhx.mall.product.entity.SpuInfoDescEntity;
import lombok.Data;

import java.util.List;

/**
 * @author lee549
 * @date 2020/10/30 15:28
 */
@Data
public class SkuItemVo {

    //1sku基本信息 pms_sku_info
    private SkuInfoEntity info;
    //2sku图片信息 sku_images
    private List<SkuImagesEntity> images;
    //3 spu的销售属性组合
    private List<SkuItemSaleAttrVo> saleAttr;
    //4 spu 的介绍
    private SpuInfoDescEntity desc;
    //5 spu的规格参数信息
    private List<SpuItemAttrGroupVo> groupAttr;

    //6.秒杀信息
    private SeckillInfoVo seckillInfoVo;


    @Data
    public static class SpuItemAttrGroupVo {
        private String groupName;
        private List<SpuBaseAttrVo> attrs;

    }

    @Data
    public static class SpuBaseAttrVo {
        // private Long attrId;
        private String attrName;
        private String attrValue;
    }
}
