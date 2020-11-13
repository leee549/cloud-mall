package cn.lhx.mall.product.vo;

import lombok.Data;

import java.util.List;

/**
 * @author lee549
 * @date 2020/11/12 8:21
 */
@Data
public class SkuItemSaleAttrVo {
    private Long attrId;
    private String attrName;
    private List<AttrValueWithSkuIdVo> attrValues;
}
