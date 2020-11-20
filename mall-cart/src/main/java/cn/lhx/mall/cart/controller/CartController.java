package cn.lhx.mall.cart.controller;

import cn.lhx.mall.cart.interceptor.CartInterceptor;
import cn.lhx.mall.cart.vo.UserInfoTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

/**
 * @author lee549
 * @date 2020/11/20 21:15
 */
@Controller
public class CartController {

    /**
     * 浏览器有一个user-key标识用户身份 一个月过期
     * 第一次使用购物车都会给一个临时身份
     * 浏览器保存，每次访问都会带上这个cookie
     *
     * 登录 有session
     * 没登录 按照user-key来做
     * 第一次登录没用户身份，给一个临时用户
     * @return
     */
    @GetMapping("/cart.html")
    public String cartListPage(){
        //同一个线程数据共享
        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();
        System.out.println(userInfoTo);
        return "cartList";

    }
}
