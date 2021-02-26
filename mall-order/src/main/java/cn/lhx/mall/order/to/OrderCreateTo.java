package cn.lhx.mall.order.to;

import cn.lhx.mall.order.entity.OrderEntity;
import cn.lhx.mall.order.entity.OrderItemEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author lee549
 * @date 2020/12/22 13:57
 */
@Data
public class OrderCreateTo {

    private OrderEntity order;

    private List<OrderItemEntity> orderItems;

    private BigDecimal payPrice;//订单计算的应付价格

    private BigDecimal fare;//运费
}
