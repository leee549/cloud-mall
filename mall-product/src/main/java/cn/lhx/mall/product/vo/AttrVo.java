package cn.lhx.mall.product.vo;

import cn.lhx.mall.product.entity.AttrEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 接收前端传来的值
 * @author lee549
 * @date 2020/9/25 22:23
 */
@Data
public class AttrVo extends AttrEntity {

    /**
     * $column.comments
     */
    private Long attrId;
    /**
     * $column.comments
     */
    private String attrName;
    /**
     * $column.comments
     */
    private Integer searchType;
    /**
     * $column.comments
     */
    private String icon;
    /**
     * $column.comments
     */
    private String valueSelect;
    /**
     * $column.comments
     */
    private Integer attrType;
    /**
     * $column.comments
     */
    private Long enable;
    /**
     * $column.comments
     */
    private Long catelogId;
    /**
     * $column.comments
     */
    private Integer showDesc;

    private Long attrGroupId;

    /**
     * 值状态
     */
    private Long valueType;

}
