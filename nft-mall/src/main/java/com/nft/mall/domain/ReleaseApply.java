package com.nft.mall.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.nft.common.annotation.Excel;
import com.nft.common.core.domain.BaseEntity;

/**
 * 藏品发布申请信息对象 t_release_apply
 *
 * @author nft
 * @date 2021-06-06
 */
@Data
public class ReleaseApply extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long releaseApplyId;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long certificationId;

    /** 拒绝原因 */
    @Excel(name = "拒绝原因")
    private String refuseReason;

    /** 申请状态：0.申请中 1.申请成功 2.申请失败 */
    @Excel(name = "申请状态：0.申请中 1.申请成功 2.申请失败")
    private Integer applyStatus;

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("releaseApplyId", getReleaseApplyId())
                .append("certificationId", getCertificationId())
                .append("refuseReason", getRefuseReason())
                .append("applyStatus", getApplyStatus())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}