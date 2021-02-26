package cn.lhx.mall.ware.config;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lee549
 * @date 2021/2/25 14:11
 */
@Configuration
public class RabbitConfig {
    /**
     * 使用json序列化机制，进行消息转换
     * @return
     */
    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }


    /**
     * 随便监听，第一次连接rabbitmq 监听消息的时候会把 队列交换机等等创建
     * @param
     */
/*    @RabbitListener(queues = "stock.release.stock.queue")
    public void listener(Message message){
    }*/

    @Bean
    public Exchange stockEventExchange(){
        return new TopicExchange("stock-event-exchange",true,false);
    }
    @Bean
    public Queue stockReleaseQueue(){
        return new Queue("stock.release.stock.queue",true,false,false);
    }

    /**
     * 延迟队列
     * @return
     */
    @Bean
    public Queue stockDelayQueue(){
        Map<String,Object> arguments =new HashMap<>();
        arguments.put("x-dead-letter-exchange","stock-event-exchange");
        arguments.put("x-dead-letter-routing-key","stock.release");
        arguments.put("x-message-ttl",120000);
        return new Queue("stock.delay.queue",true,false,false,arguments);
    }
    @Bean
    public Binding stockReleaseBinding(){
        return new Binding("stock.release.stock.queue",
                Binding.DestinationType.QUEUE,
                "stock-event-exchange",
                "stock.release.#",
                null);
    }
    @Bean
    public Binding stockLockBinding(){
        return new Binding("stock.delay.queue",
                Binding.DestinationType.QUEUE,
                "stock-event-exchange",
                "stock.locked",
                null);
    }

}
