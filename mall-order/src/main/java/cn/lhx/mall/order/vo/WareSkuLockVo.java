package cn.lhx.mall.order.vo;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import lombok.Data;

import java.util.List;

/**
 * @author lee549
 * @date 2021/1/14 14:48
 */
@Data
public class WareSkuLockVo {

    /**
     * 订单号
     */
    private String orderSn;

    /**
     * 需要锁住的所有库存信息
     */
    private List<OrderItemVo> locks;
}
