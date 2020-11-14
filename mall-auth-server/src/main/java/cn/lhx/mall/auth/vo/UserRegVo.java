package cn.lhx.mall.auth.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * @author lee549
 * @date 2020/11/14 21:31
 */
@Data
public class UserRegVo {
    @NotEmpty(message = "用户名不能为空")
    @Length(min = 6,max = 18,message = "长度必须为6-18位")
    private String userName;
    @NotEmpty(message = "密码不能为空")
    @Length(min = 6,max = 18,message = "密码必须为6-18位")
    private String password;
    @NotEmpty(message = "手机号不能为空")
    @Pattern(regexp = "^[1]([3-9])[0-9]{9}$",message = "手机号格式不正确")
    private String phone;
    @NotEmpty(message = "验证码不能为空")
    private String code;
}
