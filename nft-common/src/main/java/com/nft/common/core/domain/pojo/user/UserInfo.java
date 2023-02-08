package com.nft.common.core.domain.pojo.user;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户信息
 *
 * @author nft
 * @date 2021-05-19
 */
@Data
public class UserInfo {

    /** 主键ID */
    private Long id;

    /** 真实名称 */
    private String realName;

    /** 身份证号 */
    private String number;

    /** 手机号码或邮箱 */
    private String mobile;

    /** 国家/地区 */
    private String country;

    /** 身份证/营业登记照 */
    private String frontImageUrl;

    /** 背面照 */
    private String backImageUrl;

    /** 证件类型：1.身份证 */
    private Integer certType;

    /** 用户类型 1.个人 2.企业 */
    private Integer type;

    /** 状态：0.未实名 1.成功 2.实名成功,待上链 3.失败 4.待审核 5.审核拒绝 6.已打款,待确认 */
    private Integer status;

    /** 地址 */
    private String walletAddress;

    /** 认证时间 */
    private Date rzTime;

    /** 邀请码 */
    private String myCode;

    /** 昵称 */
    private String nickName;

    /** 头像 */
    private String avatarUrl;

    /** 余额 */
    private BigDecimal balance;

    /** 个人简介 */
    private String introduction;

    /** 团队数量 */
    private Integer teamCount;

    /** 申请状态 */
    private Integer applyStatus;

    /** 推荐人 */
    private String recommendName;

    /** 推荐人头像 */
    private String recommendAvatarUrl;

    /** 审核原因 */
    private String auditContent;

}
