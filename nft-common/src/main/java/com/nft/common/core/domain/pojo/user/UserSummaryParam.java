package com.nft.common.core.domain.pojo.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 用户简介信息
 *
 * @author nft
 * @date 2021-06-11
 */
@Data
public class UserSummaryParam {

    /** 用户ID */
    @ApiModelProperty(value = "用户ID", required = true)
    @NotNull(message = "用户ID不能为空")
    private Long certificationId;

    /** 简介 */
    @ApiModelProperty(value = "简介", required = true)
    @NotNull(message = "简介不能为空")
    @NotBlank
    private String desc;

}
