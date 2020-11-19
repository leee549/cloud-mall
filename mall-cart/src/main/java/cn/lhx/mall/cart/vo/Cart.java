package cn.lhx.mall.cart.vo;

/**
 * @author lee549
 * @date 2020/11/19 17:24
 */

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 整个购物车
 */
@Data
public class Cart {

    private List<CartItem> items;

    private Integer countNum;   //商品数量

    private Integer countType;  //商品类型

    private BigDecimal totalAmount; //商品总价

    private BigDecimal reduce = new BigDecimal("0"); //减免价格

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public Integer getCountNum() {
        int count = 0;
        if (items != null && items.size() > 0) {
            for (CartItem item : items) {
                count += item.getCount();
            }
        }
        return count;
    }

    public Integer getCountType() {
        int count = 0;
        if (items != null && items.size() > 0) {
            for (CartItem item : items) {
                count += 1;
            }
        }
        return count;
    }

    public BigDecimal getTotalAmount() {
        BigDecimal amount = new BigDecimal("0");
        //计算购物总价
        if (items != null && items.size() > 0) {
            for (CartItem item : items) {
                BigDecimal totalPrice = item.getTotalPrice();
                amount = amount.add(totalPrice);
            }
        }
        //减去优惠总价
        return amount.subtract(getReduce());
    }


    public BigDecimal getReduce() {
        return reduce;
    }

    public void setReduce(BigDecimal reduce) {
        this.reduce = reduce;
    }
}
