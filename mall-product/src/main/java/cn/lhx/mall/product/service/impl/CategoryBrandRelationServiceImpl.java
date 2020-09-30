package cn.lhx.mall.product.service.impl;

import cn.lhx.common.utils.PageUtils;
import cn.lhx.common.utils.Query;
import cn.lhx.mall.product.dao.BrandDao;
import cn.lhx.mall.product.dao.CategoryDao;
import cn.lhx.mall.product.entity.BrandEntity;
import cn.lhx.mall.product.entity.CategoryEntity;
import cn.lhx.mall.product.service.BrandService;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


import cn.lhx.mall.product.dao.CategoryBrandRelationDao;
import cn.lhx.mall.product.entity.CategoryBrandRelationEntity;
import cn.lhx.mall.product.service.CategoryBrandRelationService;

import javax.annotation.Resource;


@Service("categoryBrandRelationService")
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity> implements CategoryBrandRelationService {

    @Resource
    private BrandDao brandDao;
    @Resource
    private CategoryDao categoryDao;

    @Resource
    private BrandService brandService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryBrandRelationEntity> page = this.page(
                new Query<CategoryBrandRelationEntity>().getPage(params),
                new QueryWrapper<CategoryBrandRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveDetail(CategoryBrandRelationEntity categoryBrandRelation) {
        //查询详细信息
        BrandEntity brandEntity = this.brandDao.selectById(categoryBrandRelation.getBrandId());
        CategoryEntity categoryEntity = this.categoryDao.selectById(categoryBrandRelation.getCatelogId());
        //装配信息
        categoryBrandRelation.setBrandName(brandEntity.getName());
        categoryBrandRelation.setCatelogName(categoryEntity.getName());
        this.save(categoryBrandRelation);

    }

    /**
     * 更新关联表中的品牌信息
     */
    @Override
    public void updateBrand(Long brandId, String name) {

        CategoryBrandRelationEntity categoryBrandRelationEntity = new CategoryBrandRelationEntity();
        categoryBrandRelationEntity.setBrandId(brandId);
        categoryBrandRelationEntity.setBrandName(name);
        this.update(categoryBrandRelationEntity,
                new UpdateWrapper<CategoryBrandRelationEntity>()
                        .lambda()
                        .eq(CategoryBrandRelationEntity::getBrandId, brandId)
        );
    }

    /**
     * 更新关联表中的分类信息
     */
    @Override
    public void updateCategory(Long catId, String name) {
        CategoryBrandRelationEntity categoryBrandRelationEntity = new CategoryBrandRelationEntity();
        categoryBrandRelationEntity.setCatelogId(catId);
        categoryBrandRelationEntity.setCatelogName(name);
        this.update(categoryBrandRelationEntity,
                new UpdateWrapper<CategoryBrandRelationEntity>()
                .lambda()
                .eq(CategoryBrandRelationEntity::getCatelogId, catId)
        );
    }

    @Override
    public List<BrandEntity> getBrandsByCatId(Long catId) {
        List<CategoryBrandRelationEntity> relationEntities = this.baseMapper.selectList(new QueryWrapper<CategoryBrandRelationEntity>().lambda().eq(CategoryBrandRelationEntity::getCatelogId, catId));
        List<BrandEntity> collect = relationEntities.stream().map((item) -> {
            Long brandId = item.getBrandId();
            BrandEntity byId = brandService.getById(brandId);
            return byId;
        }).collect(Collectors.toList());
        return collect;
    }

}
