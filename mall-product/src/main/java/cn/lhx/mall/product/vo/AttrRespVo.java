package cn.lhx.mall.product.vo;

import lombok.Data;

/**
 * @author lee549
 * @date 2020/9/26 10:31
 */
@Data
public class AttrRespVo extends AttrVo {
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
