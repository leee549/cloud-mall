package cn.lhx.common.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lee549
 * @date 2020/11/16 19:48
 */
@Data
public class MemberRespVo implements Serializable {
    private Long id;
    /**
     * $column.comments
     */
    private Long levelId;
    /**
     * $column.comments
     */
    private String username;
    /**
     * $column.comments
     */
    private String password;
    /**
     * $column.comments
     */
    private String nickname;
    /**
     * $column.comments
     */
    private String mobile;
    /**
     * $column.comments
     */
    private String email;
    /**
     * $column.comments
     */
    private String header;
    /**
     * $column.comments
     */
    private Integer gender;
    /**
     * $column.comments
     */
    private Date birth;
    /**
     * $column.comments
     */
    private String city;
    /**
     * $column.comments
     */
    private String job;
    /**
     * $column.comments
     */
    private String sign;
    /**
     * $column.comments
     */
    private Integer sourceType;
    /**
     * $column.comments
     */
    private Integer integration;
    /**
     * $column.comments
     */
    private Integer growth;
    /**
     * $column.comments
     */
    private Integer status;
    /**
     * $column.comments
     */
    private Date createTime;

    private String socialUid;

    private String accessToken;

    private Long expiresIn;
}
