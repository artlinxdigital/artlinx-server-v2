package com.nft.common.core.domain.pojo.user;

import lombok.Data;

import java.util.List;

/**
 * 私钥重置密码信息
 *
 * @author nft
 * @date 2021-05-13
 */
@Data
public class ResetPwdParam {

    private List<ResetEncryptParam> resetEncryptParamList;

}
