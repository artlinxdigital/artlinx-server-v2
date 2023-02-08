package com.nft.mall.domain;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 余额
 *
 * @author nft
 * @date 2021-10-25
 */
@Data
public class Balance implements Serializable {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 余额
     */
    private BigDecimal balance;

    /**
     * 用户ID
     */
    private Long certificationId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 交易账号
     */
    private String account;

    /**
     * 交易卡号
     */
    private String bankCard;

    /**
     * 国家/地区
     */
    private String country;

    /**
     * 账号类型
     */
    private Integer accountType;

    /**
     * 银行用户名
     */
    private String accountName;

    /**
     * 银行名
     */
    private String bankName;

    /**
     * 币种
     */
    private String accountCurrency;

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
