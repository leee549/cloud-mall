package cn.lhx.mall.ware.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author lee549
 * @date 2020/12/12 11:50
 */
@Data
public class FareVo {
    private BigDecimal fare;
    private MemberReceiveAddressVo address;
}
