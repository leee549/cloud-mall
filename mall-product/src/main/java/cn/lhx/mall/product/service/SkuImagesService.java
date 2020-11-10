package cn.lhx.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.lhx.common.utils.PageUtils;
import cn.lhx.mall.product.entity.SkuImagesEntity;

import java.util.List;
import java.util.Map;

/**
 * sku图片
 *
 * @author lhx
 * @email 1193106371@qq.com
 * @date 2020-08-30 16:31:48
 */
public interface SkuImagesService extends IService<SkuImagesEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<SkuImagesEntity> getImagesBySkuId(Long skuId);
}

