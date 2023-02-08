package com.nft.common.core.domain.pojo.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * PC用户实名认证回调信息
 *
 * @author nft
 * @date 2021-07-14
 */
@Data
public class UserPcAuthCallBackParam {

    /** 用户ID */
    @ApiModelProperty(value = "用户ID", required = true)
    @NotNull(message = "用户ID不能为空")
    private Long certificationId;

    /** 用户类型 */
    @ApiModelProperty(value = "用户类型", required = true)
    @NotNull(message = "用户类型不能为空")
    @NotBlank
    private String type;

    /** 真实姓名 */
    @ApiModelProperty(value = "真实姓名", required = true)
    @NotNull(message = "真实姓名不能为空")
    @NotBlank
    private String realName;

    /** 身份证号 */
    @ApiModelProperty(value = "身份证号", required = true)
    @NotNull(message = "身份证号不能为空")
    @NotBlank
    private String number;

    /** 用户来源 */
    @ApiModelProperty(value = "用户来源", required = true)
    @NotNull(message = "用户来源不能为空")
    private Integer isLand;

    /** 邀请激励 */
    @ApiModelProperty(value = "邀请激励", required = true)
    @NotNull(message = "邀请激励不能为空")
    private BigDecimal invitorAmount;

    /** 合约地址 */
    @ApiModelProperty(value = "合约地址", required = true)
    @NotNull(message = "合约地址不能为空")
    @NotBlank
    private String contractAddress;

}
