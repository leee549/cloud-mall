package cn.lhx.mall.product.service.impl;

import cn.lhx.common.utils.R;
import cn.lhx.mall.product.entity.SkuImagesEntity;
import cn.lhx.mall.product.entity.SpuInfoDescEntity;
import cn.lhx.mall.product.entity.SpuInfoEntity;
import cn.lhx.mall.product.feign.SeckillFeignService;
import cn.lhx.mall.product.service.*;
import cn.lhx.mall.product.vo.SeckillInfoVo;
import cn.lhx.mall.product.vo.SkuItemSaleAttrVo;
import cn.lhx.mall.product.vo.SkuItemVo;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.lhx.common.utils.PageUtils;
import cn.lhx.common.utils.Query;

import cn.lhx.mall.product.dao.SkuInfoDao;
import cn.lhx.mall.product.entity.SkuInfoEntity;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;


@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {
    @Resource
    private SkuImagesService imagesService;
    @Resource
    private SpuInfoDescService spuInfoDescService;
    @Resource
    private AttrGroupService attrGroupService;
    @Resource
    private SkuSaleAttrValueService skuSaleAttrValueService;

    @Resource
    private SeckillFeignService seckillFeignService;

    @Resource
    private ThreadPoolExecutor executor;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                new QueryWrapper<SkuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageByCondiction(Map<String, Object> params) {
        LambdaQueryWrapper<SkuInfoEntity> qw = new LambdaQueryWrapper<>();

        /**
         * key:
         * catelogId: 0
         * brandId: 0
         * min: 0
         * max: 0
         */
        String key = (String) params.get("key");
        // select * from xx where  catelogId="" ,brandid="" and ( skuid="",skuname like xxx)
        if (!StringUtils.isEmpty(key)){
            qw.and((wrapper)->{
                wrapper.eq(SkuInfoEntity::getSkuId,key).or().like(SkuInfoEntity::getSkuName,key);
            });
        }

        String catelogId = (String) params.get("catelogId");
        if (!StringUtils.isEmpty(catelogId)&&!"0".equalsIgnoreCase(catelogId)){
            qw.eq(SkuInfoEntity::getCatalogId,catelogId);
        }

        String brandId = (String) params.get("brandId");
        if (!StringUtils.isEmpty(brandId)&&!"0".equalsIgnoreCase(brandId)){
            qw.eq(SkuInfoEntity::getBrandId,brandId);
        }

        String min = (String) params.get("min");
        if (!StringUtils.isEmpty(min)){
            qw.ge(SkuInfoEntity::getPrice,min);
        }

        String max = (String) params.get("max");
        if (!StringUtils.isEmpty(max)){
            BigDecimal bigDecimal = new BigDecimal(max);
            if (bigDecimal.compareTo(new BigDecimal("0"))==1){
                qw.ge(SkuInfoEntity::getPrice,max);
            }
        }
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                qw
        );


        return new PageUtils(page);


    }

    @Override
    public List<SkuInfoEntity> getSkusBySpuId(Long spuId) {
        List<SkuInfoEntity> list = this.list(new LambdaQueryWrapper<SkuInfoEntity>().eq(SkuInfoEntity::getSpuId, spuId));
        return list;
    }

    @Override
    public SkuItemVo itemInfo(Long skuId) throws ExecutionException, InterruptedException {
        SkuItemVo skuItemVo = new SkuItemVo();
        //异步编排
        CompletableFuture<SkuInfoEntity> infoFuture = CompletableFuture.supplyAsync(() -> {
            //1sku基本信息 pms_sku_info
            SkuInfoEntity info = getById(skuId);
            skuItemVo.setInfo(info);
            return info;
        }, executor);

        CompletableFuture<Void> saleAttrFuture = infoFuture.thenAcceptAsync((res) -> {
            //3 spu的销售属性组合
            List<SkuItemSaleAttrVo> saleAttrVo = skuSaleAttrValueService.getSaleAttrsBySpuId(res.getSpuId());
            skuItemVo.setSaleAttr(saleAttrVo);
        }, executor);

        CompletableFuture<Void> descFuture = infoFuture.thenAcceptAsync(res -> {
            //4 spu 的介绍(图片来的)
            SpuInfoDescEntity spuInfoDescEntity = spuInfoDescService.getById(res.getSpuId());
            skuItemVo.setDesc(spuInfoDescEntity);
        }, executor);

        CompletableFuture<Void> baseAttrFuture = infoFuture.thenAcceptAsync(res -> {
            //5 spu的规格参数信息
            List<SkuItemVo.SpuItemAttrGroupVo> attrGroupVos = attrGroupService.getAttrGroupWithAttrsBySpuId(res.getSpuId(), res.getCatalogId());
            skuItemVo.setGroupAttr(attrGroupVos);
        }, executor);

        CompletableFuture<Void> imgFuture = CompletableFuture.runAsync(() -> {
            //2sku图片信息 sku_images
            List<SkuImagesEntity> images = imagesService.getImagesBySkuId(skuId);
            skuItemVo.setImages(images);
        }, executor);

        //3.查询当前sku商品是否有参与秒杀活动
        CompletableFuture<Void> seckillFuture = CompletableFuture.runAsync(() -> {
            R skuSeckillInfo = seckillFeignService.getSkuSeckillInfo(skuId);
            if (skuSeckillInfo.getCode() == 0) {
                SeckillInfoVo seckillInfoVo = skuSeckillInfo.getData(new TypeReference<SeckillInfoVo>() {
                });
                skuItemVo.setSeckillInfoVo(seckillInfoVo);
            }
        }, executor);


        //所有完成
        CompletableFuture.allOf(imgFuture,descFuture,saleAttrFuture,baseAttrFuture,seckillFuture).get();


        return skuItemVo;

    }

}
