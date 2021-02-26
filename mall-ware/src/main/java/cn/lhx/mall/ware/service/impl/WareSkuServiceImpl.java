package cn.lhx.mall.ware.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.lhx.common.exception.NoStockException;
import cn.lhx.common.to.mq.StockDetailTo;
import cn.lhx.common.to.mq.StockLockedTo;
import cn.lhx.common.utils.R;
import cn.lhx.mall.ware.entity.WareOrderTaskDetailEntity;
import cn.lhx.mall.ware.entity.WareOrderTaskEntity;
import cn.lhx.mall.ware.feign.OrderFeignService;
import cn.lhx.mall.ware.feign.ProductFeignService;
import cn.lhx.mall.ware.service.WareOrderTaskDetailService;
import cn.lhx.mall.ware.service.WareOrderTaskService;
import cn.lhx.mall.ware.vo.OrderItemVo;
import cn.lhx.mall.ware.vo.OrderVo;
import cn.lhx.mall.ware.vo.SkuHasStockVo;
import cn.lhx.mall.ware.vo.WareSkuLockVo;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rabbitmq.client.Channel;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.lhx.common.utils.PageUtils;
import cn.lhx.common.utils.Query;

import cn.lhx.mall.ware.dao.WareSkuDao;
import cn.lhx.mall.ware.entity.WareSkuEntity;
import cn.lhx.mall.ware.service.WareSkuService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;


