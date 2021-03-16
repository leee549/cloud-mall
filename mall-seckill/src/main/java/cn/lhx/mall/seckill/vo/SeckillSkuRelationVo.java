package cn.lhx.mall.seckill.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author lee549
 * @date 2021/3/8 9:52
 */
@Data
public class SeckillSkuRelationVo {

    private Long id;
    /**
     * $column.comments
     */
    private Long promotionId;
    /**
     * $column.comments
     */
    private Long promotionSessionId;
    /**
     * $column.comments
     */
    private Long skuId;
    /**
     * $column.comments
     */
    private BigDecimal seckillPrice;
    /**
     * $column.comments
     */
    private Integer seckillCount;
    /**
     * $column.comments
     */
    private Integer seckillLimit;
    /**
     * $column.comments
     */
    private Integer seckillSort;
}
