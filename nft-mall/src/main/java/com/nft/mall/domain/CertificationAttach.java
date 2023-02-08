package com.nft.mall.domain;

import java.math.BigDecimal;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.nft.common.annotation.Excel;
import com.nft.common.core.domain.BaseEntity;

/**
 * 用户信息补充对象 t_certification_attach
 *
 * @author nft
 * @date 2021-05-13
 */
@Data
public class CertificationAttach extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long certificationAttachId;

    /** 用户人ID */
    @Excel(name = "用户人ID")
    private Long certificationId;

    /** 昵称 */
    @Excel(name = "昵称")
    private String nickName;

    /** 头像 */
    @Excel(name = "头像")
    private String avatarUrl;

    /** 余额 */
    @Excel(name = "余额")
    private BigDecimal balance;

    /** 积分 */
    @Excel(name = "积分")
    private BigDecimal integral;

    /** 个人简介 */
    @Excel(name = "个人简介")
    private String introduction;

    /** 状态：0.Normal 1.非正常 */
    @Excel(name = "状态：0.Normal 1.非正常")
    private Integer status;

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("certificationAttachId", getCertificationAttachId())
                .append("certificationId", getCertificationId())
                .append("nickName", getNickName())
                .append("avatarUrl", getAvatarUrl())
                .append("balance", getBalance())
                .append("integral", getIntegral())
                .append("introduction", getIntroduction())
                .append("status", getStatus())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}