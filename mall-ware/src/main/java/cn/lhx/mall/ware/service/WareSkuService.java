package cn.lhx.mall.ware.service;

import cn.lhx.common.to.mq.OrderTo;
import cn.lhx.common.to.mq.StockLockedTo;
import cn.lhx.mall.ware.vo.LockStockResult;
import cn.lhx.mall.ware.vo.SkuHasStockVo;
import cn.lhx.mall.ware.vo.WareSkuLockVo;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.lhx.common.utils.PageUtils;
import cn.lhx.mall.ware.entity.WareSkuEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品库存
 *
 * @author lhx
 * @email 1193106371@qq.com
 * @date 2020-08-31 17:59:27
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void addStock(Long skuId, Long wareId, Integer skuNum);

    List<SkuHasStockVo> getSkusHasStock(List<Long> skuIds);

    Boolean orderLockStock(WareSkuLockVo vo);

    void unlockStock(StockLockedTo to);

    void unlockStock(OrderTo orderTo);
}

