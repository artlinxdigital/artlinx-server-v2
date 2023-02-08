package com.nft.mall.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.nft.common.annotation.Excel;
import com.nft.common.core.domain.BaseEntity;

/**
 * 商城用户对象 t_certification
 *
 * @author nft
 * @date 2021-05-12
 */
@Data
public class Certification extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 真实名称/机构名称 */
    @Excel(name = "真实名称/机构名称")
    private String realName;

    /** 身份证号/营业注册登记号 */
    @Excel(name = "身份证号/营业注册登记号")
    private String number;

    /** 手机号码或邮箱 */
    @Excel(name = "手机号码或邮箱")
    private String mobile;

    /** 国家/地区 */
    @Excel(name = "国家/地区")
    private String country;

    /** 身份证/营业登记照 */
    @Excel(name = "身份证/营业登记照")
    private String frontImageUrl;

    /** 背面照 */
    @Excel(name = "背面照")
    private String backImageUrl;

    /** 证件类型：1.身份证 */
    @Excel(name = "证件类型：1.身份证")
    private Integer certType;

    /** 审核内容 */
    @Excel(name = "审核内容")
    private String auditContent;

    /** 类型：1.个人 2.企业 */
    @Excel(name = "类型：1.个人 2.企业")
    private Integer type;

    /** 状态：0.未实名 1.成功 2.实名成功,待上链 3.失败 4.待审核 5.审核拒绝 6.已打款,待确认 */
    @Excel(name = "状态：0.未实名 1.成功 2.实名成功,待上链 3.失败 4.待审核 5.审核拒绝 6.已打款,待确认")
    private Integer status;

    /** 人脸识别二维码链接 */
    @Excel(name = "人脸识别二维码链接")
    private String url;

    /** 第三方 认证ID */
    @Excel(name = "第三方 认证ID")
    private String bizToken;

    /** 地址 */
    @Excel(name = "地址")
    private String walletAddress;

    /** 公钥 */
    @Excel(name = "公钥")
    private String publicKey;

    /** 签名图片url */
    @Excel(name = "签名图片url")
    private String signatureUrl;

    /**
     * 实名认证申请时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "实名认证申请时间", dateFormat = "yyyy-MM-dd  HH:mm:ss")
    private Date realApplyTime;

    /**
     * 实名认证申审核时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "实名认证审核时间", dateFormat = "yyyy-MM-dd  HH:mm:ss")
    private Date realAuditTime;

    /** 认证时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "认证时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date rzTime;

    /** 邀请码 */
    @Excel(name = "邀请码")
    private String myCode;

    /** 推荐码 */
    @Excel(name = "推荐码")
    private String otherCode;

    /** 发布藏品状态 0.允许 1.禁止 */
    @Excel(name = "发布藏品状态 0.允许 1.禁止")
    private Integer releaseStatus;

    /**
     * 人脸识别实名认证用于判断当前是在微信浏览器下
     */
    private String type1;

    private String isWeiXin;

    private String msgCode;

    /**
     * 私钥
     */
    private String privateKey;

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
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
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}