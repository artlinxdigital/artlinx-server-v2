package com.nft.mall.mapper;

import java.util.List;
import com.nft.mall.domain.SaveKey;
import org.apache.ibatis.annotations.Param;

/**
 * 私钥托管Mapper接口
 *
 * @author nft
 * @date 2021-05-13
 */
public interface SaveKeyMapper
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
     * 删除私钥托管
     *
     * @param saveKeyId 私钥托管ID
     * @return 结果
     */
    public int deleteSaveKeyById(Long saveKeyId);

    /**
     * 批量删除私钥托管
     *
     * @param saveKeyIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteSaveKeyByIds(Long[] saveKeyIds);

    /**
     * 根据账号id查询托管信息
     * @param certificationId
     * @return 结果
     */
    SaveKey selectByCertificationId(@Param("certificationId") Long certificationId);

    /**
     * 更新个人托管密码
     * @param record
     * @return 结果
     */
    int updateByCertificationId(SaveKey record);

    /**
     * 获得该账户下托管的私钥
     * @param certificationId 用户id
     * @return 结果
     */
    List<SaveKey> listSaveKeyById(@Param("certificationId") Long certificationId);
}
