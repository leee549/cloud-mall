package cn.lhx.mall.product.dao;

import cn.lhx.mall.product.entity.SpuInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * spu信息
 *
 * @author lhx
 * @email 1193106371@qq.com
 * @date 2020-08-30 16:31:48
 */
@Mapper
public interface SpuInfoDao extends BaseMapper<SpuInfoEntity> {

    void updateSpuState(@Param("spuId") Long spuId, @Param("code") int code);
}
