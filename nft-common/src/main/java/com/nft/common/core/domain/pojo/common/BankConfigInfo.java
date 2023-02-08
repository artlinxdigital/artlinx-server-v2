package com.nft.common.core.domain.pojo.common;

import lombok.Data;

/**
 * 银行配置信息
 *
 * @author nft
 * @date 2021-05-19
 */
@Data
public class BankConfigInfo {

    /** 主键ID */
    private Long bankConfigId;

    /** 银行姓名 */
    private String bankName;

    /** 银行图标 */
    private String bankIcon;

    /** 银行键值 */
    private String bankKey;

    /** 银行类型 */
    private String bankType;

    /** 银行样式 */
    private String bankStyle;

}
