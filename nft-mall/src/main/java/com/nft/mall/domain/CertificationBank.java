package com.nft.mall.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.nft.common.annotation.Excel;
import com.nft.common.core.domain.BaseEntity;

/**
 * 用户银行配置信息对象 t_certification_bank
 *
 * @author nft
 * @date 2021-11-02
 */
@Data
public class CertificationBank extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long certificationBankId;

    /**
     * 用户id
     */
    @Excel(name = "用户id")
    private Long certificationId;

    /**
     * 姓名/机构名称
     */
    @Excel(name = "姓名/机构名称")
    private String realName;

    /**
     * 身份证/营业执照登记号
     */
    @Excel(name = "身份证/营业执照登记号")
    private String idNumber;

    /**
     * 邮箱
     */
    @Excel(name = "邮箱")
    private String email;

    /**
     * 银行用户名
     */
    @Excel(name = "银行用户名")
    private String accountName;

    /**
     * 国家/地区
     */
    @Excel(name = "国家/地区")
    private String country;

    /**
     * 银行名
     */
    @Excel(name = "银行名")
    private String bankName;

    /**
     * 银行账号
     */
    @Excel(name = "银行账号")
    private String accountNumber;

    /**
     * 币种
     */
    @Excel(name = "币种")
    private String accountCurrency;

    /**
     * 账号类型
     */
    @Excel(name = "账号类型")
    private Integer accountType;

    /**
     * 银行代码
     */
    @Excel(name = "银行代码")
    private String bankSwiftCode;

    /**
     * 本地银行编码
     */
    @Excel(name = "本地银行编码")
    private String localBankCode;

    /**
     * 联系地址
     */
    @Excel(name = "联系地址")
    private String contactAddress;

    /**
     * 城市
     */
    @Excel(name = "城市")
    private String city;

    /**
     * 街道
     */
    @Excel(name = "街道")
    private String street;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("certificationBankId", getCertificationBankId())
                .append("certificationId", getCertificationId())
                .append("realName", getRealName())
                .append("idNumber", getIdNumber())
                .append("email", getEmail())
                .append("accountName", getAccountName())
                .append("country", getCountry())
                .append("bankName", getBankName())
                .append("accountNumber", getAccountNumber())
                .append("accountCurrency", getAccountCurrency())
                .append("accountType", getAccountType())
                .append("bankSwiftCode", getBankSwiftCode())
                .append("localBankCode", getLocalBankCode())
                .append("contactAddress", getContactAddress())
                .append("city", getCity())
                .append("street", getStreet())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}