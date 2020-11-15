package cn.lhx.mall.member.exception;

/**
 * @author lee549
 * @date 2020/11/15 12:44
 */
public class UserNameExitsException extends RuntimeException {
    public UserNameExitsException(){
        super("用户名已存在");
    }
}
