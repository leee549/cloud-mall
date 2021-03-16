package cn.lhx.mall.seckill.config;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author lee549
 * @date 2020/12/2 15:14
 */
@Configuration
public class RabbitConfig {

    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 使用json序列化机制，进行消息转换
     * @return
     */
    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }
    // @PostConstruct
    // public void initRabbitTemplate(){
    //     //设置消息抵达交换机确认回调
    //     rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
    //         /**
    //          * 1.只要消息抵达broker就ack=true
    //          * @param correlationData 当前消息的唯一id
    //          * @param ack 消息是否接收到
    //          * @param cause 失败的原因
    //          */
    //         @Override
    //         public void confirm(CorrelationData correlationData, boolean ack, String cause) {
    //
    //         }
    //     });
    //     //设置消息从交换机抵达队列回调
    //     rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
    //         /**
    //          * 只有消息没成功抵达队列才触发回调
    //          * @param message 失败的消息详细内容
    //          * @param replyCode 回复的状态码
    //          * @param replyText 回复的文本
    //          * @param exchange 当时这个消息是发送给哪个交换机
    //          * @param routingKey 当时这个消息的路由键是
    //          */
    //         @Override
    //         public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
    //
    //         }
    //     });
    // }

}
