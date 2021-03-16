package cn.lhx.mall.order.config;

import cn.lhx.mall.order.entity.OrderEntity;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lee549
 * @date 2021/2/25 10:39
 */
@Configuration
public class MyRabbitMQConfig {
    /**
     * 第一次连接rabbitmq 监听消息的时候会把 队列交换机等等创建
     * @param entity
     * @param channel
     * @param message
     * @throws IOException
     */


    /**
     * binding que exchange 都会自动创建，前提是没有
     * @return
     */
    @Bean
    public Queue orderDelayQueue(){
        //String name, boolean durable, boolean exclusive, boolean autoDelete,@Nullable Map<String, Object> arguments
        Map<String,Object> arguments =new HashMap<>();
        arguments.put("x-dead-letter-exchange","order-event-exchange");
        arguments.put("x-dead-letter-routing-key","order.release.order");
        arguments.put("x-message-ttl",60000);
        return new Queue("order.delay.queue", true, false, false,arguments);
    }
    @Bean
    public Queue orderReleaseQueue(){
        return new Queue("order.release.order.queue", true, false, false);

    }
    @Bean
    public Exchange orderEventExchange(){
        //String name, boolean durable, boolean autoDelete, Map<String, Object> arguments
        return new TopicExchange("order-event-exchange",true,false);
    }
    @Bean
    public Binding orderCreateOrderBinding(){
       return new Binding("order.delay.queue",
                Binding.DestinationType.QUEUE,
                "order-event-exchange",
                "order.create.order",
               null);
    }
    @Bean
    public Binding orderReleaseOrderBinding(){
        return new Binding("order.release.order.queue",
                Binding.DestinationType.QUEUE,
                "order-event-exchange",
                "order.release.order",
                null);
    }

    /**
     * 订单释放直接和库存释放进行绑定
     * @return
     */
    @Bean
    public Binding orderReleaseOtherBinding(){
        return new Binding("order.release.stock.queue",
                Binding.DestinationType.QUEUE,
                "order-event-exchange",
                "order.release.other.#",
                null);
    }

    /**
     * 秒杀队列
     * @return
     */
    @Bean
    public Queue orderSeckillOrderQueue(){
        return new Queue("order.seckill.order.queue",
                true,
                false,
                false);
    }

    /**
     * 交换机与秒杀队列绑定关系
     * @return
     */
    @Bean
    public Binding orderSeckillOrderQueueBinding(){
        return new Binding("order.seckill.order.queue",
                Binding.DestinationType.QUEUE,
                "order-event-exchange",
                "order.seckill.order",null);
    }
}
