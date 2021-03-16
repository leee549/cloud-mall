package cn.lhx.mall.order.listener;

import cn.lhx.common.to.mq.SeckillOrderTo;
import cn.lhx.mall.order.entity.OrderEntity;
import cn.lhx.mall.order.service.OrderService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author lee549
 * @date 2021/3/12 14:29
 */
@Component
@RabbitListener(queues = "order.seckill.order.queue")
public class OrderSeckillListener {

    @Resource
    private OrderService orderService;

    @RabbitHandler
    public void listener(SeckillOrderTo seckillOrderTo, Channel channel, Message message) throws IOException {
        try {
            System.out.println("准备创建秒杀单："+seckillOrderTo.getOrderSn());
            orderService.createSeckillOrder(seckillOrderTo);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        }catch (Exception e){
            channel.basicReject(message.getMessageProperties().getDeliveryTag(),true);
        }

    }
}
