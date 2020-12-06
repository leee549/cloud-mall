package cn.lhx.mall.order.vo;

import lombok.Data;

/**
 * @author lee549
 * @date 2020/12/6 9:45
 */
@Data
public class MemberReceiveAddressVo {

    private Long id;
    /**
     * $column.comments
     */
    private Long memberId;
    /**
     * $column.comments
     */
    private String name;
    /**
     * $column.comments
     */
    private String phone;
    /**
     * $column.comments
     */
    private String postCode;
    /**
     * $column.comments
     */
    private String province;
    /**
     * $column.comments
     */
    private String city;
    /**
     * $column.comments
     */
    private String region;
    /**
     * $column.comments
     */
    private String detailAddress;
    /**
     * $column.comments
     */
    private String areacode;
    /**
     * $column.comments
     */
    private Integer defaultStatus;
}
