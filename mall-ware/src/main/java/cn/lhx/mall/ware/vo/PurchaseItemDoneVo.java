package cn.lhx.mall.ware.vo;

import lombok.Data;

/**
 * @author lee549
 * @date 2020/10/4 10:17
 */
@Data
public class PurchaseItemDoneVo {

    private Long itemId;
    private Integer status;
    private String  reason;
}
