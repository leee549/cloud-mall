package cn.lhx.mall.coupon.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import cn.lhx.mall.coupon.entity.SeckillSessionEntity;
import cn.lhx.mall.coupon.service.SeckillSessionService;
import cn.lhx.common.utils.PageUtils;
import cn.lhx.common.utils.R;



/**
 * 秒杀活动场次
 *
 * @author lhx
 * @email 1193106371@qq.com
 * @date 2020-08-31 18:05:32
 */
@RestController
@RequestMapping("coupon/seckillsession")
public class SeckillSessionController {
    @Autowired
    private SeckillSessionService seckillSessionService;

    @GetMapping("/future3DaysSession")
    public R getFuture3DaysSession(){
        List<SeckillSessionEntity> sessions =seckillSessionService.getFuture3DaysSession();
        return R.ok().setData(sessions);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    // @RequiresPermissions("coupon:seckillsession:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = seckillSessionService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("coupon:seckillsession:info")
    public R info(@PathVariable("id") Long id){
		SeckillSessionEntity seckillSession = seckillSessionService.getById(id);

        return R.ok().put("seckillSession", seckillSession);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    // @RequiresPermissions("coupon:seckillsession:save")
    public R save(@RequestBody SeckillSessionEntity seckillSession){
        seckillSession.setCreateTime(new Date());
		seckillSessionService.save(seckillSession);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    // @RequiresPermissions("coupon:seckillsession:update")
    public R update(@RequestBody SeckillSessionEntity seckillSession){
		seckillSessionService.updateById(seckillSession);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
        public R delete(@RequestBody Long[] ids){
		seckillSessionService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
