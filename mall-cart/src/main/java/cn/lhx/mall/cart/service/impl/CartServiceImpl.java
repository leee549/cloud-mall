package cn.lhx.mall.cart.service.impl;

import cn.hutool.json.JSONUtil;
import cn.lhx.common.utils.R;
import cn.lhx.mall.cart.feign.ProductFeignService;
import cn.lhx.mall.cart.interceptor.CartInterceptor;
import cn.lhx.mall.cart.service.CartService;
import cn.lhx.mall.cart.vo.CartItem;
import cn.lhx.mall.cart.vo.SkuInfoVo;
import cn.lhx.mall.cart.vo.UserInfoTo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author lee549
 * @date 2020/11/20 21:11
 */
@Service
public class CartServiceImpl implements CartService {
    @Resource(name = "stringRedisTemplate")
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    ThreadPoolExecutor executor;

    @Resource
    private ProductFeignService productFeignService;
    public final String CART_PREFIX = "mall:cart:";

    @Override
    public CartItem addToCart(Long skuId, Integer num) throws ExecutionException, InterruptedException {
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        String res = (String) cartOps.get(skuId.toString());
        if (StringUtils.isEmpty(res)) {
            CartItem cartItem = new CartItem();
            //新商品添加到购物车
            CompletableFuture<Void> getSkuInfo = CompletableFuture.runAsync(() -> {
                //1 远程查询 商品信息
                R skuInfo = productFeignService.getSkuInfo(skuId);
                SkuInfoVo info = skuInfo.getData("skuInfo", new TypeReference<SkuInfoVo>() {
                });
                cartItem.setCount(num);
                cartItem.setCheck(true);
                cartItem.setSkuId(skuId);
                cartItem.setImg(info.getSkuDefaultImg());
                cartItem.setTitle(info.getSkuTitle());
                cartItem.setPrice(info.getPrice());
            }, executor);

            //远程查询 sku组合信息
            CompletableFuture<Void> getSkuSaleAttrValues = CompletableFuture.runAsync(() -> {
                List<String> values = productFeignService.skuSaleAttrValues(skuId);
                cartItem.setSkuAttr(values);
            }, executor);

            CompletableFuture.allOf(getSkuInfo, getSkuSaleAttrValues).get();
            String s = JSON.toJSONString(cartItem);
            cartOps.put(skuId.toString(), s);
            return cartItem;
        } else {
            //购物车有此商品，增加数量
            CartItem cartItem = JSON.parseObject(res, CartItem.class);
            cartItem.setCount(cartItem.getCount() + num);
            cartOps.put(skuId.toString(), JSON.toJSONString(cartItem));
            return cartItem;
        }
    }

    @Override
    public CartItem getCartItem(Long skuId) {
        BoundHashOperations<String, Object, Object> ops = getCartOps();
        String str = (String) ops.get(skuId.toString());
        CartItem cartItem = JSON.parseObject(str, CartItem.class);
        return cartItem;
    }

    /**
     * 获取要操作的购物车
     *
     * @return
     */
    private BoundHashOperations<String, Object, Object> getCartOps() {
        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();
        //1.
        String cartKey = "";
        if (userInfoTo.getUserId() != null) {
            //登录购物车
            cartKey = CART_PREFIX + userInfoTo.getUserId();
        } else {
            //临时购物车
            cartKey = CART_PREFIX + userInfoTo.getUserKey();
        }
        return stringRedisTemplate.boundHashOps(cartKey);

    }
}
