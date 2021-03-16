package cn.lhx.mall.seckill.service;

import cn.lhx.mall.seckill.to.SeckillSkuRedisTo;

import java.util.List;

/**
 * @author lee549
 * @date 2021/3/7 15:58
 */
public interface SeckillService {
    void uploadSeckillLatest3Days();

    List<SeckillSkuRedisTo> getCurrentSeckillSkus();

    SeckillSkuRedisTo getSkuSeckillInfo(Long skuId);

    String kill(String killId, String key, Integer num);
}
