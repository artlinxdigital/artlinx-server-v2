package com.nft.common.core.domain.pojo.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 个人/机构实名认证信息
 *
 * @author nft
 * @date 2021-06-11
 */
@Data
public class UserRealNameParam {

    /** 用户ID */
    @ApiModelProperty(value = "用户ID", required = true)
    @NotNull(message = "用户ID不能为空")
    private Long certificationId;

    /** 真实名称 */
    @ApiModelProperty(value = "真实名称", required = true)
    @NotNull(message = "真实名称不能为空")
    @NotBlank
    private String realName;

    /** 身份证号 */
    @ApiModelProperty(value = "身份证号", required = true)
    @NotNull(message = "身份证号不能为空")
    @NotBlank
    private String number;

    /** 国家/地区 */
    @ApiModelProperty(value = "国家/地区", required = true)
    @NotNull(message = "国家/地区不能为空")
    @NotBlank
    private String country;

    /** 身份证/营业登记照 */
    @ApiModelProperty(value = "国家/地区", required = true)
    @NotNull(message = "身份证/营业登记照不能为空")
    @NotBlank
    private String frontImageUrl;

    /** 背面照 */
    private String backImageUrl;

    /** 证件类型：1.身份证 */
    @ApiModelProperty(value = "证件类型：1.身份证", required = true)
    private Integer certType;

    /** 用户类型：1.个人 2.企业 */
    @ApiModelProperty(value = "用户类型：1.个人 2.企业", required = true)
    private Integer type;

}
