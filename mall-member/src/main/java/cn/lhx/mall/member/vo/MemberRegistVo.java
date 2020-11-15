package cn.lhx.mall.member.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * @author lee549
 * @date 2020/11/15 11:26
 */
@Data
public class MemberRegistVo {

    private String userName;
    private String password;
    private String phone;
}
