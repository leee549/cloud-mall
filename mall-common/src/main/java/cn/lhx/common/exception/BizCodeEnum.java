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
     * 系统未知错误
     */
    UNKNOW_EXCEPTION(10000,"系统未知错误"),
    /**
     * 数据校验异常
     */
    VALID_EXCEPTION(10001,"数据校验异常"),
    /**
     * 商品上架错误
     */
    PRODUCT_UP_EXCEPTION(11000,"商品上架错误");


    public int code;
    public String msg;



}
