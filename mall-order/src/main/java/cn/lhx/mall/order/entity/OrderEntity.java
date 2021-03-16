package cn.lhx.mall.order.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 订单
 *
 * @author lhx
 * @email 1193106371@qq.com
 * @date 2020-08-31 18:10:48
 */
@Data
@TableName("oms_order")
public class OrderEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * $column.comments
	 */
	@TableId
	private Long id;
	/**
	 * $column.comments
	 */
	private Long memberId;
	/**
	 * $column.comments
	 */
	private String orderSn;
	/**
	 * $column.comments
	 */
	private Long couponId;
	/**
	 * $column.comments
	 */
	private Date createTime;
	/**
	 * $column.comments
	 */
	private String memberUsername;
	/**
	 * $column.comments
	 */
	private BigDecimal totalAmount;
	/**
	 * $column.comments
	 */
	private BigDecimal payAmount;
	/**
	 * $column.comments
	 */
	private BigDecimal freightAmount;
	/**
	 * $column.comments
	 */
	private BigDecimal promotionAmount;
	/**
	 * $column.comments
	 */
	private BigDecimal integrationAmount;
	/**
	 * $column.comments
	 */
	private BigDecimal couponAmount;
	/**
	 * $column.comments
	 */
	private BigDecimal discountAmount;
	/**
	 * $column.comments
	 */
	private Integer payType;
	/**
	 * $column.comments
	 */
	private Integer sourceType;
	/**
	 * $column.comments
	 */
	private Integer status;
	/**
	 * $column.comments
	 */
	private String deliveryCompany;
	/**
	 * $column.comments
	 */
	private String deliverySn;
	/**
	 * $column.comments
	 */
	private Integer autoConfirmDay;
	/**
	 * $column.comments
	 */
	private Integer integration;
	/**
	 * $column.comments
	 */
	private Integer growth;
	/**
	 * $column.comments
	 */
	private Integer billType;
	/**
	 * $column.comments
	 */
	private String billHeader;
	/**
	 * $column.comments
	 */
	private String billContent;
	/**
	 * $column.comments
	 */
	private String billReceiverPhone;
	/**
	 * $column.comments
	 */
	private String billReceiverEmail;
	/**
	 * $column.comments
	 */
	private String receiverName;
	/**
	 * $column.comments
	 */
	private String receiverPhone;
	/**
	 * $column.comments
	 */
	private String receiverPostCode;
	/**
	 * $column.comments
	 */
	private String receiverProvince;
	/**
	 * $column.comments
	 */
	private String receiverCity;
	/**
	 * $column.comments
	 */
	private String receiverRegion;
	/**
	 * $column.comments
	 */
	private String receiverDetailAddress;
	/**
	 * $column.comments
	 */
	private String note;
	/**
	 * $column.comments
	 */
	private Integer confirmStatus;
	/**
	 * $column.comments
	 */
	private Integer deleteStatus;
	/**
	 * $column.comments
	 */
	private Integer useIntegration;
	/**
	 * $column.comments
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private Date paymentTime;
	/**
	 * $column.comments
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private Date deliveryTime;
	/**
	 * $column.comments
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private Date receiveTime;
	/**
	 * $column.comments
	 */


	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private Date commentTime;
	/**
	 * $column.comments
	 */

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private Date modifyTime;

	/**
	 * 订单项
	 */
	@TableField(exist = false)
	private List<OrderItemEntity> itemEntities;

}
