package com.nft.common.core.domain.pojo.collect;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 藏品交易查询信息
 *
 * @author nft
 * @date 2021-05-12
 */
@Data
public class CollectionTradeSearchParam {

    /** 当前页码 */
    @ApiModelProperty(value = "当前页码", required = true)
    public Integer pageNum;

    /** 每页条数 */
    @ApiModelProperty(value = "每页条数", required = true)
    public Integer pageSize;

    /** 用户ID */
    @ApiModelProperty(value = "用户ID", required = true)
    public Long certificationId;

    /** 排序字段 */
    @ApiModelProperty(value = "排序字段", required = true)
    public String orderBy;

}
