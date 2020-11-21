package cn.lhx.mall.cart.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author lee549
 * @date 2020/11/21 20:03
 */
@Data
public class SkuInfoVo {

    private Long skuId;
    /**
     * $column.comments
     */
    private Long spuId;
    /**
     * $column.comments
     */
    private String skuName;
    /**
     * $column.comments
     */
    private String skuDesc;
    /**
     * $column.comments
     */
    private Long catalogId;
    /**
     * $column.comments
     */
    private Long brandId;
    /**
     * $column.comments
     */
    private String skuDefaultImg;
    /**
     * $column.comments
     */
    private String skuTitle;
    /**
     * $column.comments
     */
    private String skuSubtitle;
    /**
     * $column.comments
     */
    private BigDecimal price;
    /**
     * $column.comments
     */
    private Long saleCount;
}
