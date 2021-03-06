package cn.lhx.mall.cart.service.impl;

import cn.lhx.common.utils.R;
import cn.lhx.mall.cart.feign.ProductFeignService;
import cn.lhx.mall.cart.interceptor.CartInterceptor;
import cn.lhx.mall.cart.service.CartService;
import cn.lhx.mall.cart.vo.Cart;
import cn.lhx.mall.cart.vo.CartItem;
import cn.lhx.mall.cart.vo.SkuInfoVo;
import cn.lhx.mall.cart.vo.UserInfoTo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

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

    @Override
    public Cart getCart() throws ExecutionException, InterruptedException {
        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();
        Cart cart = new Cart();
        if (userInfoTo.getUserId() != null) {
            //登录
            String cartKey = CART_PREFIX + userInfoTo.getUserId();
            //如果有临时购物车
            String tempCartKey = CART_PREFIX + userInfoTo.getUserKey();
            List<CartItem> tempCartItems = getCartItems(tempCartKey);
            if (tempCartItems != null) {
                //有数据则需要合并
                for (CartItem item : tempCartItems) {
                    addToCart(item.getSkuId(), item.getCount());
                }
                //合并后需要清空购物车
                clearCart(tempCartKey);
            }
            //获取登陆后的购物车所有数据,包含合并的数据
            List<CartItem> cartItem = getCartItems(cartKey);
            cart.setItems(cartItem);
        } else {
            // 没登录
            String cartKey = CART_PREFIX + userInfoTo.getUserKey();
            //获取临时购物车的所有数据
            List<CartItem> cartItem = getCartItems(cartKey);
            cart.setItems(cartItem);
        }
        return cart;
    }

    @Override
    public void clearCart(String cartKey) {
        stringRedisTemplate.delete(cartKey);
    }

    @Override
    public void checkItem(Long skuId, Integer check) {
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        CartItem cartItem = getCartItem(skuId);
        cartItem.setCheck(check==1?true:false);
        String s = JSON.toJSONString(cartItem);
        cartOps.put(skuId.toString(),s);

    }

    @Override
    public void changeItemCount(Long skuId, Integer num) {
        CartItem cartItem = getCartItem(skuId);
        cartItem.setCount(num);
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        cartOps.put(skuId.toString(),JSON.toJSONString(cartItem));
    }

    @Override
    public void deleteItem(Long skuId) {
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        cartOps.delete(skuId.toString());
    }

    @Override
    public List<CartItem> getUserCartItems() {
        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();
        if (userInfoTo.getUserId()==null){
            return null;
        }else {
            String cartKey = CART_PREFIX + userInfoTo.getUserId();
            List<CartItem> cartItems = getCartItems(cartKey);

            //获取所有选中的购物项
            List<CartItem> collect = cartItems.stream()
                    .filter(CartItem::getCheck)
                    .map(item->{
                        //设置为最新的价格
                        R r = productFeignService.getSkuPrice(item.getSkuId());
                        String data = r.getData(new TypeReference<String>() {
                        });
                        item.setPrice(new BigDecimal(data));
                        return item;
                    })
                    .collect(Collectors.toList());

            return collect;
        }
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

    private List<CartItem> getCartItems(String cartKey) {
        BoundHashOperations<String, Object, Object> hashOps = stringRedisTemplate.boundHashOps(cartKey);
        List<Object> values = hashOps.values();
        if (values != null && values.size() > 0) {
            List<CartItem> collect = values.stream().map(obj -> {
                String str = (String) obj;
                CartItem cartItem = JSON.parseObject(str, CartItem.class);
                return cartItem;
            }).collect(Collectors.toList());
            return collect;
        }

        return null;
    }

}
