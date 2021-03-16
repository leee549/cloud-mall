package cn.lhx.mall.product.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author lee549
 * @date 2021/3/10 15:45
 */
@Data
public class SeckillInfoVo {
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
    private BigDecimal seckillLimit;
    /**
     * $column.comments
     */
    private Integer seckillSort;
    //秒杀开始时间
    private Long startTime;

    private Long endTime;

    private String randomCode;
}
