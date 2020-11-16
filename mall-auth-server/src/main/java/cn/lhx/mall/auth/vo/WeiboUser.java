package cn.lhx.mall.auth.vo;

import lombok.Data;

/**
 * @author lee549
 * @date 2020/11/16 16:33
 */
@Data
public class WeiboUser {

    private String access_token;
    private String remind_in;
    private int expires_in;
    private String uid;
    private String isRealName;
}
