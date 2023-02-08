package com.nft.mall.domain.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CertificationVO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 真实名称
     */
    private String realName;

    /**
     * 身份证号或者统一信用代码
     */
    private String number;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 公钥
     */
    private String publicKey;

    /**
     * 地址
     */
    private String walletAddress;

    /**
     * 我的邀请码
     */
    private String myCode;

    /**
     * 推荐人的邀请码
     */
    private String otherCode;

    /**
     * 认证时间
     */
    private Date rzTime;

    /**
     * 私钥列表
     */
    private List<String> privateKeys;

    /**
     * 私钥加密后的密码
     */
    private String privatePass;

    /**
     * 邀请码加密的私钥
     */
    private String inviteCode;

    /**
     * 状态描述
     */
    private String statusDesc;

}
