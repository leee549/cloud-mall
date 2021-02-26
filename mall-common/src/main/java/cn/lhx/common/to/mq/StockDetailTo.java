package cn.lhx.common.to.mq;

import lombok.Data;

/**
 * @author lee549
 * @date 2021/2/25 15:46
 */
@Data
public class StockDetailTo {

    private Long id;
    /**
     * $column.comments
     */
    private Long skuId;
    /**
     * $column.comments
     */
    private String skuName;
    /**
     * $column.comments
     */
    private Integer skuNum;
    /**
     * $column.comments
     */
    private Long taskId;

    private Long wareId;

    private Integer lockStatus;
}
