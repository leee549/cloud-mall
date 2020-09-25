package cn.lhx.mall.product.service;

import cn.lhx.common.utils.PageUtils;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.lhx.mall.product.entity.CategoryBrandRelationEntity;

import java.util.Map;

/**
 *
 *
 * @author lhx
 * @email 1193106371@gmail.com
 * @date 2020-09-25 12:08:15
 */
public interface CategoryBrandRelationService extends IService<CategoryBrandRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveDetail(CategoryBrandRelationEntity categoryBrandRelation);

    /**
     * 更新关联表中品牌信息
     * @param brandId
     * @param name
     */
    void updateBrand(Long brandId, String name);

    /**
     * 更新关联表中的分类信息
     * @param catId
     * @param name
     */
    void updateCategory(Long catId, String name);
}

