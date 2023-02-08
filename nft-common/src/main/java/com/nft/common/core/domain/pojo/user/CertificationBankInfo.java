package com.nft.common.core.domain.pojo.user;

import lombok.Data;

/**
 * 银行展示信息
 *
 * @author nft
 * @date 2021-11-02
 */
@Data
public class CertificationBankInfo {

    /**
     * 主键ID
     */
    private Long certificationBankId;

    /**
     * 用户id
     */
    private Long certificationId;

    /**
     * 姓名/机构名称
     */
    private String realName;

    /**
     * 身份证/营业执照登记号
     */
    private String idNumber;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 银行用户名
     */
    private String accountName;

    /**
     * 国家/地区
     */
    private String country;

    /**
     * 银行名
     */
    private String bankName;

    /**
     * 银行账号
     */
    private String accountNumber;

    /**
     * 币种
     */
    private String accountCurrency;

    /**
     * 账号类型
     */
    private Integer accountType;

    /**
     * 银行代码
     */
    private String bankSwiftCode;

    /**
     * 本地银行编码
     */
    private String localBankCode;

    /**
     * 联系地址
     */
    private String contactAddress;

    /**
     * 城市
     */
    private String city;

    /**
     * 街道
     */
    private String street;

}
