package cn.lhx.mall.coupon.service.impl;

import cn.lhx.mall.coupon.entity.SeckillSkuRelationEntity;
import cn.lhx.mall.coupon.service.SeckillSkuRelationService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.lhx.common.utils.PageUtils;
import cn.lhx.common.utils.Query;

import cn.lhx.mall.coupon.dao.SeckillSessionDao;
import cn.lhx.mall.coupon.entity.SeckillSessionEntity;
import cn.lhx.mall.coupon.service.SeckillSessionService;

import javax.annotation.Resource;


@Service("seckillSessionService")
public class SeckillSessionServiceImpl extends ServiceImpl<SeckillSessionDao, SeckillSessionEntity> implements SeckillSessionService {

    @Resource
    private SeckillSkuRelationService seckillSkuRelationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SeckillSessionEntity> page = this.page(
                new Query<SeckillSessionEntity>().getPage(params),
                new QueryWrapper<SeckillSessionEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<SeckillSessionEntity> getFuture3DaysSession() {
        List<SeckillSessionEntity> list = this.list(new LambdaQueryWrapper<SeckillSessionEntity>().between(SeckillSessionEntity::getStartTime, startTime(), endTime()));
        if (list != null && list.size() > 0) {
            List<SeckillSessionEntity> collect = list.stream().map(session -> {
                List<SeckillSkuRelationEntity> relationEntities = seckillSkuRelationService.list(new LambdaQueryWrapper<SeckillSkuRelationEntity>().eq(SeckillSkuRelationEntity::getPromotionSessionId, session.getId()));
                session.setRelationSkus(relationEntities);
                return session;
            }).collect(Collectors.toList());
            return collect;
        }

        return null;
    }

    /**
     * 获取最近3天起始时间
     *
     * @return
     */
    public String startTime() {
        return LocalDateTime.of(LocalDate.now(), LocalTime.MIN).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 获取最近3天结束时间
     *
     * @return
     */
    public String endTime() {
        return LocalDateTime.of(LocalDate.now().plusDays(2), LocalTime.MAX).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

}
