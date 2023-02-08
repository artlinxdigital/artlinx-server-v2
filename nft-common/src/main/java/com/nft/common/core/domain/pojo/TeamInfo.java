package com.nft.common.core.domain.pojo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 团队信息
 *
 * @author nft
 * @date 2021-05-19
 */
@Data
public class TeamInfo {

    /** 真实名称 */
    private String realName;

    /** 手机号码或邮箱 */
    private String account;

    /** 昵称 */
    private String nickName;

    /** 头像 */
    private String avatarUrl;

    /** 状态描述 */
    private String statusDesc;

    /** 交易总额 */
    private BigDecimal tradeAmount;

    /** 注册时间 */
    private String createTime;

    /** 是否实名 */
    private Integer isVerify;

}
