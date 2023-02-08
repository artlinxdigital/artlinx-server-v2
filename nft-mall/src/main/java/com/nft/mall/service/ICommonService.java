package com.nft.mall.service;

import com.nft.common.core.domain.AjaxResult;
import com.nft.mall.domain.BankConfig;
import com.nft.mall.domain.Banner;
import com.nft.mall.domain.vo.BannerVO;

import java.util.List;

/**
 * 公共信息Service接口
 *
 * @author nft
 * @date 2021-06-12
 */
public interface ICommonService {

    /**
     * 查询银行配置信息列表
     *
     * @param bankConfig 银行配置信息
     * @return 银行配置信息集合
     */
    AjaxResult getBankConfigList(BankConfig bankConfig);

    /**
     * 查询轮播图信息列表
     *
     * @param banner 轮播图信息
     * @return 轮播图信息集合
     */
    List<BannerVO> listBanner(Banner banner);

    /**
     * 查询藏品相关统计信息
     *
     * @return 藏品相关统计信息
     */
    AjaxResult statProductInfo();

}
