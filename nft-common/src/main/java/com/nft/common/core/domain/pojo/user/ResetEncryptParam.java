package com.nft.common.core.domain.pojo.user;

import com.nft.common.enums.CertificationType;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

/**
 * 重置后的加密信息
 *
 * @author nft
 * @date 2021-05-13
 */
@Data
public class ResetEncryptParam {

    /**
     * 用户ID
     */
    private Long loginId;

    /**
     * 被重置的个人或者企业
     */
    private Long operationId;

    /**
     * 类型 1-个人 2-企业
     */
    private Integer type;
    /**
     * 托管密码加密私钥
     */
    private String encryptPrivateKey;

    /**
     * 私钥加密托管密码
     */
    private String encryptPassword;

    public static boolean valid(List<ResetEncryptParam> infoList) {
        if (CollectionUtils.isEmpty(infoList)) {
            return false;
        }
        boolean isValid = true;
        for (ResetEncryptParam info : infoList) {
            Integer type = info.getType();
            if (Objects.isNull(type)) {
                isValid = false;
                break;
            }
            CertificationType byStatus = CertificationType.getByCode(type);
            if (Objects.isNull(byStatus)) {
                isValid = false;
                break;
            }
            isValid = Objects.nonNull(info.getType())
                    && Objects.nonNull(info.getLoginId())
                    && info.getLoginId() > 0
                    && Objects.nonNull(info.getOperationId())
                    && info.getOperationId() > 0
                    && StringUtils.isNotEmpty(info.getEncryptPrivateKey())
                    && StringUtils.isNotEmpty(info.getEncryptPassword());
            if (!isValid) {
                isValid = false;
                break;
            }
        }
        return isValid;
    }

}
