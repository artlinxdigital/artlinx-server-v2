package com.nft.system.domain;

import java.math.BigDecimal;
import java.util.Set;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.nft.common.annotation.Excel;
import com.nft.common.core.domain.BaseEntity;

/**
 * 出账记录对象 t_certification_withdraw
 *
 * @author nft
 * @date 2021-07-26
 */
@Data
public class SysCertWithdraw extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long certificationWithdrawId;

    /**
     * 订单号
     */
    @Excel(name = "订单号")
    private String orderNumber;

    /**
     * 出账用户id
     */
    private Long certificationId;

    /**
     * 出账会员
     */
    @Excel(name = "出账会员")
    private String realName;

    /**
     * 出账地址
     */
    @Excel(name = "出账地址", width = 50)
    private String walletAddress;

    /**
     * 出账账号
     */
    @Excel(name = "出账账号")
    private String withdrawAccount;

    /**
     * 出账金额
     */
    @Excel(name = "出账金额")
    private BigDecimal withdrawAmount;

    /**
     * 出账方式：1.微信 2.支付宝 3.银行卡
     */
    @Excel(name = "出账方式", readConverterExp = "1=微信,2=支付宝,3=银行卡,4=购买,5=收藏夹,6=铸造,7=上架,8=手续费")
    private Integer withdrawType;

    /**
     * 出账状态：0.审核中 1.已出账 2.出账失败 3.已拒绝
     */
    @Excel(name = "出账状态", readConverterExp = "0=审核中,1=已出账,2=出账失败,3=已拒绝")
    private Integer withdrawStatus;

    /**
     * 提现类型列表
     */
    private Set<Integer> billTypeList;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("certificationWithdrawId", getCertificationWithdrawId())
                .append("orderNumber", getOrderNumber())
                .append("certificationId", getCertificationId())
                .append("realName", getRealName())
                .append("walletAddress", getWalletAddress())
                .append("withdrawAccount", getWithdrawAccount())
                .append("withdrawAmount", getWithdrawAmount())
                .append("withdrawType", getWithdrawType())
                .append("withdrawStatus", getWithdrawStatus())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}