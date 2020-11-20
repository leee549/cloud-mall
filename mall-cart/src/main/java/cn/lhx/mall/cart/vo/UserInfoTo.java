package cn.lhx.mall.cart.vo;

import lombok.Data;
import lombok.ToString;

/**
 * @author lee549
 * @date 2020/11/20 22:22
 */
@Data
@ToString
public class UserInfoTo {
    private Long userId;

    private String userKey;

    private boolean tempUser = false;
}
