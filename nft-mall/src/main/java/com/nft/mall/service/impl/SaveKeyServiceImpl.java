package com.nft.mall.service.impl;

import java.util.List;
import com.nft.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nft.mall.mapper.SaveKeyMapper;
import com.nft.mall.domain.SaveKey;
import com.nft.mall.service.ISaveKeyService;

/**
 * 私钥托管Service业务层处理
 *
 * @author nft
 * @date 2021-05-13
 */
@Service
public class SaveKeyServiceImpl implements ISaveKeyService
{
    @Autowired
    private SaveKeyMapper saveKeyMapper;

    /**
     * 查询私钥托管
     *
     * @param saveKeyId 私钥托管ID
     * @return 私钥托管
     */
    @Override
    public SaveKey selectSaveKeyById(Long saveKeyId)
    {
        return saveKeyMapper.selectSaveKeyById(saveKeyId);
    }

    /**
     * 查询私钥托管列表
     *
     * @param saveKey 私钥托管
     * @return 私钥托管
     */
    @Override
    public List<SaveKey> selectSaveKeyList(SaveKey saveKey)
    {
        return saveKeyMapper.selectSaveKeyList(saveKey);
    }

    /**
     * 新增私钥托管
     *
     * @param saveKey 私钥托管
     * @return 结果
     */
    @Override
    public int insertSaveKey(SaveKey saveKey)
    {
        saveKey.setCreateTime(DateUtils.getNowDate());
        return saveKeyMapper.insertSaveKey(saveKey);
    }

    /**
     * 修改私钥托管
     *
     * @param saveKey 私钥托管
     * @return 结果
     */
    @Override
    public int updateSaveKey(SaveKey saveKey)
    {
        return saveKeyMapper.updateSaveKey(saveKey);
    }

    /**
     * 批量删除私钥托管
     *
     * @param saveKeyIds 需要删除的私钥托管ID
     * @return 结果
     */
    @Override
    public int deleteSaveKeyByIds(Long[] saveKeyIds)
    {
        return saveKeyMapper.deleteSaveKeyByIds(saveKeyIds);
    }

    /**
     * 删除私钥托管信息
     *
     * @param saveKeyId 私钥托管ID
     * @return 结果
     */
    @Override
    public int deleteSaveKeyById(Long saveKeyId)
    {
        return saveKeyMapper.deleteSaveKeyById(saveKeyId);
    }
}
