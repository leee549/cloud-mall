package cn.lhx.mall.order.web;

import cn.lhx.mall.order.config.AlipayTemplate;
import cn.lhx.mall.order.service.OrderService;
import cn.lhx.mall.order.vo.PayVo;
import com.alipay.api.AlipayApiException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @author lee549
 * @date 2021/3/2 14:28
 */
@Controller
public class PayWebController {

    @Resource
    private AlipayTemplate alipayTemplate;
    @Resource
    private OrderService orderService;

    @ResponseBody
    @GetMapping(value = "/payOrder",produces = "text/html")
    public String payOrder(@RequestParam("orderSn")String orderSn) throws AlipayApiException {
        PayVo payVo = orderService.getOrderPay(orderSn);
        return alipayTemplate.pay(payVo) ;
    }
}
