package cn.lhx.mall.product.service.impl;

import cn.lhx.common.constant.ProductConstant;
import cn.lhx.common.to.SkuHasStockVo;
import cn.lhx.common.to.SkuReductionTo;
import cn.lhx.common.to.SpuBoundTo;
import cn.lhx.common.to.es.SkuEsModel;
import cn.lhx.common.utils.R;
import cn.lhx.mall.product.entity.*;
import cn.lhx.mall.product.feign.CouponFeignService;
import cn.lhx.mall.product.feign.SearchFeignService;
import cn.lhx.mall.product.feign.WareFeignService;
import cn.lhx.mall.product.service.*;
import cn.lhx.mall.product.vo.*;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.lhx.common.utils.PageUtils;
import cn.lhx.common.utils.Query;

import cn.lhx.mall.product.dao.SpuInfoDao;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {
    @Resource
    private SpuInfoDescService spuInfoDescService;
    @Resource
    private SpuImagesService spuImagesService;
    @Resource
    private AttrService attrService;
    @Resource
    private ProductAttrValueService productAttrValueService;
    @Resource
    private SkuInfoService skuInfoService;
    @Resource
    private SkuImagesService skuImagesService;
    @Resource
    private SkuSaleAttrValueService skuSaleAttrValueService;
    @Resource
    private CouponFeignService couponFeignService;
    @Resource
    private BrandService brandService;
    @Resource
    private CategoryService categoryService;
    @Resource
    private WareFeignService wareFeignService;
    @Resource
    private SearchFeignService searchFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 适合seata 的AT 模式，不需要大并发
     * @param vo
     */
    @GlobalTransactional
    @Transactional(rollbackFor = {})
    @Override
    public void saveSpuInfo(SpuSaveVo vo) {
        //1.保存spu基本属性 pms_spu_info
        SpuInfoEntity infoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(vo,infoEntity);
        infoEntity.setCreateTime(new Date());
        infoEntity.setUpdateTime(new Date());
        this.saveBaseSpuInfo(infoEntity);

        //2.保存spu的描述图片pms_spu_info_desc
        List<String> decript = vo.getDecript();
        SpuInfoDescEntity descEntity = new SpuInfoDescEntity();
        descEntity.setSpuId(infoEntity.getId());
        descEntity.setDecript(String.join(",",decript));
        spuInfoDescService.saveSpuInfoDecript(descEntity);

        //3.保存 spu的图片集pms_spu_images
        List<String> images = vo.getImages();
        spuImagesService.saveImages(infoEntity.getId(),images);

        //4.保存spu 的规格参数pms_product_attr_value
        List<BaseAttrs> baseAttrs = vo.getBaseAttrs();
        List<ProductAttrValueEntity> collect = baseAttrs.stream().map(attr -> {
            ProductAttrValueEntity valueEntity = new ProductAttrValueEntity();
            AttrEntity attrEntity = attrService.getById(attr.getAttrId());
            valueEntity.setAttrName(attrEntity.getAttrName());
            valueEntity.setAttrId(attr.getAttrId());
            valueEntity.setAttrValue(attr.getAttrValues());
            valueEntity.setQuickShow(attr.getShowDesc());
            valueEntity.setSpuId(infoEntity.getId());
            return valueEntity;
        }).collect(Collectors.toList());
        productAttrValueService.saveProductAttr(collect);


        //5.保存spu的积分信息 sms->sms_spu_bounds
        Bounds bounds = vo.getBounds();
        SpuBoundTo spuBoundTo = new SpuBoundTo();
        BeanUtils.copyProperties(bounds,spuBoundTo);
        spuBoundTo.setSpuId(infoEntity.getId());
        R r = couponFeignService.saveSpuBounds(spuBoundTo);
        if (r.getCode()!=0){
            System.out.println(r.getCode());
            log.error("远程保存spu的积分信息失败");
        }

        //5. 保存spu对应的所有sku信息
        //5.1) sku的基本信息：pms_sku_info
        List<Skus> skus = vo.getSkus();
        if (skus!=null && skus.size()>0){
            skus.forEach(item->{
                String defaultImg = "";
                for (Images image : item.getImages()) {
                    if (image.getDefaultImg()==1){
                        defaultImg=image.getImgUrl();
                    }
                }
                SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
                BeanUtils.copyProperties(item,skuInfoEntity);
                skuInfoEntity.setBrandId(infoEntity.getBrandId());
                skuInfoEntity.setCatalogId(infoEntity.getCatalogId());
                skuInfoEntity.setSaleCount(0L);
                skuInfoEntity.setSpuId(infoEntity.getId());
                skuInfoEntity.setSkuDefaultImg(defaultImg);
                //5.1) sku的基本信息：pms_sku_info
                skuInfoService.save(skuInfoEntity);

                Long skuId = skuInfoEntity.getSkuId();
                List<SkuImagesEntity> skuImagesEntities = item.getImages().stream().map(img -> {
                    SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                    skuImagesEntity.setSkuId(skuId);
                    skuImagesEntity.setImgUrl(img.getImgUrl());
                    skuImagesEntity.setDefaultImg(img.getDefaultImg());
                    return skuImagesEntity;
                }).filter(entity->{
                    //返回false 代表过滤
                    return !StringUtils.isEmpty(entity.getImgUrl());
                }).collect(Collectors.toList());
                //5.2）sku的图片信息：pms_sku_images
                skuImagesService.saveBatch(skuImagesEntities);
                //5.3) sku的销售信息：pms_sku_sale_attr_value
                List<Attr> attr = item.getAttr();
                List<SkuSaleAttrValueEntity> saleAttrValueEntities = attr.stream().map(a -> {
                    SkuSaleAttrValueEntity skuSaleAttrValueEntity = new SkuSaleAttrValueEntity();
                    BeanUtils.copyProperties(a, skuSaleAttrValueEntity);
                    skuSaleAttrValueEntity.setSkuId(skuId);
                    return skuSaleAttrValueEntity;
                }).collect(Collectors.toList());
                skuSaleAttrValueService.saveBatch(saleAttrValueEntities);
                //5.4） sku的满减优惠信息：sms_sku_full_reduction  sms_sku_ladder sms_member_price
                SkuReductionTo skuReductionTo = new SkuReductionTo();
                BeanUtils.copyProperties(item,skuReductionTo);
                skuReductionTo.setSkuId(skuId);
                if (skuReductionTo.getFullCount()>0||skuReductionTo.getFullPrice().compareTo(new BigDecimal("0"))==1){
                    R r1 = couponFeignService.saveSkuReduction(skuReductionTo);
                    if (r1.getCode()!=0){
                        log.error("远程sku的满减优惠信息失败");
                    }
                }

            });
        }





    }

    @Override
    public void saveBaseSpuInfo(SpuInfoEntity infoEntity) {
        this.baseMapper.insert(infoEntity);
    }

    @Override
    public PageUtils queryPageByCondiction(Map<String, Object> params) {
        LambdaQueryWrapper<SpuInfoEntity> qw = new LambdaQueryWrapper<>();
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)){
                qw.and((w)->{
                    w.eq(SpuInfoEntity::getId,key);
                });
        }

        String status = (String) params.get("status");
        if (!StringUtils.isEmpty(status)){
                qw.eq(SpuInfoEntity::getPublishStatus,status);
        }

        String brandId = (String) params.get("brandId");
        if (!StringUtils.isEmpty(brandId)&&!"0".equalsIgnoreCase(brandId)){
                qw.eq(SpuInfoEntity::getBrandId,brandId);
        }

        String catelogId = (String) params.get("catelogId");
        if (!StringUtils.isEmpty(catelogId)&&!"0".equalsIgnoreCase(catelogId)){
                qw.eq(SpuInfoEntity::getCatalogId,catelogId);
        }
        /**
         * status: 0
         * key:
         * brandId: 5
         * catelogId: 225
         */
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                qw
        );

        return new PageUtils(page);
    }

    @Override
    public void up(Long spuId) {
        //1 查出当前spuid对应的sku信息，品牌的名字等等
        List<SkuInfoEntity> skus = skuInfoService.getSkusBySpuId(spuId);
        List<Long> skuIdList = skus.stream().map(SkuInfoEntity::getSkuId).collect(Collectors.toList());
        // 5 查询当前sku的所有可以被检索的规格属性
        List<ProductAttrValueEntity> baseAttrs = productAttrValueService.baseAttrlistforspu(spuId);
        List<Long> attrIds = baseAttrs.stream().map(attr -> {
            return attr.getAttrId();
        }).collect(Collectors.toList());
        //    0
        List<Long> searchAttrIds= attrService.selectSearchAttrs(attrIds);

        Set<Long> idSet =new HashSet<>(searchAttrIds);
        List<SkuEsModel.Attrs> attrsList = baseAttrs.stream().filter(item -> {
            return idSet.contains(item.getAttrId());
        }).map(item -> {
            SkuEsModel.Attrs attrs = new SkuEsModel.Attrs();
            BeanUtils.copyProperties(item, attrs);
            return attrs;
        }).collect(Collectors.toList());
        //todo 远程调用库存
        Map<Long, Boolean> stockMap =null;
        try {
            R skusHasStock = wareFeignService.getSkusHasStock(skuIdList);
            TypeReference<List<SkuHasStockVo>> listTypeReference = new TypeReference<List<SkuHasStockVo>>(){};
            stockMap = skusHasStock.getData(listTypeReference).stream().collect(Collectors.toMap(SkuHasStockVo::getSkuId, item -> item.getHasStock()));
        }catch (Exception e){
            log.error("远程调用库存服务异常：{}",e);
        }

        //2封装 每个sku信息
        Map<Long, Boolean> finalStockMap = stockMap;
        List<SkuEsModel> upProducts = skus.stream().map(sku -> {
            //组装需要的数据
            SkuEsModel esModel = new SkuEsModel();
            BeanUtils.copyProperties(sku, esModel);
            esModel.setSkuPrice(sku.getPrice());
            esModel.setSkuImg(sku.getSkuDefaultImg());
            //1 发送远程调用 检查库存
            if (finalStockMap ==null){
                esModel.setHasStock(true);
            }else {
                esModel.setHasStock(finalStockMap.get(sku.getSkuId()));
            }
            //2 热度 默认0
            esModel.setHotScore(0L);
            //3查询品牌相关
            BrandEntity brand = brandService.getById(sku.getBrandId());
            esModel.setBrandImg(brand.getLogo());
            esModel.setBrandName(brand.getName());
            //4设置分类
            CategoryEntity category = categoryService.getById(esModel.getCatalogId());
            esModel.setCatalogName(category.getName());
            //5设置属性
            esModel.setAttrs(attrsList);
            return esModel;
        }).collect(Collectors.toList());
        // 6远程调用ES 发送上架
        R r = searchFeignService.productStatusUp(upProducts);
        if (r.getCode()==0){
            //成功 修改spu状态为上架
            baseMapper.updateSpuState(spuId, ProductConstant.StatusEnum.SPU_UP.getCode());
        }else {
            // todo  7重复调用问题 接口幂等性
        }


    }

    @Override
    public SpuInfoEntity getSpuInfoBySkuId(Long skuId) {
        SkuInfoEntity byId = skuInfoService.getById(skuId);
        Long spuId = byId.getSpuId();
        SpuInfoEntity spuInfoEntity = getById(spuId);
        return spuInfoEntity;

    }


}
