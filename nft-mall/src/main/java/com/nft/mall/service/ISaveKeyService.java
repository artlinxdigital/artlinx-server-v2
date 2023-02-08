package com.nft.mall.service;

import java.util.List;
import com.nft.mall.domain.SaveKey;

/**
 * 私钥托管Service接口
 *
 * @author nft
 * @date 2021-05-13
 */
public interface ISaveKeyService
{
    /**
     * 查询私钥托管
     *
     * @param saveKeyId 私钥托管ID
     * @return 私钥托管
     */
    public SaveKey selectSaveKeyById(Long saveKeyId);

    /**
     * 查询私钥托管列表
     *
     * @param saveKey 私钥托管
     * @return 私钥托管集合
     */
    public List<SaveKey> selectSaveKeyList(SaveKey saveKey);

    /**
     * 新增私钥托管
     *
     * @param saveKey 私钥托管
     * @return 结果
     */
    public int insertSaveKey(SaveKey saveKey);

    /**
     * 修改私钥托管
     *
     * @param saveKey 私钥托管
     * @return 结果
     */
    public int updateSaveKey(SaveKey saveKey);

    /**
     * 批量删除私钥托管
     *
     * @param saveKeyIds 需要删除的私钥托管ID
     * @return 结果
     */
    public int deleteSaveKeyByIds(Long[] saveKeyIds);

    /**
     * 删除私钥托管信息
     *
     * @param saveKeyId 私钥托管ID
     * @return 结果
     */
    public int deleteSaveKeyById(Long saveKeyId);
}
