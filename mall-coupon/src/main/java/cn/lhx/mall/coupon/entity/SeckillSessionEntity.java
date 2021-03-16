package cn.lhx.mall.coupon.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.awt.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 秒杀活动场次
 *
 * @author lhx
 * @email 1193106371@qq.com
 * @date 2020-08-31 18:05:32
 */
@Data
@TableName("sms_seckill_session")
public class SeckillSessionEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * $column.comments
	 */
	@TableId
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
	private List<SeckillSkuRelationEntity> relationSkus;

}
