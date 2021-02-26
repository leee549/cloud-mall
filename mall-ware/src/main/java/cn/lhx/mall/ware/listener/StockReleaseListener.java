package cn.lhx.mall.ware.listener;

import cn.lhx.common.to.mq.StockDetailTo;
import cn.lhx.common.to.mq.StockLockedTo;
import cn.lhx.common.utils.R;
import cn.lhx.mall.ware.entity.WareOrderTaskDetailEntity;
import cn.lhx.mall.ware.entity.WareOrderTaskEntity;
import cn.lhx.mall.ware.service.WareSkuService;
import cn.lhx.mall.ware.vo.OrderVo;
import com.alibaba.fastjson.TypeReference;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author lee549
 * @date 2021/2/26 15:16
 */
@Service
@RabbitListener(queues = "stock.release.stock.queue")
public class StockReleaseListener {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private WareSkuService wareSkuService;

    /**
     * 1库存自动解锁
     *  下单成功，库存锁定成功，业务异常回滚，导致订单回滚。之前锁定的库存要回滚
     *  2.订单失败
     *      锁库存失败
     *
     *  只要解锁库存的消息失败。一定要告诉服务解锁失败
     * @param
     * @return
     */
    @RabbitHandler
    public void handleStockLockedRelease(StockLockedTo to, Message message, Channel channel) throws IOException {
        try {
            logger.info("收到解锁库存的消息");
            wareSkuService.unlockStock(to);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        }catch (Exception e){
            //重新回队
            channel.basicReject(message.getMessageProperties().getDeliveryTag(),true);
        }


    }
}
