package cn.lhx.mall.order.listener;

import cn.lhx.mall.order.entity.OrderEntity;
import cn.lhx.mall.order.service.OrderService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author lee549
 * @date 2021/2/26 16:07
 */
@RabbitListener(queues = "order.release.order.queue")
@Service
public class OrderCloseListener {

    @Resource
    private OrderService orderService;

    @RabbitHandler
    public void listener(OrderEntity entity, Channel channel, Message message) throws IOException {
        try {
            System.out.println("收到过期的订单，准备关闭："+entity.getOrderSn());
            orderService.closeOrder(entity);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        }catch (Exception e){
            channel.basicReject(message.getMessageProperties().getDeliveryTag(),true);
        }

    }
}
