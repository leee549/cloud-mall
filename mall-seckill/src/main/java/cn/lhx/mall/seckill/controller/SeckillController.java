package cn.lhx.mall.seckill.controller;

import cn.lhx.common.utils.R;
import cn.lhx.mall.seckill.service.SeckillService;
import cn.lhx.mall.seckill.to.SeckillSkuRedisTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author lee549
 * @date 2021/3/10 10:10
 */
@Controller
public class SeckillController {

    @Resource
    private SeckillService seckillService;

    /**
     * 返回当前时间可以参与的秒杀商品
     *
     * @return
     */
    @ResponseBody
    @GetMapping("/currentSeckillSkus")
    public R getCurrentSeckillSkus() {
        List<SeckillSkuRedisTo> vos = seckillService.getCurrentSeckillSkus();
        return R.ok().setData(vos);
    }

    /**
     * 秒杀预告信息
     *
     * @param skuId
     * @return
     */
    @ResponseBody
    @GetMapping("/sku/seckill/{skuId}")
    public R getSkuSeckillInfo(@PathVariable("skuId") Long skuId) {
        SeckillSkuRedisTo to = seckillService.getSkuSeckillInfo(skuId);
        return R.ok().setData(to);
    }

    //TODO 上架秒杀商品的时候每一个数据都有过期时间
    //TODO 秒杀后续，简化了收获地址信息等，上架锁定库存等

    /**
     * 秒杀下单
     */
    @GetMapping("/kill")
    public String seckill(@RequestParam("killId") String killId, @RequestParam("key") String key,
                          @RequestParam("num") Integer num, Model model) {
        String orderSn = seckillService.kill(killId, key, num);
        model.addAttribute("orderSn",orderSn);
        return "success";
    }


}
