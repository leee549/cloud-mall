package cn.lhx.mall.member.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 会员收货地址
 * 
 * @author lhx
 * @email 1193106371@qq.com
 * @date 2020-08-31 18:09:24
 */
@Data
@TableName("ums_member_receive_address")
public class MemberReceiveAddressEntity implements Serializable {
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
	private String name;
	/**
	 * $column.comments
	 */
	private String phone;
	/**
	 * $column.comments
	 */
	private String postCode;
	/**
	 * $column.comments
	 */
	private String province;
	/**
	 * $column.comments
	 */
	private String city;
	/**
	 * $column.comments
	 */
	private String region;
	/**
	 * $column.comments
	 */
	private String detailAddress;
	/**
	 * $column.comments
	 */
	private String areacode;
	/**
	 * $column.comments
	 */
	private Integer defaultStatus;

}
