package cn.lhx.mall.cart.service;

import cn.lhx.mall.cart.vo.CartItem;

import java.util.concurrent.ExecutionException;

/**
 * @author lee549
 * @date 2020/11/20 21:10
 */
public interface CartService {
    CartItem addToCart(Long skuId, Integer num) throws ExecutionException, InterruptedException;
}
