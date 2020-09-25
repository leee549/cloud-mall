package cn.lhx.mall.product.entity;

import cn.lhx.common.valid.ListValue;
import cn.lhx.common.valid.AddGroup;
import cn.lhx.common.valid.UpdateGroup;
import cn.lhx.common.valid.UpdateStatus;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;

/**
 * 品牌
 *
 * @author lhx
 * @email 1193106371@qq.com
 * @date 2020-08-30 16:31:48
 */
@Data
@TableName("pms_brand")
public class BrandEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * $column.comments
	 */
	@TableId
	@Null(message = "新增id必须为空",groups ={AddGroup.class})
	@NotNull(message = "id不能为空",groups ={UpdateGroup.class} )
	private Long brandId;
	/**
	 * $column.comments
	 */
	@NotBlank(message = "品牌名不能为空",groups = {AddGroup.class, UpdateGroup.class})
	private String name;
	/**
	 * $column.comments
	 */
	@NotBlank(groups = {AddGroup.class})
	@URL(message = "logo必须为合法的URL地址",groups = {AddGroup.class, UpdateGroup.class})
	private String logo;
	/**
	 * $column.comments
	 */
	private String descript;
	/**
	 * $column.comments
	 */
	@NotNull(groups = {AddGroup.class, UpdateStatus.class})
	@ListValue(value = {0,1},groups = {AddGroup.class, UpdateStatus.class})
	private Integer showStatus;
	/**
	 * $column.comments
	 */
	@NotEmpty(groups = {AddGroup.class})
	@Pattern(regexp = "^[a-zA-Z]$",message = "检索首字母必须为一个字母",groups = {AddGroup.class, UpdateGroup.class})
	private String firstLetter;
	/**
	 * $column.comments
	 */
	@NotNull(groups = {AddGroup.class})
	@Min(value = 0,message = "排序必须大于等于0",groups = {AddGroup.class, UpdateGroup.class})
	private Integer sort;

}
