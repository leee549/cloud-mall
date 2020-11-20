package cn.lhx.mall.cart.service.impl;

import cn.lhx.mall.cart.service.CartService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author lee549
 * @date 2020/11/20 21:11
 */
@Service
public class CartServiceImpl implements CartService {
    @Resource(name = "stringRedisTemplate")
    private StringRedisTemplate stringRedisTemplate;
}
