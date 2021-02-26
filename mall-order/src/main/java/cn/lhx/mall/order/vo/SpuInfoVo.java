package cn.lhx.mall.order.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author lee549
 * @date 2020/12/22 14:52
 */
@Data
public class SpuInfoVo {
    private Long id;
    /**
     * $column.comments
     */
    private String spuName;
    /**
     * $column.comments
     */
    private String spuDescription;
    /**
     * $column.comments
     */
    private Long catalogId;
    /**
     * $column.comments
     */
    private Long brandId;
    /**
     * $column.comments
     */
    private BigDecimal weight;
    /**
     * $column.comments
     */
    private Integer publishStatus;
    /**
     * $column.comments
     */
    private Date createTime;
    /**
     * $column.comments
     */
    private Date updateTime;
}
