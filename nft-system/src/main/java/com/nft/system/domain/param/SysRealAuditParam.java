package com.nft.system.domain.param;

import lombok.Data;

/**
 * 实名认证审核参数
 *
 * @author nft
 * @date 2021-05-12
 */
@Data
public class SysRealAuditParam {

    /** 主键ID */
    private Long id;

    /** 状态 */
    private Integer status;

    /** 审核内容 */
    private String auditContent;

}
