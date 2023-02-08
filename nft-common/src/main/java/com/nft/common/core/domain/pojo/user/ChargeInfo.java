package com.nft.common.core.domain.pojo.user;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 充值信息
 *
 * @author nft
 * @date 2021-05-19
 */
@Data
public class ChargeInfo {

    /** 主键ID */
    private String id;

    /** 入账金额 */
    private BigDecimal money;

    /** 入账方式：1.微信 2.支付宝 */
    private Integer type;

    /** 入账描述 */
    private String typeDesc;

    /** 入账人地址 */
    private String address;

    /** 入账状态：0.充值中 1.充值成功 2.充值失败 */
    private Integer status;

    /** 状态描述 */
    private String statusDesc;

    /** 创建时间 */
    private Date createTime;

    /** 入账时间 */
    private String chargeTime;

    /** 入账来源 */
    private String chargeSource;

}
