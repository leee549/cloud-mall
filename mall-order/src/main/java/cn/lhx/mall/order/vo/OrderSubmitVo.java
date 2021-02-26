package cn.lhx.mall.order.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 封装订单提交数据
 * @author lee549
 * @date 2020/12/22 11:03
 */
@Data
public class OrderSubmitVo {

    private Long addrId; //收获地址ID

    private Integer payType;  //支付方式

    //无需提交要购买的商品，直接查询购物车即可
    //优惠 发票 等
    //放重复令牌
    private String orderToken;

    //应付金额
    private BigDecimal payPrice;

    private String note; //订单备注

    //用户信息,直接去session中获取
}
