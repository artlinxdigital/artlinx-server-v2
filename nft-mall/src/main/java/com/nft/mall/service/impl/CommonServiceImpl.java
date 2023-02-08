package com.nft.mall.service.impl;

import java.util.List;
import java.util.Map;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nft.common.core.domain.AjaxResult;
import com.nft.common.core.domain.pojo.common.BankConfigInfo;
import com.nft.common.utils.CopyUtils;
import com.nft.mall.domain.Banner;
import com.nft.mall.domain.vo.BannerVO;
import com.nft.mall.mapper.BannerMapper;
import com.nft.mall.mapper.ProductMapper;
import com.nft.mall.service.ICommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nft.mall.mapper.BankConfigMapper;
import com.nft.mall.domain.BankConfig;

/**
 * 公共信息Service业务层处理
 *
 * @author nft
 * @date 2021-06-12
 */
@Service
public class CommonServiceImpl implements ICommonService {

    @Autowired
    private BankConfigMapper bankConfigMapper;

    @Autowired
    protected BannerMapper bannerMapper;

    @Autowired
    private ProductMapper productMapper;

    /**
     * 查询银行配置信息列表
     *
     * @param bankConfig 银行配置信息
     * @return 银行配置信息集合
     */
    @Override
    public AjaxResult getBankConfigList(BankConfig bankConfig) {
        List<BankConfig> bankConfigList = bankConfigMapper.selectBankConfigList(bankConfig);
        List<BankConfigInfo> configInfoList = CopyUtils.copyList(bankConfigList, BankConfigInfo.class);
        return AjaxResult.success("查询成功", configInfoList);
    }

    /**
     * 查询轮播图信息列表
     *
     * @param banner 轮播图信息
     * @return 轮播图信息集合
     */
    @Override
    public List<BannerVO> listBanner(Banner banner) {
        List<BannerVO> bannerVOList = Lists.newLinkedList();
        List<Banner> bannerList = bannerMapper.selectBannerList(banner);
        if (CollUtil.isNotEmpty(bannerList)) {
            bannerVOList = CopyUtils.copyList(bannerList, BannerVO.class);
        }
        return bannerVOList;
    }

    /**
     * 查询藏品相关统计信息
     *
     * @return 藏品相关统计信息
     */
    @Override
    public AjaxResult statProductInfo() {
        Map<String, Object> resultMap = Maps.newHashMap();
        // 总的艺术品
        String totalArtworks = productMapper.countAllProduct();
        resultMap.put("totalArtworks", totalArtworks);
        // 今日上架艺术品
        String newArtPiecesOfToday = productMapper.countToadyProduct();
        resultMap.put("newArtPiecesOfToday", newArtPiecesOfToday);
        // 市场标价总和
        String totalMarketValue = productMapper.sumAllProduct();
        resultMap.put("totalMarketValue", totalMarketValue);
        return AjaxResult.success(resultMap);
    }
}
