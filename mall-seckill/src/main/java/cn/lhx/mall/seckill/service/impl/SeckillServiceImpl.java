package cn.lhx.mall.seckill.service.impl;

import cn.lhx.common.to.mq.SeckillOrderTo;
import cn.lhx.common.utils.R;
import cn.lhx.common.vo.MemberRespVo;
import cn.lhx.mall.seckill.feign.CouponFeignService;
import cn.lhx.mall.seckill.feign.ProductFeignService;
import cn.lhx.mall.seckill.inteceptor.LoginUserInterceptor;
import cn.lhx.mall.seckill.service.SeckillService;
import cn.lhx.mall.seckill.to.SeckillSkuRedisTo;
import cn.lhx.mall.seckill.vo.SeckillSessionVo;
import cn.lhx.mall.seckill.vo.SeckillSkuRelationVo;
import cn.lhx.mall.seckill.vo.SkuInfoVo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author lee549
 * @date 2021/3/7 15:59
 */
@Service
public class SeckillServiceImpl implements SeckillService {

    private final String SESSION_CACHE_PREFIX = "seckill:sessions:";

    private final String SKU_CACHE_PREFIX = "seckill:skus";

    private final String SKU_STOCK_SEMAPHORE = "seckill:stock:";//+商品随机码


    @Resource
    private CouponFeignService couponFeignService;

    @Resource
    private ProductFeignService productFeignService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Override
    public void uploadSeckillLatest3Days() {
        //1.扫描需要参与秒杀的活动
        R r = couponFeignService.getFuture3DaysSession();
        if (r.getCode() == 0) {
            //上架商品
            List<SeckillSessionVo> data = r.getData(new TypeReference<List<SeckillSessionVo>>() {
            });
            if (data != null && data.size() > 0) {
                //缓存到redis
                //1.缓存活动详情信息
                saveSessionInfos(data);
                //2.缓存活动的关联的商品信息
                saveSessionSkuInfos(data);
            }

        }

    }

    /**
     * 返回当前时间可以参与秒杀的商品
     *
     * @return
     */
    @Override
    public List<SeckillSkuRedisTo> getCurrentSeckillSkus() {
        //1.确定当前时间属于哪个秒杀场次
        Long now = System.currentTimeMillis();
        Set<String> keys = stringRedisTemplate.keys(SESSION_CACHE_PREFIX + "*");
        for (String key : keys) {
            String replace = key.replace(SESSION_CACHE_PREFIX, "");
            String[] s = replace.split("_");
            Long startTime = Long.parseLong(s[0]);
            Long endTime = Long.parseLong(s[1]);
            if (now >= startTime && now <= endTime) {
                //获取该场次的所有商品信息
                List<String> range = stringRedisTemplate.opsForList().range(key, -100, 100);
                BoundHashOperations<String, String, String> hashOps = stringRedisTemplate.boundHashOps(SKU_CACHE_PREFIX);
                List<String> list = hashOps.multiGet(range);
                if (list != null) {
                    List<SeckillSkuRedisTo> collect = list.stream().map(item -> {
                        SeckillSkuRedisTo redisTo = JSON.parseObject(item, SeckillSkuRedisTo.class);
                        return redisTo;
                    }).collect(Collectors.toList());
                    return collect;
                }
                break;
            }
        }
        //2.获取该场次的所有商品信息

        return null;
    }

    @Override
    public SeckillSkuRedisTo getSkuSeckillInfo(Long skuId) {
        //1.找到所有秒杀商品key信息
        BoundHashOperations<String, String, String> ops = stringRedisTemplate.boundHashOps(SKU_CACHE_PREFIX);
        Set<String> keys = ops.keys();
        if (keys != null && keys.size() > 0) {
            String regx = "\\d_" + skuId;
            for (String key : keys) {
                if (Pattern.matches(regx, key)) {
                    String json = ops.get(key);
                    SeckillSkuRedisTo redisTo = JSON.parseObject(json, SeckillSkuRedisTo.class);
                    Long now = System.currentTimeMillis();
                    //随机码
                    if (now >= redisTo.getStartTime() && now <= redisTo.getEndTime()) {

                    } else {
                        redisTo.setRandomCode(null);
                    }
                    return redisTo;
                }
            }
        }
        return null;

    }

