package cn.lhx.mall.ware.vo;

import lombok.Data;

import java.util.List;

/**
 * @author lee549
 * @date 2020/10/3 22:19
 */
@Data
public class MergeVo {
    // items: [1, 2]
    // purchaseId: 10
    private Long purchaseId;

    private List<Long> items;

}
