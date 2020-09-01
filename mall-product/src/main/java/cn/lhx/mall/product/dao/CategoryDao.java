package cn.lhx.mall.product.dao;

import cn.lhx.mall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author lhx
 * @email 1193106371@qq.com
 * @date 2020-08-30 16:31:48
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
