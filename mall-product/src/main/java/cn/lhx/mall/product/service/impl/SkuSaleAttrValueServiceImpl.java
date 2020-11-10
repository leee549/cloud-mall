package cn.lhx.mall.product.service.impl;

import cn.lhx.mall.product.entity.SkuInfoEntity;
import cn.lhx.mall.product.service.SkuInfoService;
import cn.lhx.mall.product.vo.SkuItemVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.lhx.common.utils.PageUtils;
import cn.lhx.common.utils.Query;

import cn.lhx.mall.product.dao.SkuSaleAttrValueDao;
import cn.lhx.mall.product.entity.SkuSaleAttrValueEntity;
import cn.lhx.mall.product.service.SkuSaleAttrValueService;

import javax.annotation.Resource;


@Service("skuSaleAttrValueService")
public class SkuSaleAttrValueServiceImpl extends ServiceImpl<SkuSaleAttrValueDao, SkuSaleAttrValueEntity> implements SkuSaleAttrValueService {
    @Resource
    private SkuInfoService skuInfoService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuSaleAttrValueEntity> page = this.page(
                new Query<SkuSaleAttrValueEntity>().getPage(params),
                new QueryWrapper<SkuSaleAttrValueEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<SkuItemVo.SkuItemSaleAttrVo> getSaleAttrsBySpuId(Long spuId) {
        //根据spuId查询有多少种sku,属性值
        List<SkuInfoEntity> skusBySpuId = skuInfoService.getSkusBySpuId(spuId);
        List<Long> skuIds = skusBySpuId.stream().map(SkuInfoEntity::getSkuId).collect(Collectors.toList());
        //查询attr id 和 attr name 并分组
        List<SkuSaleAttrValueEntity> list = this.list(new QueryWrapper<SkuSaleAttrValueEntity>()
                .select("attr_id,attr_name")
                .in("sku_id", skuIds)
                .groupBy("attr_id", "attr_name"));

        if (list.size() > 0) {
            //优化查询，先查询所有值，根据id再过滤出每个值
            List<Long> attrIds = list.stream().map(SkuSaleAttrValueEntity::getAttrId).collect(Collectors.toList());
            List<SkuSaleAttrValueEntity> attrValueEntities = this.list(new QueryWrapper<SkuSaleAttrValueEntity>()
                    .select("distinct attr_value,attr_id").in("attr_id", attrIds));

            List<SkuItemVo.SkuItemSaleAttrVo> vos = list.stream().map(i -> {
                SkuItemVo.SkuItemSaleAttrVo vo = new SkuItemVo.SkuItemSaleAttrVo();
                //赋值
                vo.setAttrId(i.getAttrId());
                vo.setAttrName(i.getAttrName());
                //根据attrid 查询attrvalue
                 List<String> collect = attrValueEntities.stream().map(SkuSaleAttrValueEntity::getAttrValue).collect(Collectors.toList());
                List<String> attrValue = attrValueEntities.stream().filter(
                        attr ->attr.getAttrId().equals(i.getAttrId())
                ).map(SkuSaleAttrValueEntity::getAttrValue).collect(Collectors.toList());
                vo.setAttrValue(attrValue);
                return vo;
            }).collect(Collectors.toList());
            return vos;
        }
        return new ArrayList<>(0);

    }

}
