package cn.lhx.mall.order.web;

import cn.lhx.mall.order.entity.OrderEntity;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.UUID;

/**
 * @author lee549
 * @date 2020/12/4 9:54
 */
@Controller
public class HelloController {
    @Autowired
    RabbitTemplate rabbitTemplate;

    @RequestMapping("/{page}.html")
    public String hello(@PathVariable("page")String page){
        return page;
    }

    @ResponseBody
    @RequestMapping("/test/createOrder")
    public String createOrder(){
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderSn(UUID.randomUUID().toString());
        orderEntity.setModifyTime(new Date());
        rabbitTemplate.convertAndSend("order-event-exchange","order.create.order",orderEntity);
        return "ok";
    }
}
