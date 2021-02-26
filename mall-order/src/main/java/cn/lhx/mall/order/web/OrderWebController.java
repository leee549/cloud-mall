package cn.lhx.mall.order.web;

import cn.lhx.common.exception.NoStockException;
import cn.lhx.mall.order.service.OrderService;
import cn.lhx.mall.order.vo.OrderConfirmVo;
import cn.lhx.mall.order.vo.OrderSubmitResponseVo;
import cn.lhx.mall.order.vo.OrderSubmitVo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;

/**
 * @author lee549
 * @date 2020/12/4 15:46
 */
@Controller
public class OrderWebController {
    @Resource
    private OrderService orderService;

    /**
     * 去结算页
     * @param model
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @GetMapping("/toTrade")
    public String toTrade(Model model) throws ExecutionException, InterruptedException {
        OrderConfirmVo orderConfirmVo = orderService.orderConfirm();
        model.addAttribute("orderConfirmData",orderConfirmVo);
        return "confirm";
    }

    /**
     * 下单功能
     * @param vo
     * @return
     */
    @PostMapping("/submitOrder")
    public String submitOrder(OrderSubmitVo vo, Model model, RedirectAttributes redirectAttributes){

        try {
            OrderSubmitResponseVo responseVo = orderService.submitOrder(vo);
            if (responseVo.getCode()==0){
                //下单成功
                model.addAttribute("submitOrderResp",responseVo);
                return "pay";
            }else {
                String msg = "提交订单失败，";
                switch (responseVo.getCode()){
                    case 1 : msg+="订单信息过期，请刷新后再次提交";break;
                    case 2 : msg+="订单商品价格发生变化，请确认后再次提交";break;
                    case 3 : msg+="库存锁定失败，商品库存不足";break;
                }
                redirectAttributes.addFlashAttribute("msg",msg);
                return "redirect:http://order.mall.com/toTrade";
            }
        }catch (Exception e){
            if (e instanceof NoStockException){
                String msg = e.getMessage();
                redirectAttributes.addFlashAttribute("msg",msg);
            }
            return "redirect:http://order.mall.com/toTrade";
        }

    }


}
