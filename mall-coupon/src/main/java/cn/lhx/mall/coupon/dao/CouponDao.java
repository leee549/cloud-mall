package cn.lhx.mall.coupon.dao;

import cn.lhx.mall.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author lhx
 * @email 1193106371@qq.com
 * @date 2020-08-31 18:05:32
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
