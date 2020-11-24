package cn.lhx.mall.cart.service;

import cn.lhx.mall.cart.vo.CartItem;

import java.util.concurrent.ExecutionException;

/**
 * @author lee549
 * @date 2020/11/20 21:10
 */
public interface CartService {
    /**
     * 将商品添加到购物车
     *
     * @param skuId
     * @param num
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    CartItem addToCart(Long skuId, Integer num) throws ExecutionException, InterruptedException;

    /**
     * 获取购物车中某个购物项
     *
     * @param skuId
     * @return
     */
    CartItem getCartItem(Long skuId);
}
