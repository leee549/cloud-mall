package cn.lhx.mall.search.vo;

import lombok.Data;

import java.util.List;

/**
 * @author lee549
 * @date 2020/10/15 15:41
 */
@Data
public class SearchParam {
    /**
     * 查询关键字
     */
    private String keyword;

    /**
     * 三级分类Id
     */
    private String catalog3Id;

    /**
     * 排序字段
     */
    private String sort;

    /**
     * 是否只显示库存
     * 0 无库存  1有库存 默认查有库存
     */
    private Integer hasStock;

    /**
     * 价格区间
     */
    private String skuPrice;

    private List<Long> brandId;
    private List<String> attrs;
    private Integer pageNum=1;
}
