package cn.lhx.mall.product.vo;

import cn.lhx.mall.product.entity.AttrEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.List;

/**
 * @author lee549
 * @date 2020/9/28 16:28
 */
@Data
public class AttrGroupWithAttrsVo {

    private Long attrGroupId;

    private String attrGroupName;

    private Integer sort;

    private String descript;

    private String icon;

    private Long catelogId;

    private List<AttrEntity> attrs;
}
