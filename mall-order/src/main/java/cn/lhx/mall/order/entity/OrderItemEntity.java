package cn.lhx.mall.order.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 订单项信息
 * 
 * @author lhx
 * @email 1193106371@qq.com
 * @date 2020-08-31 18:10:48
 */
@Data
@TableName("oms_order_item")
public class OrderItemEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * $column.comments
	 */
	@TableId
	private Long id;
	/**
	 * $column.comments
	 */
	private Long orderId;
	/**
	 * $column.comments
	 */
	private String orderSn;
	/**
	 * $column.comments
	 */
	private Long spuId;
	/**
	 * $column.comments
	 */
	private String spuName;
	/**
	 * $column.comments
	 */
	private String spuPic;
	/**
	 * $column.comments
	 */
	private String spuBrand;
	/**
	 * $column.comments
	 */
	private Long categoryId;
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
	private String skuPic;
	/**
	 * $column.comments
	 */
	private BigDecimal skuPrice;
	/**
	 * $column.comments
	 */
	private Integer skuQuantity;
	/**
	 * $column.comments
	 */
	private String skuAttrsVals;
	/**
	 * $column.comments
	 */
	private BigDecimal promotionAmount;
	/**
	 * $column.comments
	 */
	private BigDecimal couponAmount;
	/**
	 * $column.comments
	 */
	private BigDecimal integrationAmount;
	/**
	 * $column.comments
	 */
	private BigDecimal realAmount;
	/**
	 * $column.comments
	 */
	private Integer giftIntegration;
	/**
	 * $column.comments
	 */
	private Integer giftGrowth;

}
