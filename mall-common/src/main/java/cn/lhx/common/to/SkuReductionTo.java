package cn.lhx.common.to;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author lee549
 * @date 2020/9/29 18:03
 */
@Data
public class SkuReductionTo {
    private Long skuId;
    private int fullCount;
    private BigDecimal discount;
    private int countStatus;
    private BigDecimal fullPrice;
    private BigDecimal reducePrice;
    private int priceStatus;
    private List<MemberPrice> memberPrice;
}
