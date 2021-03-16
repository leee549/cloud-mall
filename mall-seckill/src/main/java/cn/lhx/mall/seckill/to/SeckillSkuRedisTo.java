package cn.lhx.mall.seckill.to;

import cn.lhx.mall.seckill.vo.SkuInfoVo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author lee549
 * @date 2021/3/9 15:11
 */

@Data
public class SeckillSkuRedisTo {

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

    /**
     * sku详细信息
     */
    private SkuInfoVo skuInfo;

    //秒杀开始时间
    private Long startTime;

    private Long endTime;

    private String randomCode;
}
