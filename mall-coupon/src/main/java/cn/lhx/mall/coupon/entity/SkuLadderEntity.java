package cn.lhx.mall.coupon.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 商品阶梯价格
 * 
 * @author lhx
 * @email 1193106371@qq.com
 * @date 2020-08-31 18:05:32
 */
@Data
@TableName("sms_sku_ladder")
public class SkuLadderEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * $column.comments
	 */
	@TableId
	private Long id;
	/**
	 * $column.comments
	 */
	private Long skuId;
	/**
	 * $column.comments
	 */
	private Integer fullCount;
	/**
	 * $column.comments
	 */
	private BigDecimal discount;
	/**
	 * $column.comments
	 */
	private BigDecimal price;
	/**
	 * $column.comments
	 */
	private Integer addOther;

}
