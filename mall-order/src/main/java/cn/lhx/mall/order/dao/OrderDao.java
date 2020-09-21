package cn.lhx.mall.order.dao;

import cn.lhx.mall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author lhx
 * @email 1193106371@qq.com
 * @date 2020-08-31 18:10:48
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
