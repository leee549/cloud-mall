package cn.lhx.common.to.mq;

import lombok.Data;

import java.util.List;

/**
 * @author lee549
 * @date 2021/2/25 15:38
 */
@Data
public class StockLockedTo {
    private Long id;    //库存工作单Id
    private StockDetailTo detail; //工资单详情的ID
}
