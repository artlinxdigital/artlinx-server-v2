package com.nft.common.core.domain.pojo.collect;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Set;

/**
 * 藏品搜索信息
 *
 * @author nft
 * @date 2021-05-12
 */
@Data
public class CollectionSearchParam {

    /** 当前页码 */
    @ApiModelProperty(value = "当前页码", required = true)
    public Integer pageNum;

    /** 每页条数 */
    @ApiModelProperty(value = "每页条数", required = true)
    public Integer pageSize;

    /** 用户ID */
    @ApiModelProperty(value = "用户ID", required = true)
    public Long certificationId;

    /** 藏品ID */
    @ApiModelProperty(value = "藏品ID", required = true)
    public Long productId;

    /** 分类ID */
    @ApiModelProperty(value = "分类ID", required = true)
    public Long productCategoryId;

    /** 操作类型 */
    @ApiModelProperty(value = "操作类型", required = true)
    public Integer opeType;

    /** 排序字段 */
    @ApiModelProperty(value = "排序字段", required = true)
    public String orderBy;

    /** 关键字 */
    @ApiModelProperty(value = "关键字", required = true)
    public String keyword;

    /** 状态 */
    @ApiModelProperty(value = "状态", required = true)
    public Integer status;

    /** 用户ID */
    @ApiModelProperty(value = "用户ID", required = true)
    public Long loginId;

    /**
     * 状态列表
     */
    public Set<Integer> statusSet;
    public Set<Long> certIdSet;

}
