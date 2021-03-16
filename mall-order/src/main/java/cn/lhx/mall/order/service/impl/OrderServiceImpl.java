package cn.lhx.mall.order.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.lhx.common.exception.NoStockException;
import cn.lhx.common.to.SkuHasStockVo;
import cn.lhx.common.to.mq.OrderTo;
import cn.lhx.common.to.mq.SeckillOrderTo;
import cn.lhx.common.utils.R;
import cn.lhx.common.vo.MemberRespVo;
import cn.lhx.mall.order.constant.OrderConstant;
import cn.lhx.mall.order.dao.OrderItemDao;
import cn.lhx.mall.order.entity.OrderItemEntity;
import cn.lhx.mall.order.enume.OrderStatusEnum;
import cn.lhx.mall.order.feign.CartFeignService;
import cn.lhx.mall.order.feign.MemberFeignService;
import cn.lhx.mall.order.feign.ProductFeignService;
import cn.lhx.mall.order.feign.WmsFeignService;
import cn.lhx.mall.order.inteceptor.LoginUserInterceptor;
import cn.lhx.mall.order.service.OrderItemService;
import cn.lhx.mall.order.to.OrderCreateTo;
import cn.lhx.mall.order.vo.*;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.lhx.common.utils.PageUtils;
import cn.lhx.common.utils.Query;

import cn.lhx.mall.order.dao.OrderDao;
import cn.lhx.mall.order.entity.OrderEntity;
import cn.lhx.mall.order.service.OrderService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.annotation.Resource;


