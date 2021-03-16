package cn.lhx.common.to.mq;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author lee549
 * @date 2021/3/12 14:02
 */
@Data
public class SeckillOrderTo {

    private String orderSn;

    private Long memberId;

    private String username;

    private Long promotionSessionId;

    private Long skuId;

    private BigDecimal seckillPrice;

    private Integer num;

}
