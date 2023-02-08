package com.nft.mall.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.nft.common.annotation.Excel;
import com.nft.common.core.domain.BaseEntity;

/**
 * 私钥托管对象 t_save_key
 *
 * @author nft
 * @date 2021-05-13
 */
@Data
public class SaveKey extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long saveKeyId;

    /** 认证人id */
    @Excel(name = "认证人id")
    private Long certificationId;

    /** 加密后的私钥 */
    @Excel(name = "加密后的私钥")
    private String privateKey;

    /** 加密后的密码 */
    @Excel(name = "加密后的密码")
    private String privatePass;

    /** 创建人 */
    @Excel(name = "创建人")
    private Long createId;

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("saveKeyId", getSaveKeyId())
                .append("certificationId", getCertificationId())
                .append("privateKey", getPrivateKey())
                .append("privatePass", getPrivatePass())
                .append("createId", getCreateId())
                .append("createTime", getCreateTime())
                .toString();
    }
}
