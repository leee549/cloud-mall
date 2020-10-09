package cn.lhx.mall.ware.service.impl;

import cn.lhx.common.utils.R;
import cn.lhx.mall.ware.feign.ProductFeignService;
import cn.lhx.mall.ware.vo.SkuHasStockVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.lhx.common.utils.PageUtils;
import cn.lhx.common.utils.Query;

import cn.lhx.mall.ware.dao.WareSkuDao;
import cn.lhx.mall.ware.entity.WareSkuEntity;
import cn.lhx.mall.ware.service.WareSkuService;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;


@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {
    @Resource
    private WareSkuDao wareSkuDao;
    @Resource
    ProductFeignService productFeignService;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<WareSkuEntity> qw = new LambdaQueryWrapper<>();
        String skuId = (String) params.get("skuId");
        if (!StringUtils.isEmpty(skuId)){
            qw.eq(WareSkuEntity::getSkuId,skuId);
        }
        String wareId = (String) params.get("ware_id");
        if (!StringUtils.isEmpty(wareId)){
            qw.eq(WareSkuEntity::getWareId,wareId);
        }
        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params),
                qw
        );

        return new PageUtils(page);
    }

    @Override
    public void addStock(Long skuId, Long wareId, Integer skuNum) {
        //判断是否有这个库存没有就新增
        LambdaQueryWrapper<WareSkuEntity> qw = new LambdaQueryWrapper<>();
        qw.eq(WareSkuEntity::getSkuId,skuId)
          .eq(WareSkuEntity::getWareId,wareId);
        List<WareSkuEntity> entities = this.list(qw);
        if (entities==null||entities.size()==0){
            WareSkuEntity wareSkuEntity = new WareSkuEntity();
            wareSkuEntity.setSkuId(skuId);
            wareSkuEntity.setWareId(wareId);
            wareSkuEntity.setStock(skuNum);
            wareSkuEntity.setStockLocked(0);
            //远程查询skuname
           try {
               R info = productFeignService.info(skuId);
               Map<String,Object> data = (Map<String, Object>) info.get("skuInfo");

               if (info.getCode()==0){
                   wareSkuEntity.setSkuName((String) data.get("skuName"));
               }
           }catch (Exception e){

           }

            wareSkuDao.insert(wareSkuEntity);
        }else {
            wareSkuDao.addStock(skuId,wareId,skuNum);

        }
    }

    @Override
    public List<SkuHasStockVo> getSkusHasStock(List<Long> skuIds) {

        List<SkuHasStockVo> collect = skuIds.stream().map(skuId -> {
            SkuHasStockVo vo = new SkuHasStockVo();
            Long count =baseMapper.getSkuStock(skuId);
            vo.setSkuId(skuId);
            vo.setHasStock(count==null?false:count>0);
            return vo;
        }).collect(Collectors.toList());
        return collect;

    }

}
