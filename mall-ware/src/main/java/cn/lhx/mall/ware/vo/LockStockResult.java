package cn.lhx.mall.ware.vo;

import lombok.Data;

/**
 * @author lee549
 * @date 2021/1/14 15:26
 */
@Data
public class LockStockResult {

    private Long skuId;
    private Integer num;
    private Boolean locked;
}
