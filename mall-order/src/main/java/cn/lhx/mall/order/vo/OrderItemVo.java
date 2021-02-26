package cn.lhx.mall.order.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author lee549
 * @date 2020/12/6 10:55
 */
@Data
public class OrderItemVo {

    private Long skuId;
    private String title;
    private String img;
    private List<String> skuAttr;
    private BigDecimal price;
    private Integer count;
    private BigDecimal totalPrice;
    private BigDecimal weight;

}
