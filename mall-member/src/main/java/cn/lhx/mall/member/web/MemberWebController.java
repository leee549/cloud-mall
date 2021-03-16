package cn.lhx.mall.member.web;

import cn.lhx.common.utils.R;
import cn.lhx.mall.member.feign.OrderFeignService;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lee549
 * @date 2021/3/2 15:32
 */
@Controller
public class MemberWebController {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Resource
    private OrderFeignService orderFeignService;

    @GetMapping("/memberOrder.html")
    public String memberOrderPage(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                  Model model) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("page", pageNum.toString());
        R r = orderFeignService.listWithItem(map);
        logger.info(JSON.toJSONString(r));
        model.addAttribute("orders", r);
        return "orderList";
    }
}
