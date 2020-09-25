package cn.lhx.mall.product.service.impl;

import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.lhx.common.utils.PageUtils;
import cn.lhx.common.utils.Query;

import cn.lhx.mall.product.dao.BrandDao;
import cn.lhx.mall.product.entity.BrandEntity;
import cn.lhx.mall.product.service.BrandService;
import org.springframework.util.StringUtils;


@Service("brandService")
public class BrandServiceImpl extends ServiceImpl<BrandDao, BrandEntity> implements BrandService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String key = (String) params.get("key");
        QueryWrapper<BrandEntity> qw = new QueryWrapper<>();
        if (!StringUtils.isEmpty(key)) {
            qw.lambda().eq(BrandEntity::getBrandId, key)
                    .or()
                    .like(BrandEntity::getName, key);
        }
        IPage<BrandEntity> page = this.page(
                new Query<BrandEntity>().getPage(params), qw

        );

        return new PageUtils(page);
    }

}