@Service("orderService")
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderEntity> implements OrderService {

    private final ThreadLocal<OrderSubmitVo> confirmVoThreadLocal = new ThreadLocal<>();
    @Resource
    private MemberFeignService memberFeignService;

    @Resource
    private CartFeignService cartFeignService;

    @Resource
    private ThreadPoolExecutor executor;

    @Resource
    private WmsFeignService wmsFeignService;

    @Resource
    private ProductFeignService productFeignService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private OrderItemService orderItemService;

    @Resource
    private RabbitTemplate rabbitTemplate;



    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OrderEntity> page = this.page(
                new Query<OrderEntity>().getPage(params),
                new QueryWrapper<OrderEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public OrderConfirmVo orderConfirm() throws ExecutionException, InterruptedException {
        OrderConfirmVo confirmVo = new OrderConfirmVo();
        MemberRespVo memberRespVo = LoginUserInterceptor.loginUser.get();
        //获取之前的请求 异步调用要获取该线程请求头信息，共享给其他线程
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();

        CompletableFuture<Void> getAddressFuture = CompletableFuture.runAsync(() -> {
            //每一个线程都要共享之前的请求数据，共享请求头等信息
            RequestContextHolder.setRequestAttributes(attributes);
            //1 远程查询收获地址
            List<MemberReceiveAddressVo> address = memberFeignService.getAddress(memberRespVo.getId());
            confirmVo.setAddress(address);
        }, executor);

        CompletableFuture<Void> catFuture = CompletableFuture.runAsync(() -> {
            //每一个线程都要共享之前的请求数据，共享请求头等信息
            RequestContextHolder.setRequestAttributes(attributes);
            // 2 远程查询购物车所有选中的购物项
            List<OrderItemVo> items = cartFeignService.getCurrentUserCartItems();
            confirmVo.setItems(items);
            // feign远程调用之前要构造请求，会调用很多的拦截器，需要实现拦截器，不然feign默认请求头不带cookie
        }, executor).thenRunAsync(() -> {
            //查询库存
            List<OrderItemVo> items = confirmVo.getItems();
            List<Long> skuIds = items.stream().map(OrderItemVo::getSkuId).collect(Collectors.toList());
            R r = wmsFeignService.getSkusHasStock(skuIds);
            List<SkuHasStockVo> data = r.getData(new TypeReference<List<SkuHasStockVo>>() {
            });
            Map<Long, Boolean> map = data.stream().collect(Collectors.toMap(SkuHasStockVo::getSkuId, SkuHasStockVo::getHasStock));
            confirmVo.setStocks(map);

        });

        //3 查询用户积分
        Integer integration = memberRespVo.getIntegration();
        confirmVo.setIntegration(integration);

        //4.其他数据自动计算

        //5 todo 防止重复令牌
        String token = UUID.randomUUID().toString().replace("-", "");
        stringRedisTemplate.opsForValue().set(OrderConstant.USER_ORDER_TOKEN_PREFIX + memberRespVo.getId(), token, 30, TimeUnit.MINUTES);
        confirmVo.setOrderToken(token);

        CompletableFuture.allOf(getAddressFuture, catFuture).get();
        return confirmVo;
    }
    //@GlobalTransactional
    @Transactional
    @Override
    public OrderSubmitResponseVo submitOrder(OrderSubmitVo vo) {
        OrderSubmitResponseVo responseVo = new OrderSubmitResponseVo();
        responseVo.setCode(0);
        //下单：创建订单 验证令牌 价格 锁库存
        MemberRespVo memberRespVo = LoginUserInterceptor.loginUser.get();
        confirmVoThreadLocal.set(vo);
        //1.验证令牌,必须保证原子性
        //脚本返回0 删除失败-1 删除成功
        String script = "if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";
        String token = vo.getOrderToken();
        Long res = stringRedisTemplate.execute(new DefaultRedisScript<Long>(script, Long.class), Arrays.asList(OrderConstant.USER_ORDER_TOKEN_PREFIX + memberRespVo.getId()), token);
        if (res == 0) {
            //验证失败
            responseVo.setCode(1);
            return responseVo;
        } else {
            //令牌验证成功
            //下单：创建订单 验证令牌 价格 锁库存
            //1.创建订单，订单项信息
            OrderCreateTo order = createOrder();
            //2.验价
            BigDecimal payAmount = order.getOrder().getPayAmount();
            BigDecimal payPrice = vo.getPayPrice();
            //绝对值差价小于0.01
            if (Math.abs(payAmount.subtract(payPrice).doubleValue())<0.01){
                //金额对比
                //3.保存订单
                saveOrder(order);
                //4.锁定库存
                WareSkuLockVo lockVo = new WareSkuLockVo();
                lockVo.setOrderSn(order.getOrder().getOrderSn());
                List<OrderItemVo> locks = order.getOrderItems().stream().map(item -> {
                    OrderItemVo orderItemVo = new OrderItemVo();
                    orderItemVo.setSkuId(item.getSkuId());
                    orderItemVo.setCount(item.getSkuQuantity());
                    orderItemVo.setTitle(item.getSkuName());
                    return orderItemVo;
                }).collect(Collectors.toList());
                lockVo.setLocks(locks);
                //远程锁库存
                // 为了保证高并发，不适合使用Seata 的AT模式，可以发消息给库存服务；
                //库存服务本身也可以自动解锁  消息队列
                //TODO 本地事务下此处可能调用成功，但是可能网络波动返回超时导致异常回滚订单服务，远程不滚
                R r = wmsFeignService.orderLockStock(lockVo);
                if (r.getCode() == 0){
                    //锁定成功
                    responseVo.setOrder(order.getOrder());
                    //TODO 本地事务下此处如果出现异常会导致 //订单回滚，远程服务不回滚。
                    //int i=10/0;//模拟订单回滚，库存不回滚
                    //TODO 订单创建成功发送消息给MQ
                    rabbitTemplate.convertAndSend("order-event-exchange","order.create.order",order.getOrder());
                    return responseVo;
                }else {
                    //锁定失败
                    responseVo.setCode(3);
                    throw new NoStockException("库存锁定失败，商品库存不足");
                }

            }else {
                //金额改变
                responseVo.setCode(2);
                throw new NoStockException("订单商品价格发生变化，请确认后再次提交");
            }

        }
    }

    @Override
    public OrderEntity getOrderByOrderSn(String orderSn) {
        OrderEntity order = this.getOne(new LambdaQueryWrapper<OrderEntity>().eq(OrderEntity::getOrderSn, orderSn));

        return order;
    }

    @Override
    public void closeOrder(OrderEntity entity) {
        //查询该订单的最新状态
        OrderEntity orderEntity = this.getById(entity.getId());
        if (orderEntity.getStatus().equals(OrderStatusEnum.CREATE_NEW.getCode())){
            //关单
            OrderEntity update = new OrderEntity();
            update.setId(entity.getId());
            update.setStatus(OrderStatusEnum.CANCLED.getCode());
            this.updateById(update);
            OrderTo orderTo = new OrderTo();
            BeanUtils.copyProperties(orderEntity,orderTo);
            //发给MQ
            try {
                //TODO 保证消息一定要发出去
                rabbitTemplate.convertAndSend("order-event-exchange","order.release.other",orderTo);
            }catch (Exception e){
            }
        }


    }

    @Override
    public PayVo getOrderPay(String orderSn) {
        PayVo payVo = new PayVo();
        OrderEntity order = this.getOrderByOrderSn(orderSn);
        payVo.setTotal_amount(order.getTotalAmount().setScale(2,BigDecimal.ROUND_UP).toString());

        List<OrderItemEntity> list = orderItemService.list(new LambdaQueryWrapper<OrderItemEntity>().eq(OrderItemEntity::getOrderSn, orderSn));
        OrderItemEntity itemEntity = list.get(0);
        payVo.setBody(itemEntity.getSkuAttrsVals());
        payVo.setSubject(itemEntity.getSkuName());
        payVo.setOut_trade_no(order.getOrderSn());
        return payVo;
    }

    @Override
    public PageUtils queryPageWithItem(Map<String, Object> params) {
        MemberRespVo memberRespVo = LoginUserInterceptor.loginUser.get();
        IPage<OrderEntity> page = this.page(
                new Query<OrderEntity>().getPage(params),
                new LambdaQueryWrapper<OrderEntity>().eq(OrderEntity::getMemberId,memberRespVo.getId()).orderByDesc(OrderEntity::getId)
        );
        List<OrderEntity> orderRecords = page.getRecords().stream().map(order -> {
            List<OrderItemEntity> list = orderItemService.list(new LambdaQueryWrapper<OrderItemEntity>().eq(OrderItemEntity::getOrderSn, order.getOrderSn()));
            order.setItemEntities(list);
            return order;
        }).collect(Collectors.toList());
        page.setRecords(orderRecords);
        return new PageUtils(page);


    }

    @Override
    public void createSeckillOrder(SeckillOrderTo seckillOrderTo) {
        //TODO 保存订单信息
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderSn(seckillOrderTo.getOrderSn());
        orderEntity.setMemberId(seckillOrderTo.getMemberId());
        orderEntity.setStatus(OrderStatusEnum.CREATE_NEW.getCode());
        //获取用户默认地址
        MemberReceiveAddressVo addrVo = memberFeignService.getDefaultAddr(seckillOrderTo.getMemberId());
        if (addrVo!=null){
            orderEntity.setReceiverName(addrVo.getName());
            orderEntity.setReceiverDetailAddress(addrVo.getDetailAddress());
            orderEntity.setReceiverProvince(addrVo.getProvince());
            orderEntity.setReceiverPhone(addrVo.getPhone());
            orderEntity.setReceiverCity(addrVo.getCity());
            orderEntity.setReceiverPostCode(addrVo.getPostCode());
            orderEntity.setReceiverRegion(addrVo.getRegion());
        }

        orderEntity.setModifyTime(new Date());
        orderEntity.setDeleteStatus(0);
        //获取收获地址信息
        // OrderSubmitVo submitVo = confirmVoThreadLocal.get();
        // R r = wmsFeignService.getFare(submitVo.getAddrId());
        // FareVo fareVo = r.getData(new TypeReference<FareVo>() {
        // });

        BigDecimal multiply = seckillOrderTo.getSeckillPrice().multiply(new BigDecimal("" + seckillOrderTo.getNum()));
        orderEntity.setPayAmount(multiply);
        orderEntity.setTotalAmount(multiply);
        //设置运费信息
        // orderEntity.setFreightAmount(fareVo.getFare());


        this.save(orderEntity);
        //订单项信息
        OrderItemEntity orderItemEntity = new OrderItemEntity();
        orderItemEntity.setOrderSn(seckillOrderTo.getOrderSn());
        //orderItemEntity.setRealAmount(multiply);
        orderItemEntity.setSkuQuantity(seckillOrderTo.getNum());

        //TODO 获取sku详细信息 prodcutFeignService.getSpuInfoByskuId();
        R spuInfoBySkuId = productFeignService.getSpuInfoBySkuId(seckillOrderTo.getSkuId());
        SpuInfoVo data = spuInfoBySkuId.getData(new TypeReference<SpuInfoVo>() {
        });
        orderItemEntity.setSpuId(data.getId());
        orderItemEntity.setSpuName(data.getSpuName());
        orderItemEntity.setSpuBrand(data.getBrandId().toString());
        orderItemEntity.setCategoryId(data.getCatalogId());

        R skuInfoBySkuId = productFeignService.getSkuInfoBySkuId(seckillOrderTo.getSkuId());
        SkuInfoVo skuInfo = skuInfoBySkuId.getData("skuInfo", new TypeReference<SkuInfoVo>() {
        });

        orderItemEntity.setSkuId(seckillOrderTo.getSkuId());
        orderItemEntity.setSkuPic(skuInfo.getSkuDefaultImg());
        orderItemEntity.setSkuName(skuInfo.getSkuName());
        orderItemEntity.setSkuPrice(skuInfo.getPrice());

        List<String> skuAttrs = productFeignService.skuSaleAttrValues(seckillOrderTo.getSkuId());
        String skuAttr = StringUtils.collectionToDelimitedString(skuAttrs, ";");
        orderItemEntity.setSkuAttrsVals(skuAttr);


        orderItemService.save(orderItemEntity);

    }

    /**
     * 保存订单数据
     * @param order
     */
    private void saveOrder(OrderCreateTo order) {
        OrderEntity orderEntity = order.getOrder();
        orderEntity.setModifyTime(new Date());
        this.save(orderEntity);

        List<OrderItemEntity> orderItems = order.getOrderItems();
        orderItemService.saveBatch(orderItems);

    }

    /**
     * 创建订单
     *
     * @return
     */
    private OrderCreateTo createOrder() {
        OrderCreateTo createTo = new OrderCreateTo();
        //1.生成订单
        String orderSn = IdWorker.getTimeId();
        //创建订单号
        OrderEntity orderEntity = buildOrder(orderSn);

        //2.订单项信息
        List<OrderItemEntity> itemEntities = buildOrderItems(orderSn);

        //3.计算价格、积分相关信息
        computePrice(orderEntity,itemEntities);

        createTo.setOrder(orderEntity);
        createTo.setOrderItems(itemEntities);
        return createTo;
    }

    private void computePrice(OrderEntity orderEntity, List<OrderItemEntity> itemEntities) {
        //1.订单价格相关
        BigDecimal total = new BigDecimal("0.0");
        //优惠券
        BigDecimal coupon = new BigDecimal("0.0");
        //积分
        BigDecimal integration = new BigDecimal("0.0");
        //打折
        BigDecimal promotion = new BigDecimal("0.0");
        //可获得的 积分 成长值
        BigDecimal growth = new BigDecimal("0.0");

        BigDecimal giftIntegration = new BigDecimal("0.0");


        //订单的总额，叠加每个订单项的总额
        for (OrderItemEntity entity : itemEntities) {
            coupon = coupon.add(entity.getCouponAmount());
            integration = integration.add(entity.getIntegrationAmount());
            promotion = promotion.add(entity.getPromotionAmount());
            total = total.add(entity.getRealAmount());
            growth = growth.add(new BigDecimal(entity.getGiftGrowth().toString()));
            giftIntegration = giftIntegration.add(new BigDecimal(entity.getGiftIntegration().toString()));
        }
        //1.订单价格相关
        orderEntity.setTotalAmount(total);
        //应付总额 +上运费
        orderEntity.setPayAmount(total.add(orderEntity.getFreightAmount()));
        orderEntity.setPromotionAmount(promotion);
        orderEntity.setIntegrationAmount(integration);
        orderEntity.setCouponAmount(coupon);
        //设置积分等信息
        orderEntity.setIntegration(giftIntegration.intValue());
        orderEntity.setGrowth(growth.intValue());

        orderEntity.setDeleteStatus(0);//未删除


    }

    /**
     * 构建订单
     * @param orderSn
     * @return
     */
    private OrderEntity buildOrder(String orderSn) {
        MemberRespVo memberRespVo = LoginUserInterceptor.loginUser.get();
        OrderEntity entity = new OrderEntity();
        entity.setOrderSn(orderSn);
        entity.setMemberId(memberRespVo.getId());
        entity.setMemberUsername(memberRespVo.getUsername());
        //获取收获地址信息
        OrderSubmitVo submitVo = confirmVoThreadLocal.get();
        R r = wmsFeignService.getFare(submitVo.getAddrId());
        FareVo fareVo = r.getData(new TypeReference<FareVo>() {
        });
        //设置运费信息
        entity.setFreightAmount(fareVo.getFare());
        //设置收货人相关信息
        entity.setReceiverCity(fareVo.getAddress().getCity());
        entity.setReceiverDetailAddress(fareVo.getAddress().getDetailAddress());
        entity.setReceiverName(fareVo.getAddress().getName());
        entity.setReceiverPhone(fareVo.getAddress().getPhone());
        entity.setReceiverPostCode(fareVo.getAddress().getPostCode());
        entity.setReceiverProvince(fareVo.getAddress().getProvince());
        entity.setReceiverRegion(fareVo.getAddress().getRegion());
        //设置订单状态
        entity.setStatus(OrderStatusEnum.CREATE_NEW.getCode());
        entity.setAutoConfirmDay(7);
        return entity;
    }

    /**
     * 构建所有订单项数据
     * @return
     */
    private List<OrderItemEntity> buildOrderItems(String orderSn) {
        //最后确认价格
        List<OrderItemVo> currentUserCartItems = cartFeignService.getCurrentUserCartItems();
        if (currentUserCartItems != null && currentUserCartItems.size() > 0) {
            List<OrderItemEntity> itemEntities = currentUserCartItems.stream().map(cartItem -> {
                OrderItemEntity itemEntity = buildOrderItem(cartItem);
                itemEntity.setOrderSn(orderSn);
                return itemEntity;
            }).collect(Collectors.toList());
            return itemEntities;
        }
        return null;
    }

    /**
     * 构建某一个订单项内容
     * @param cartItem
     * @return
     */
    private OrderItemEntity buildOrderItem(OrderItemVo cartItem) {
        OrderItemEntity itemEntity = new OrderItemEntity();
        //1.订单信息 订单号
        //2.商品spu信息
        Long skuId = cartItem.getSkuId();
        R r = productFeignService.getSpuInfoBySkuId(skuId);
        SpuInfoVo data = r.getData(new TypeReference<SpuInfoVo>() {
        });
        itemEntity.setSpuId(data.getId());
        itemEntity.setSpuName(data.getSpuName());
        itemEntity.setSpuBrand(data.getBrandId().toString());
        itemEntity.setCategoryId(data.getCatalogId());
        //3.商品的sku信息
        itemEntity.setSkuId(cartItem.getSkuId());
        itemEntity.setSkuName(cartItem.getTitle());
        itemEntity.setSkuPic(cartItem.getImg());
        itemEntity.setSkuPrice(cartItem.getPrice());
        String skuAttr = StringUtils.collectionToDelimitedString(cartItem.getSkuAttr(), ";");
        itemEntity.setSkuAttrsVals(skuAttr);
        itemEntity.setSkuQuantity(cartItem.getCount());
        //4.优惠信息
        //5.积分信息
        itemEntity.setGiftGrowth(cartItem.getPrice().multiply(new BigDecimal(cartItem.getCount().toString())).intValue());
        itemEntity.setGiftIntegration(cartItem.getPrice().multiply(new BigDecimal(cartItem.getCount().toString())).intValue());
        //6.订单项的价格
        itemEntity.setPromotionAmount(new BigDecimal("0"));
        itemEntity.setCouponAmount(new BigDecimal("0"));
        itemEntity.setIntegrationAmount(new BigDecimal("0"));
        //订单项的实际金额 : 总额-优惠
        BigDecimal origin =itemEntity.getSkuPrice().multiply(new BigDecimal(itemEntity.getSkuQuantity().toString()));
        BigDecimal real = origin.subtract(itemEntity.getCouponAmount())
                .subtract(itemEntity.getPromotionAmount())
                .subtract(itemEntity.getIntegrationAmount());
        itemEntity.setRealAmount(real);
        return itemEntity;
    }


}
