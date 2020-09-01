package cn.lhx.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.lhx.common.utils.PageUtils;
import cn.lhx.mall.product.entity.BrandEntity;

import java.util.Map;

/**
 * 品牌
 *
 * @author lhx
 * @email 1193106371@qq.com
 * @date 2020-08-30 15:39:44
 */
public interface BrandService extends IService<BrandEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

