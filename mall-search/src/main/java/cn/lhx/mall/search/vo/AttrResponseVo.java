package cn.lhx.mall.search.vo;

import lombok.Data;

/**
 * @author lee549
 * @date 2020/10/21 15:53
 */
@Data
public class AttrResponseVo {


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

    /**
     * 分类名字
     */
    private String catelogName;
    /**
     * 分组名字
     */
    private String groupName;

    /**
     *
     */
    private Long[] catelogPath;
}
