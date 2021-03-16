package cn.lhx.mall.seckill.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author lee549
 * @date 2021/3/8 9:51
 */
@Data
public class SeckillSessionVo {
    private Long id;
    /**
     * $column.comments
     */
    private String name;
    /**
     * $column.comments
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date startTime;
    /**
     * $column.comments
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date endTime;
    /**
     * $column.comments
     */
    private Integer status;
    /**
     * $column.comments
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

    @TableField(exist = false)
    private List<SeckillSkuRelationVo> relationSkus;
}
