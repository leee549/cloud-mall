package cn.lhx.mall.product.service.impl;

import cn.lhx.mall.product.service.CategoryBrandRelationService;
import cn.lhx.mall.product.vo.Catelog2Vo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.lhx.common.utils.PageUtils;
import cn.lhx.common.utils.Query;

import cn.lhx.mall.product.dao.CategoryDao;
import cn.lhx.mall.product.entity.CategoryEntity;
import cn.lhx.mall.product.service.CategoryService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {
    @Resource
    private CategoryBrandRelationService categoryBrandRelationService;

    @Resource(name = "stringRedisTemplate")
    private StringRedisTemplate redisTemplate;

    @Resource
    private RedissonClient redisson;

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
                categoryEntity.getParentCid() == 0)//从所有分类中过滤出id为0的父分类
                .map((menu) -> {
                    //为父分类设置子类
                    menu.setChildren(getChildren(menu, entities));
                    return menu;
                }).sorted((menu1, menu2) -> {
                    //排序
                    return (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort());
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

    @Override
    public Long[] findCatelogPath(Long catelogId) {
        List<Long> list = new ArrayList<>();
        list.add(catelogId);//255
        CategoryEntity entity = this.getById(catelogId);
        //1
        while (entity.getParentCid() != 0) {
            list.add(entity.getParentCid());
            CategoryEntity byId = this.getById(entity.getParentCid());
            entity.setParentCid(byId.getParentCid());
        }
        Collections.reverse(list);

        return (Long[]) list.toArray(new Long[list.size()]);
    }

    /**
     * 级联更新所有关联的数据
     *
     *
     *
     * // 组合多种缓存操作
     *     @Caching(evict = {
     *             @CacheEvict(value = "category",key = "'getLv1Category'"),
     *             @CacheEvict(value = "category",key = "'getCatalogJson'")
     *     })
     *         @CacheEvict:    缓存失效模式
     *         @CachePut:  双写模式 如果返回值还是修改的对象则保存一份到缓存
     *
     *
     * @param category
     */
    @CacheEvict(value = "category",allEntries = true)
    @Override
    @Transactional(rollbackFor = {})
    public void updateCascade(CategoryEntity category) {
        this.updateById(category);
        categoryBrandRelationService.updateCategory(category.getCatId(), category.getName());

    }
    @Cacheable(value = {"category"},key = "#root.method.name",sync = true)
    @Override
    public List<CategoryEntity> getLv1Category() {
        return baseMapper.selectList(new LambdaQueryWrapper<CategoryEntity>().eq(CategoryEntity::getParentCid, 0));


    }
    @Cacheable(value = {"category"},key = "#root.method.name")
    @Override
    public Map<String, List<Catelog2Vo>> getCatalogJson() {
        // 优化，查询全部
        List<CategoryEntity> selectList = baseMapper.selectList(null);
        //1.查出所有1级分类
        List<CategoryEntity> level1Category = getParentCid(selectList, 0L);
        //2.封装数据
        Map<String, List<Catelog2Vo>> parentCid = level1Category.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
            //1 查到每个一级分类的二级分类id
            List<CategoryEntity> categoryEntities = getParentCid(selectList, v.getCatId());
            // 2 封装上面的数据
            List<Catelog2Vo> catelog2Vos = null;
            if (categoryEntities != null) {
                catelog2Vos = categoryEntities.stream().map(l2 -> {
                    Catelog2Vo catelog2Vo = new Catelog2Vo(v.getCatId().toString(), null, l2.getCatId().toString(), l2.getName());
                    //1.找当前2级下的三级分类
                    List<CategoryEntity> level3 = getParentCid(selectList, l2.getCatId());
                    if (level3 != null) {
                        List<Catelog2Vo.Catelog3Vo> collect = level3.stream().map(l3 -> {
                            //2封装数据
                            Catelog2Vo.Catelog3Vo catelog3Vo = new Catelog2Vo.Catelog3Vo(l2.getCatId().toString(), l3.getCatId().toString(), l3.getName());
                            return catelog3Vo;
                        }).collect(Collectors.toList());
                        catelog2Vo.setCatalog3List(collect);
                    }
                    return catelog2Vo;
                }).collect(Collectors.toList());
            }
            return catelog2Vos;
        }));
        return parentCid;
    }

    public Map<String, List<Catelog2Vo>> getCatalogJson2() {
        /**
         * 1.缓存穿透 ：缓存一个null
         * 2.缓存雪崩： 缓存时间加随机值
         * 3.缓存击穿： 加锁
         */
        String catalogJSON = redisTemplate.opsForValue().get("catalogJSON");
        if (StringUtils.isEmpty(catalogJSON)) {
            //查询数据库和 放入缓存必须是原子操作
            Map<String, List<Catelog2Vo>> catalogJsonFromDb = this.getCatalogJsonFromDb();
            //序列化
            String s = JSON.toJSONString(catalogJsonFromDb);
            redisTemplate.opsForValue().set("catalogJSON", s, 1, TimeUnit.DAYS);
            return catalogJsonFromDb;
        }
        //反序列化
        Map<String, List<Catelog2Vo>> result = JSON.parseObject(catalogJSON, new TypeReference<Map<String, List<Catelog2Vo>>>() {
        });
        return result;
    }

    /**
     * 缓存一致性问题：1 双写模式： 数据库修改后 修改缓存
     *               2 失效模式： 数据库修改后删除缓存 等待下一次查询添加缓存
     *
     *               最终解决方案：实时性要求不高的： 1缓存+过期时间
     *                                                2 加读写锁 （业务不关心脏数据时可以不加锁）
     *                            实时性要求高的： 直接去查数据库
     *
     * spring-cache 不足：
     *        1读模式：
     *            缓存穿透：查询null 缓存空数据， cache-null-values: true
     *            缓存击穿：大量并发进来同时查询一个正好过期的数据，解决:加锁。 默认是无锁的，sync=true（本地锁）
     *            缓存雪崩：大量的key同时过期。 加随机过期时间
     *        2写模式（缓存与数据库一致）：
     *             1）读写加锁
     *             2）引入canal 感知mysql的更新 从而更新redis
     *             3）读多写少 直接去查数据库
     *
     *      总结：
     *           常规数据 （读多写少，即时性，数据一致性要求不高）使用spring-cache 写模式（有过期时间足够了）
     *           特殊数据：特殊设计  加分布式锁 、canal
     *
     * @return
     */
    public Map<String, List<Catelog2Vo>> getCatalogJsonFromDbWithRedissonLock() {
        RLock lock = redisson.getLock("CatalogJson-lock");
        lock.lock();
        //查询数据库和 放入缓存必须是原子操作
        Map<String, List<Catelog2Vo>> catalogJsonFromDb;
        try {
            catalogJsonFromDb = this.getCatalogJsonFromDb();
        } finally {
            lock.unlock();
        }
        return catalogJsonFromDb;
    }

    public Map<String, List<Catelog2Vo>> getCatalogJsonFromDb() {
        // 优化，查询全部
        List<CategoryEntity> selectList = baseMapper.selectList(null);
        //1.查出所有1级分类
        List<CategoryEntity> level1Category = getParentCid(selectList, 0L);
        //2.封装数据
        Map<String, List<Catelog2Vo>> parentCid = level1Category.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
            //1 查到每个一级分类的二级分类id
            List<CategoryEntity> categoryEntities = getParentCid(selectList, v.getCatId());
            // 2 封装上面的数据
            List<Catelog2Vo> catelog2Vos = null;
            if (categoryEntities != null) {
                catelog2Vos = categoryEntities.stream().map(l2 -> {
                    Catelog2Vo catelog2Vo = new Catelog2Vo(v.getCatId().toString(), null, l2.getCatId().toString(), l2.getName());
                    //1.找当前2级下的三级分类
                    List<CategoryEntity> level3 = getParentCid(selectList, l2.getCatId());
                    if (level3 != null) {
                        List<Catelog2Vo.Catelog3Vo> collect = level3.stream().map(l3 -> {
                            //2封装数据
                            Catelog2Vo.Catelog3Vo catelog3Vo = new Catelog2Vo.Catelog3Vo(l2.getCatId().toString(), l3.getCatId().toString(), l3.getName());
                            return catelog3Vo;
                        }).collect(Collectors.toList());
                        catelog2Vo.setCatalog3List(collect);
                    }
                    return catelog2Vo;
                }).collect(Collectors.toList());
            }
            return catelog2Vos;
        }));
        return parentCid;
    }

    private List<CategoryEntity> getParentCid(List<CategoryEntity> selectList, Long parentCid) {
        List<CategoryEntity> collect = selectList.stream().filter(item -> item.getParentCid().equals(parentCid)).collect(Collectors.toList());
        return collect;
    }


    private List<CategoryEntity> getChildren(CategoryEntity root, List<CategoryEntity> all) {
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
                    return (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort());
                }).collect(Collectors.toList());
        return children;
    }

}
