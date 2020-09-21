package cn.lhx.mall.product.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.lhx.common.utils.PageUtils;
import cn.lhx.common.utils.Query;

import cn.lhx.mall.product.dao.CategoryDao;
import cn.lhx.mall.product.entity.CategoryEntity;
import cn.lhx.mall.product.service.CategoryService;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        //1.查找所有分类
        List<CategoryEntity> entities = baseMapper.selectList(null);
        //2.组装父子分类
        //2.1 获取父分类
        List<CategoryEntity> level1Menus = entities.stream().filter(categoryEntity ->
                categoryEntity.getParentCid()==0)//从所有分类中过滤出id为0的父分类
                .map((menu)->{
                    //为父分类设置子类
                    menu.setChildren(getChildren(menu,entities));
                    return menu;
        }).sorted((menu1,menu2)->{
            //排序
            return (menu1.getSort()==null?0:menu1.getSort()) - (menu2.getSort()==null?0:menu2.getSort());
                })
                .collect(Collectors.toList());

        return level1Menus;
    }

    @Override
    public void removeMenuByIds(List<Long> asList) {
        //TODO 检查是否被其他地方引用
        //逻辑删除
        baseMapper.deleteBatchIds(asList);
    }

    private List<CategoryEntity> getChildren(CategoryEntity root,List<CategoryEntity> all){
        List<CategoryEntity> children = all.stream()
                //过滤出全部菜单中 父id 等于 分类id的菜单，即子菜单
                .filter(categoryEntity ->
             categoryEntity.getParentCid().equals(root.getCatId())
        ).map(categoryEntity -> {
            //找到子菜单
            categoryEntity.setChildren(getChildren(categoryEntity, all));
            return categoryEntity;
        }).sorted((menu1, menu2) -> {
            //排序
            return (menu1.getSort()==null?0:menu1.getSort()) - (menu2.getSort()==null?0:menu2.getSort());
        }).collect(Collectors.toList());
        return children;
    }

}
