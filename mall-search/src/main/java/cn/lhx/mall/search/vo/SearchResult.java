package cn.lhx.mall.search.vo;

import cn.lhx.common.to.es.SkuEsModel;
import lombok.Data;

import java.util.List;

/**
 * @author lee549
 * @date 2020/10/15 16:31
 */
@Data
public class SearchResult {
    private List<SkuEsModel> products;
    private Integer pageNum;
    private Long total;
    private Integer totalPages;

    private List<BrandVo> brands;   //当前查询到的结果 涉及到的所有品牌

    private List<AttrVo> attrs;     //当前查询到的结果 设计到的所有属性

    private List<CatalogVo> catalogs; //当前查询到的结果 设计到的所有分类


    @Data
    public static class BrandVo {
        private Long brandId;

        private String brandName;

        private String brandImg;
    }

    @Data
    public static class AttrVo {
        private Long attrId;
        private String attrName;
        private List<String> attrValue;
    }

    @Data
    public static class CatalogVo {
        private Long catalogId;
        private String catalogName;
    }
}
