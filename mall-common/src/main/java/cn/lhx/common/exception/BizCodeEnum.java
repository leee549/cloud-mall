package cn.lhx.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author lee549
 * @date 2020/9/21 21:37
 * 业务代码枚举
 */
@AllArgsConstructor
public enum BizCodeEnum {
    /**
     * 10 通用
     * 11 商品
     * 12 订单
     * 13 购物车
     * 14 物流
     * 15 用户
     */


    /**
     * 系统未知错误
     */
    UNKNOW_EXCEPTION(10000,"系统未知错误"),
    /**
     * 数据校验异常
     */
    VALID_EXCEPTION(10001,"数据校验异常"),
    /**
     * 验证码频率过高
     */
    SMS_CODE_EXCEPTION(10002,"验证码发送频繁，请稍后再试"),


    USER_EXIST_EXCEPTION(15001,"用户已存在"),


    PHONE_EXIST_EXCEPTION(15002,"手机号已存在"),

    ACCOUNT_PASSWORD_EXCEPTION(15003,"账号或密码错误"),

    /**
     * 商品上架错误
     */
    PRODUCT_UP_EXCEPTION(11000,"商品上架错误");


    public int code;
    public String msg;



}