@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {

    @Resource
    private WareSkuDao wareSkuDao;
    @Resource
    ProductFeignService productFeignService;
    @Resource
    RabbitTemplate rabbitTemplate;
    @Resource
    private WareOrderTaskDetailService wareOrderTaskDetailService;
    @Resource
    private WareOrderTaskService wareOrderTaskService;
    @Resource
    private OrderFeignService orderFeignService;


    private void unLockStock(Long skuId, Long wareId, Integer num, Long taskDetailId) {
        //库存解锁
        wareSkuDao.unLockStock(skuId, wareId, num);
        //库存工资单修改状态
        WareOrderTaskDetailEntity entity = new WareOrderTaskDetailEntity();
        entity.setId(taskDetailId);
        entity.setLockStatus(2);
        wareOrderTaskDetailService.updateById(entity);
    }


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<WareSkuEntity> qw = new LambdaQueryWrapper<>();
        String skuId = (String) params.get("skuId");
        if (!StringUtils.isEmpty(skuId)) {
            qw.eq(WareSkuEntity::getSkuId, skuId);
        }
        String wareId = (String) params.get("ware_id");
        if (!StringUtils.isEmpty(wareId)) {
            qw.eq(WareSkuEntity::getWareId, wareId);
        }
        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params),
                qw
        );

        return new PageUtils(page);
    }

    @Override
    public void addStock(Long skuId, Long wareId, Integer skuNum) {
        //判断是否有这个库存没有就新增
        LambdaQueryWrapper<WareSkuEntity> qw = new LambdaQueryWrapper<>();
        qw.eq(WareSkuEntity::getSkuId, skuId)
                .eq(WareSkuEntity::getWareId, wareId);
        List<WareSkuEntity> entities = this.list(qw);
        if (entities == null || entities.size() == 0) {
            WareSkuEntity wareSkuEntity = new WareSkuEntity();
            wareSkuEntity.setSkuId(skuId);
            wareSkuEntity.setWareId(wareId);
            wareSkuEntity.setStock(skuNum);
            wareSkuEntity.setStockLocked(0);
            //远程查询skuname
            try {
                R info = productFeignService.info(skuId);
                Map<String, Object> data = (Map<String, Object>) info.get("skuInfo");

                if (info.getCode() == 0) {
                    wareSkuEntity.setSkuName((String) data.get("skuName"));
                }
            } catch (Exception e) {

            }

            wareSkuDao.insert(wareSkuEntity);
        } else {
            wareSkuDao.addStock(skuId, wareId, skuNum);

        }
    }

    @Override
    public List<SkuHasStockVo> getSkusHasStock(List<Long> skuIds) {

        List<SkuHasStockVo> collect = skuIds.stream().map(skuId -> {
            SkuHasStockVo vo = new SkuHasStockVo();
            Long count = baseMapper.getSkuStock(skuId);
            vo.setSkuId(skuId);
            vo.setHasStock(count == null ? false : count > 0);
            return vo;
        }).collect(Collectors.toList());
        return collect;

    }

    /**
     * 为订单锁定库存
     *
     * @param vo 库存解锁场景
     *           1.下单成功。订单过期没有支付自动解锁、被用户取消订单
     *           2.下订单成功，库存锁定成功，接下来的业务失败导致回滚，之前锁定的库存就要解锁，（使用seata太慢）自动解锁
     * @return
     */
    @Transactional
    @Override
    public Boolean orderLockStock(WareSkuLockVo vo) {
        /**
         * 保存工资单详情
         */
        WareOrderTaskEntity taskEntity = new WareOrderTaskEntity();
        taskEntity.setOrderSn(vo.getOrderSn());
        wareOrderTaskService.save(taskEntity);
        //TODO 1.按照下单的收获地址找到最近的仓库，锁定库存（不做）
        //1.找到每个商品在那个仓库有库存
        List<OrderItemVo> locks = vo.getLocks();
        List<SkuWareHasStock> collect = locks.stream().map(item -> {
            SkuWareHasStock stock = new SkuWareHasStock();
            Long skuId = item.getSkuId();
            //查询这个商品在哪个仓库有库存
            List<Long> wareIds = wareSkuDao.listWareIdHasSkuStock(skuId);
            stock.setSkuId(skuId);
            stock.setWareId(wareIds);
            stock.setNum(item.getCount());
            return stock;
        }).collect(Collectors.toList());

        //2.锁定库存
        for (SkuWareHasStock hasStock : collect) {
            boolean skuStocked = false;
            Long skuId = hasStock.getSkuId();
            List<Long> wareIds = hasStock.getWareId();
            Integer num = hasStock.getNum();
            if (wareIds == null || wareIds.size() == 0) {
                //没有任何仓库有库存
                throw new NoStockException(skuId);
            }
            for (Long wareId : wareIds) {
                //成功就影响1行 否则0行
                Long count = wareSkuDao.lockSkuStock(skuId, wareId, num);
                if (count == 1) {
                    skuStocked = true;
                    //TODO 告诉mq库存锁定成功
                    WareOrderTaskDetailEntity orderTaskDetailEntity = new WareOrderTaskDetailEntity(null, skuId, "", hasStock.getNum(), taskEntity.getId(), wareId, 1);
                    wareOrderTaskDetailService.save(orderTaskDetailEntity);
                    StockLockedTo lockedTo = new StockLockedTo();
                    lockedTo.setId(taskEntity.getId());
                    StockDetailTo stockDetailTo = new StockDetailTo();
                    BeanUtil.copyProperties(orderTaskDetailEntity, stockDetailTo);
                    lockedTo.setDetail(stockDetailTo);
                    rabbitTemplate.convertAndSend("stock-event-exchange", "stock.locked", lockedTo);
                    break;
                } else {
                    //当前仓库锁失败重试下一个仓库
                }
            }
            if (!skuStocked) {
                //当前商品所有仓库都没锁住
                throw new NoStockException(skuId);
            }
        }
        //3.全部都锁成功
        return true;
    }

    @Override
    public void unlockStock(StockLockedTo to) {

        StockDetailTo detail = to.getDetail();
        Long detailId = detail.getId();
        //解锁
        //1.查询数据库关于这个订单的锁定库存信息。
        //有；证明库存锁定成功
        //      解锁订单
        //          1.没这个订单，必须解锁
        //          2.有这个订单，不解锁
        //                  订单状态： 已经取消：解锁
        //                              没取消：不能解锁
        //没有，库存锁定失败，库存回滚。这情况无需解锁
        WareOrderTaskDetailEntity byId = wareOrderTaskDetailService.getById(detailId);
        if (byId != null) {
            //解锁
            Long id = to.getId();
            WareOrderTaskEntity taskEntity = wareOrderTaskService.getById(id);
            String orderSn = taskEntity.getOrderSn();//根据订单号查询订单的状态
            R r = orderFeignService.getOrderStatus(orderSn);
            if (r.getCode() == 0) {
                //订单数据返回成功
                OrderVo data = r.getData(new TypeReference<OrderVo>() {
                });
                if (data == null || data.getStatus() == 4) {
                    //订单已经被取消，可以解锁
                    //订单不存在，解锁
                    if (byId.getLockStatus()==1){
                        //工作单详情状态是锁定状态1才能解锁
                        unLockStock(detail.getSkuId(), detail.getWareId(), detail.getSkuNum(), detailId);
                    }
                }
            } else {
                throw new RuntimeException("远程服务调用失败");
            }

        } else {
            //无需解锁
        }

    }

    @Data
    static class SkuWareHasStock {
        private Long skuId;
        private List<Long> wareId;
        //锁几件
        private Integer num;
    }

}
