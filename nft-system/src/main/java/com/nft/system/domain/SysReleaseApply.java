package com.nft.system.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.nft.common.annotation.Excel;
import com.nft.common.core.domain.BaseEntity;

/**
 * 铸造权限申请信息对象 t_release_apply
 *
 * @author nft
 * @date 2021-07-24
 */
@Data
public class SysReleaseApply extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long releaseApplyId;

    /**
     * 用户ID
     */
    private Long certificationId;

    /**
     * 会员名称
     */
    @Excel(name = "会员名称")
    private String realName;

    /**
     * 会员账号
     */
    @Excel(name = "会员账号")
    private String mobile;

    /**
     * 拒绝原因
     */
    private String refuseReason;

    /**
     * 审核状态：-1.待申请 0.审核中 1.成功 2.拒绝
     */
    @Excel(name = "审核状态", readConverterExp = "-1=待申请,0=审核中,1=成功,2=拒绝")
    private Integer applyStatus;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("releaseApplyId", getReleaseApplyId())
                .append("certificationId", getCertificationId())
                .append("realName", getRealName())
                .append("mobile", getMobile())
                .append("refuseReason", getRefuseReason())
                .append("applyStatus", getApplyStatus())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}