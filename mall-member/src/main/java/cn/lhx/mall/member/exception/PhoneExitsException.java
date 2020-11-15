package cn.lhx.mall.member.exception;

/**
 * @author lee549
 * @date 2020/11/15 12:43
 */
public class PhoneExitsException extends RuntimeException {
    public PhoneExitsException(){
        super("手机号已存在");
    }
}
