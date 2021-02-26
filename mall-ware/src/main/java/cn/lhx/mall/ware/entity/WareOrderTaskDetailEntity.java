package cn.lhx.mall.ware.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 库存工作单
 *
 * @author lhx
 * @email 1193106371@qq.com
 * @date 2020-08-31 17:59:27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("wms_ware_order_task_detail")
public class WareOrderTaskDetailEntity implements Serializable {
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
	private String skuName;
	/**
	 * $column.comments
	 */
	private Integer skuNum;
	/**
	 * $column.comments
	 */
	private Long taskId;

	private Long wareId;

	private Integer lockStatus;

}