    @Override
    public String kill(String killId, String key, Integer num) {
        MemberRespVo userVo = LoginUserInterceptor.loginUser.get();
        //1.获取当前秒杀商品的详细信息
        BoundHashOperations<String, String, String> ops = stringRedisTemplate.boundHashOps(SKU_CACHE_PREFIX);
        String json = ops.get(killId);
        if (!StringUtils.isEmpty(json)) {
            SeckillSkuRedisTo redisTo = JSON.parseObject(json, SeckillSkuRedisTo.class);
            //1校验合法性
            Long startTime = redisTo.getStartTime();
            Long endTime = redisTo.getEndTime();
            Long now = System.currentTimeMillis();
            if (now < startTime || now > endTime) {
                return null;
            }
            //2、校验随机码和商品id
            String randomCode = redisTo.getRandomCode();
            String id = redisTo.getPromotionSessionId() + "_" + redisTo.getSkuId();
            if (!key.equals(randomCode) && !killId.equals(id)) {
                return null;
            }
            //3.验证数量
            if (num > redisTo.getSeckillLimit()) {
                return null;
            }
            //4.验证已经买过
            String redisKey = userVo.getId() + "_" + id;
            Boolean ifAbsent = stringRedisTemplate.opsForValue().setIfAbsent(redisKey, num.toString(), endTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
            if (!ifAbsent) {
                return null;
            }
            RSemaphore semaphore = redissonClient.getSemaphore(SKU_STOCK_SEMAPHORE+randomCode);

            //获取到信号量才能成功
            boolean b = semaphore.tryAcquire(num);
            if (!b) {
                return null;
            }
            //秒杀成功，发送MQ消息
            String timeId = IdWorker.getTimeId();
            SeckillOrderTo orderTo = new SeckillOrderTo();
            orderTo.setOrderSn(timeId);
            orderTo.setMemberId(userVo.getId());
            orderTo.setNum(num);
            orderTo.setPromotionSessionId(redisTo.getPromotionSessionId());
            orderTo.setSkuId(redisTo.getSkuId());
            orderTo.setSeckillPrice(redisTo.getSeckillPrice());
            rabbitTemplate.convertAndSend("order-event-exchange", "order.seckill.order", orderTo);
            return timeId;

        }
        return null;
    }

    private void saveSessionInfos(List<SeckillSessionVo> data) {
        data.stream().forEach(session -> {
            Long startTime = session.getStartTime().getTime();
            Long endTime = session.getEndTime().getTime();
            String key = SESSION_CACHE_PREFIX + startTime + "_" + endTime;
            Boolean hasKey = stringRedisTemplate.hasKey(key);
            if (!hasKey) {
                List<String> collect = session.getRelationSkus().stream().map(item -> item.getPromotionSessionId() + "_" + item.getSkuId().toString()).collect(Collectors.toList());
                //缓存活动信息
                stringRedisTemplate.opsForList().leftPushAll(key, collect);
                stringRedisTemplate.expire(key, endTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);//设置过期时间
            }
        });

    }

    private void saveSessionSkuInfos(List<SeckillSessionVo> data) {
        data.stream().forEach(session -> {
            //准备hash操作
            BoundHashOperations<String, Object, Object> ops = stringRedisTemplate.boundHashOps(SKU_CACHE_PREFIX);
            for (SeckillSkuRelationVo seckillSkuVo : session.getRelationSkus()) {
                String token = UUID.randomUUID().toString().replace("-", "");
                if (!ops.hasKey(seckillSkuVo.getPromotionSessionId() + "_" + seckillSkuVo.getSkuId().toString())) {
                    //缓存商品
                    SeckillSkuRedisTo redisTo = new SeckillSkuRedisTo();
                    //1.sku基本信息
                    R skuInfo = productFeignService.getSkuInfo(seckillSkuVo.getSkuId());
                    if (skuInfo.getCode() == 0) {
                        SkuInfoVo info = skuInfo.getData("skuInfo", new TypeReference<SkuInfoVo>() {
                        });
                        redisTo.setSkuInfo(info);
                    }
                    //2.sku的秒杀信息
                    BeanUtils.copyProperties(seckillSkuVo, redisTo);
                    //3.设置开始结束时间
                    redisTo.setStartTime(session.getStartTime().getTime());
                    redisTo.setEndTime(session.getEndTime().getTime());
                    //4.随机码,防止别人攻击
                    redisTo.setRandomCode(token);

                    String jsonString = JSON.toJSONString(redisTo);
                    ops.put(seckillSkuVo.getPromotionSessionId().toString() + "_" + seckillSkuVo.getSkuId().toString(), jsonString);
                    //设置过期时间
                    ops.expire(session.getEndTime().getTime() - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
                    //5.引入分布式信号量 限流
                    RSemaphore semaphore = redissonClient.getSemaphore(SKU_STOCK_SEMAPHORE + token);
                    //商品数量作为信号量
                    semaphore.trySetPermits(seckillSkuVo.getSeckillCount());
                }

            }
        });
    }


}
