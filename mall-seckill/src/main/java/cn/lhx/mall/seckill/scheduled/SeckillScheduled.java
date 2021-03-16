package cn.lhx.mall.seckill.scheduled;

import cn.lhx.mall.seckill.service.SeckillService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author lee549
 * @date 2021/3/7 15:52
 */

@Service
@Slf4j
public class SeckillScheduled {

    @Autowired
    SeckillService seckillService;

    @Resource
    RedissonClient redissonClient;

    private final String upload_lock = "seckill:upload:lock";

    @Scheduled(cron = "*/10 * * * * ?")
    public void uploadSeckillLatest3Days() {
        log.info("上架最近3天秒杀活动的商品");
        RLock lock = redissonClient.getLock(upload_lock);
        lock.lock(10, TimeUnit.SECONDS);
        try {
            //定时上架最近3天上架的秒杀活动的商品
            seckillService.uploadSeckillLatest3Days();
        } finally {
            lock.unlock();
        }

    }
}
