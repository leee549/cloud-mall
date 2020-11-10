package cn.lhx.mall.product.service.impl;

import cn.lhx.mall.product.entity.AttrAttrgroupRelationEntity;
import cn.lhx.mall.product.entity.AttrEntity;
import cn.lhx.mall.product.entity.ProductAttrValueEntity;
import cn.lhx.mall.product.service.AttrAttrgroupRelationService;
import cn.lhx.mall.product.service.AttrService;
import cn.lhx.mall.product.service.ProductAttrValueService;
import cn.lhx.mall.product.vo.AttrGroupWithAttrsVo;
import cn.lhx.mall.product.vo.SkuItemVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.lhx.common.utils.PageUtils;
import cn.lhx.common.utils.Query;

import cn.lhx.mall.product.dao.AttrGroupDao;
import cn.lhx.mall.product.entity.AttrGroupEntity;
import cn.lhx.mall.product.service.AttrGroupService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import sun.plugin.dom.core.Attr;

import javax.annotation.Resource;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {
    @Resource
    private AttrService attrService;
    @Resource
    private ProductAttrValueService productAttrValueService;
    @Resource
    private AttrAttrgroupRelationService attrgroupRelationService;
    @Resource
    private AttrGroupService attrGroupService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Long catelogId) {
        String key = (String) params.get("key");
        QueryWrapper<AttrGroupEntity> qw = new QueryWrapper<AttrGroupEntity>();
        if (!StringUtils.isEmpty(key)) {
            qw.and((obj) -> {
                obj.eq("attr_group_id", key).or().like("attr_group_name", key);
            });
        }
        if (catelogId == 0) {
            IPage<AttrGroupEntity> page = this.page(
                    new Query<AttrGroupEntity>().getPage(params),
                    qw);
            return new PageUtils(page);
        } else {
            qw.lambda().eq(AttrGroupEntity::getCatelogId, catelogId);
            IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(params), qw);
            return new PageUtils(page);
        }

    }

    /**
     * 根据分类ID查出所有分组 和分组中的属性
     *
     * @param catelogId
     * @return
     */
    @Override
    public List<AttrGroupWithAttrsVo> getAttrGroupWithAttrsByCatelogId(Long catelogId) {
        //1.查询分组
        List<AttrGroupEntity> attrGroupEntities = this.list(new QueryWrapper<AttrGroupEntity>().lambda().eq(AttrGroupEntity::getCatelogId, catelogId));
        List<AttrGroupWithAttrsVo> collect = attrGroupEntities.stream().map(group -> {
            AttrGroupWithAttrsVo vo = new AttrGroupWithAttrsVo();
            BeanUtils.copyProperties(group, vo);
            List<AttrEntity> attr = attrService.getRelationAttr(vo.getAttrGroupId());
            vo.setAttrs(attr);
            return vo;
        }).collect(Collectors.toList());
        return collect;


    }

    @Override
    @Transactional
    public List<SkuItemVo.SpuItemAttrGroupVo> getAttrGroupWithAttrsBySpuId(Long spuId, Long catalogId) {


        //1.查出当前SPU 对应的分组信息 以及当前分组下的所有属性对应的值
        //1)spu对应的attr_id
        List<ProductAttrValueEntity> attrValues = productAttrValueService.list(new LambdaQueryWrapper<ProductAttrValueEntity>().eq(ProductAttrValueEntity::getSpuId, spuId));

        List<SkuItemVo.SpuBaseAttrVo> attrs = attrValues.stream().map((attrValueEntity) -> {
            SkuItemVo.SpuBaseAttrVo spuBaseAttrVo = new SkuItemVo.SpuBaseAttrVo();
            //set attrName attrValue
            spuBaseAttrVo.setAttrValue(attrValueEntity.getAttrValue());
            spuBaseAttrVo.setAttrName(attrValueEntity.getAttrName());
            return spuBaseAttrVo;

        }).collect(Collectors.toList());
        List<Long> attrIds = attrValues.stream().map(ProductAttrValueEntity::getAttrId).collect(Collectors.toList());
        //拿到AttrIds
        //查询联系表得到 groupid
        if (attrIds.size() > 0) {
            List<AttrAttrgroupRelationEntity> attrGroupRelation = attrgroupRelationService.list(new LambdaQueryWrapper<AttrAttrgroupRelationEntity>().in(AttrAttrgroupRelationEntity::getAttrId, attrIds));
            List<Long> attrGroupId = attrGroupRelation.stream().map(AttrAttrgroupRelationEntity::getAttrGroupId).collect(Collectors.toList());
            //查询group信息表
            List<AttrGroupEntity> attrGroupEntities = attrGroupService.list(new LambdaQueryWrapper<AttrGroupEntity>().in(AttrGroupEntity::getAttrGroupId, attrGroupId).eq(AttrGroupEntity::getCatelogId, catalogId));
            List<SkuItemVo.SpuItemAttrGroupVo> vos = attrGroupEntities.stream().map((groupEntity) -> {
                SkuItemVo.SpuItemAttrGroupVo spuItemAttrGroupVo = new SkuItemVo.SpuItemAttrGroupVo();
                spuItemAttrGroupVo.setGroupName(groupEntity.getAttrGroupName());
                spuItemAttrGroupVo.setAttrs(attrs);
                return spuItemAttrGroupVo;
            }).collect(Collectors.toList());
            return vos;
        }
        return new ArrayList<>();


    }


}



