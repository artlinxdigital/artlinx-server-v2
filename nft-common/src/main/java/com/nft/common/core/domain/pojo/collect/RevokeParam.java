package com.nft.common.core.domain.pojo.collect;

import com.nft.common.annotation.Excel;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 撤回参数
 *
 * @author nft
 */
@Data
public class RevokeParam {

    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    private Long certificationId;

    /**
     * 藏品ID
     */
    @NotNull(message = "藏品ID不能为空")
    private Long productId;

    /**
     * 收藏夹ID
     */
    private Long collectDirId;

    /**
     * Token ID
     */
    private String tokenId;

    /**
     * 交易hash
     */
    private String tradeHash;

    /**
     * 分类ID
     */
    private Long productCategoryId;

    /** 备注 */
    @Excel(name = "备注")
    private String remark;

}
