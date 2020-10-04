package cn.lhx.mall.ware.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author lee549
 * @date 2020/10/4 10:15
 */
@Data
public class PurchaseDoneVo {
    @NotNull
    private Long id;//采购单Id

    private List<PurchaseItemDoneVo> items;
}
