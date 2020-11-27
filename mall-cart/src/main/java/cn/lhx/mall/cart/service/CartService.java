package cn.lhx.mall.cart.service;

import cn.lhx.mall.cart.vo.Cart;
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

    /**
     * 获取整个购物车
     * @return
     */
    Cart getCart() throws ExecutionException, InterruptedException;

    /**
     * 清空购物车内容
     * @param cartKey
     */
    void clearCart(String cartKey);

    /**
     * 勾选购物项
     * @param skuId
     * @param check
     */
    void checkItem(Long skuId, Integer check);

    /**
     * 修改数量
     * @param skuId
     * @param num
     */
    void changeItemCount(Long skuId, Integer num);

    /**
     * 删除购物车项
     * @param skuId
     */
    void deleteItem(Long skuId);
}
