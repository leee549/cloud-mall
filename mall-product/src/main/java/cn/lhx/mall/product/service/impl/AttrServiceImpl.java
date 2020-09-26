package cn.lhx.mall.product.service.impl;

import cn.lhx.mall.product.entity.AttrAttrgroupRelationEntity;
import cn.lhx.mall.product.entity.AttrGroupEntity;
import cn.lhx.mall.product.entity.CategoryEntity;
import cn.lhx.mall.product.service.AttrGroupService;
import cn.lhx.mall.product.service.CategoryService;
import cn.lhx.mall.product.vo.AttrRespVo;
import cn.lhx.mall.product.vo.AttrVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.databind.util.BeanUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.lhx.common.utils.PageUtils;
import cn.lhx.common.utils.Query;

import cn.lhx.mall.product.dao.AttrDao;
import cn.lhx.mall.product.entity.AttrEntity;
import cn.lhx.mall.product.service.AttrService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {
    @Resource
    private AttrAttrgroupRelationServiceImpl attrRelationService;
    @Resource
    private AttrGroupService attrGroupService;
    @Resource
    private CategoryService categoryService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional
    public void saveAttr(AttrVo attr) {
        AttrEntity attrEntity = new AttrEntity();
        //将前端传来的值赋值给PO
        BeanUtils.copyProperties(attr, attrEntity);
        //1.保存基本属性
        this.save(attrEntity);
        //2.保存关联关系
        AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
        relationEntity.setAttrGroupId(attr.getAttrGroupId());
        relationEntity.setAttrId(attrEntity.getAttrId());
        attrRelationService.save(relationEntity);

    }

    @Override
    public PageUtils queryBaseAttrPage(Map<String, Object> params, Long catlogId) {
        QueryWrapper<AttrEntity> qw = new QueryWrapper<>();
        if (catlogId != 0) {
            qw.lambda().eq(AttrEntity::getCatelogId, catlogId);
        }
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            qw.lambda().and((wrapper) -> {
                wrapper.eq(AttrEntity::getAttrId, key).or().like(AttrEntity::getAttrName, key);
            });
        }
        IPage<AttrEntity> page = this.page(new Query<AttrEntity>().getPage(params), qw);
        PageUtils pageUtils = new PageUtils(page);
        List<AttrEntity> records = page.getRecords();
        List<AttrRespVo> respVos = records.stream().map((attrEntity -> {
            AttrRespVo attrRespVo = new AttrRespVo();
            BeanUtils.copyProperties(attrEntity, attrRespVo);
            //设置分组名
            AttrAttrgroupRelationEntity attrGroupRelationEntity = attrRelationService.getOne(new QueryWrapper<AttrAttrgroupRelationEntity>().lambda().eq(AttrAttrgroupRelationEntity::getAttrId, attrEntity.getAttrId()));
            if (attrGroupRelationEntity != null) {
                AttrGroupEntity attrGroupEntity = attrGroupService.getById(attrGroupRelationEntity.getAttrGroupId());
                attrRespVo.setGroupName(attrGroupEntity.getAttrGroupName());
            }
            //设置分类名
            CategoryEntity categoryEntity = categoryService.getById(attrEntity.getCatelogId());
            if (categoryEntity != null) {
                attrRespVo.setCatelogName(categoryEntity.getName());
            }
            return attrRespVo;
        })).collect(Collectors.toList());
        pageUtils.setList(respVos);
        return pageUtils;
    }

    @Override
    public AttrRespVo getAttrInfo(Long attrId) {
        AttrRespVo attrRespVo = new AttrRespVo();
        AttrEntity attrEntity = this.getById(attrId);
        BeanUtils.copyProperties(attrEntity, attrRespVo);
        //1.分组信息的设置
        AttrAttrgroupRelationEntity attrgroupRelation = attrRelationService.getOne(new QueryWrapper<AttrAttrgroupRelationEntity>().lambda().eq(AttrAttrgroupRelationEntity::getAttrId, attrId));
        if (attrgroupRelation != null) {
            attrRespVo.setAttrGroupId(attrgroupRelation.getAttrGroupId());
            AttrGroupEntity groupEntity = attrGroupService.getById(attrgroupRelation.getAttrGroupId());
            if (groupEntity != null) {
                attrRespVo.setGroupName(groupEntity.getAttrGroupName());
            }
        }
        //2.设置分类信息

        Long[] catelogPath = categoryService.findCatelogPath(attrEntity.getCatelogId());
        attrRespVo.setCatelogPath(catelogPath);

        CategoryEntity categoryEntity = categoryService.getById(attrEntity.getAttrId());
        if (categoryEntity != null) {
            attrRespVo.setCatelogName(categoryEntity.getName());
        }
        return attrRespVo;
    }

    @Transactional(rollbackFor = {})
    @Override
    public void updateAttr(AttrVo attr) {
        //基本修改
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr, attrEntity);
        this.updateById(attrEntity);
        //修改关联表
        AttrAttrgroupRelationEntity attrgroupRelationEntity = new AttrAttrgroupRelationEntity();
        attrgroupRelationEntity.setAttrId(attr.getAttrId());
        attrgroupRelationEntity.setAttrGroupId(attr.getAttrGroupId());
        int count = attrRelationService.count(new UpdateWrapper<AttrAttrgroupRelationEntity>().lambda().eq(AttrAttrgroupRelationEntity::getAttrId, attr.getAttrId()));
        //判断是否是新增操作
        if (count>0){
            attrRelationService.update(attrgroupRelationEntity,new UpdateWrapper<AttrAttrgroupRelationEntity>().lambda().eq(AttrAttrgroupRelationEntity::getAttrId, attr.getAttrId()));
        }else {
            attrRelationService.save(attrgroupRelationEntity);
        }
    }

}
