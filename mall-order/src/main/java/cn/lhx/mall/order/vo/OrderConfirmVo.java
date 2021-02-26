package cn.lhx.mall.order.vo;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author lee549
 * @date 2020/12/6 10:45
 */

public class OrderConfirmVo {

    //收货地址
    List<MemberReceiveAddressVo> address;

    //所有选中的购物项-->CartItem
    List<OrderItemVo> items;

    //发票记录。。

    //优惠券 积分信息
    private Integer integration;

    //防重复令牌
    private String orderToken;

    //订单总额
    // private BigDecimal total;

    //库存信息
    private Map<Long,Boolean> stocks;



    //应付总额
    // private BigDecimal payPrice;
    public Integer getCount(){
        Integer i = 0;
        if (items!=null){
            for (OrderItemVo item : items) {
               i+=item.getCount();
            }
        }
        return i;
    }


    public BigDecimal getTotal() {
        BigDecimal sum= new BigDecimal("0");
        if (items!=null){
            for (OrderItemVo item : items) {
                BigDecimal multiply = item.getPrice().multiply(new BigDecimal(item.getCount().toString()));
                sum=sum.add(multiply);
            }
        }
        return sum;
    }
    public Map<Long, Boolean> getStocks() {
        return stocks;
    }

    public void setStocks(Map<Long, Boolean> stocks) {
        this.stocks = stocks;
    }
    public BigDecimal getPayPrice() {
        return getTotal();
    }

    public String getOrderToken() {
        return orderToken;
    }

    public void setOrderToken(String orderToken) {
        this.orderToken = orderToken;
    }

    public List<MemberReceiveAddressVo> getAddress() {
        return address;
    }

    public void setAddress(List<MemberReceiveAddressVo> address) {
        this.address = address;
    }

    public List<OrderItemVo> getItems() {
        return items;
    }

    public void setItems(List<OrderItemVo> items) {
        this.items = items;
    }

    public Integer getIntegration() {
        return integration;
    }

    public void setIntegration(Integer integration) {
        this.integration = integration;
    }
}
