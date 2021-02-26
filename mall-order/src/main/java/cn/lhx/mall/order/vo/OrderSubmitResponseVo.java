package cn.lhx.mall.order.vo;

import cn.lhx.mall.order.entity.OrderEntity;
import lombok.Data;

/**
 * @author lee549
 * @date 2020/12/22 13:29
 */
@Data
public class OrderSubmitResponseVo {

    private OrderEntity order;

    private Integer code; //错误状态码 0为成功
}
