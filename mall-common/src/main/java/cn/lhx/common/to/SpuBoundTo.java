package cn.lhx.common.to;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author lee549
 * @date 2020/9/29 11:13
 */
@Data
public class SpuBoundTo {

    private Long spuId;
    private BigDecimal buyBounds;
    private BigDecimal growBounds;
}
