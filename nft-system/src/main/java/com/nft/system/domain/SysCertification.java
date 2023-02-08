package com.nft.system.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.nft.common.annotation.Excel;
import com.nft.common.core.domain.BaseEntity;

/**
 * 会员信息对象 t_certification
 *
 * @author nft
 * @date 2021-07-24
 */
@Data
public class SysCertification extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 会员名称
     */
//    @Excel(name = "会员名称")
    private String realName;

    /**
     * 身份证号
     */
//    @Excel(name = "身份证号")
    private String number;

    /**
     * 会员账号
     */
    @Excel(name = "会员账号", width = 30)
    private String mobile;

    /** 国家/地区 */
//    @Excel(name = "国家/地区")
    private String country;

    /** 身份证/营业登记照 */
//    @Excel(name = "身份证/营业登记照")
    private String frontImageUrl;

    /** 背面照 */
//    @Excel(name = "背面照")
    private String backImageUrl;

    /** 证件类型：1.身份证 */
//    @Excel(name = "证件类型")
    private Integer certType;

    /** 审核内容 */
//    @Excel(name = "审核内容")
    private String auditContent;

    /**
     * 类型：1.个人 2.企业
     */
    private Integer type;

    /**
     * 会员状态：0.未实名 1.已实名 2.待审核 3.已拒绝
     */
    @Excel(name = "会员状态", readConverterExp = "0=未实名,1=已实名,2=待审核,3=已拒绝")
    private Integer status;

    /**
     * 人脸识别二维码链接
     */
    private String url;

    /**
     * 第三方 认证ID
     */
    private String bizToken;

    /**
     * 会员地址
     */
    @Excel(name = "会员地址", width = 50)
    private String walletAddress;

    /**
     * 公钥
     */
    private String publicKey;

    /**
     * 签名图片url
     */
    private String signatureUrl;

    /**
     * 实名认证申请时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @Excel(name = "实名认证申请时间", dateFormat = "yyyy-MM-dd  HH:mm:ss")
    private Date realApplyTime;

    /**
     * 实名认证申审核时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @Excel(name = "实名认证审核时间", dateFormat = "yyyy-MM-dd  HH:mm:ss")
    private Date realAuditTime;

    /**
     * 认证时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @Excel(name = "认证时间", dateFormat = "yyyy-MM-dd  HH:mm:ss")
    private Date rzTime;

    /**
     * 邀请码
     */
//    @Excel(name = "邀请码")
    private String myCode;

    /**
     * 推荐码
     */
//    @Excel(name = "推荐码")
    private String otherCode;

    /**
     * 铸造藏品权限 0.允许 1.禁止
     */
    @Excel(name = "铸造藏品权限", readConverterExp = "0=允许,1=禁止")
    private Integer releaseStatus;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("realName", getRealName())
                .append("number", getNumber())
                .append("mobile", getMobile())
                .append("country", getCountry())
                .append("frontImageUrl", getFrontImageUrl())
                .append("backImageUrl", getBackImageUrl())
                .append("certType", getCertType())
                .append("auditContent", getAuditContent())
                .append("type", getType())
                .append("status", getStatus())
                .append("url", getUrl())
                .append("bizToken", getBizToken())
                .append("walletAddress", getWalletAddress())
                .append("publicKey", getPublicKey())
                .append("signatureUrl", getSignatureUrl())
                .append("ralApplyTime", getRealApplyTime())
                .append("ralAuditTime", getRealAuditTime())
                .append("rzTime", getRzTime())
                .append("myCode", getMyCode())
                .append("otherCode", getOtherCode())
                .append("releaseStatus", getReleaseStatus())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}