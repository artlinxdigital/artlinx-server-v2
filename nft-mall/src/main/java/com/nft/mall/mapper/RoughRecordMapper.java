package com.nft.mall.mapper;

import java.util.List;
import com.nft.mall.domain.RoughRecord;

/**
 * 原石记录Mapper接口
 *
 * @author nft
 * @date 2021-07-01
 */
public interface RoughRecordMapper
{
    /**
     * 查询原石记录
     *
     * @param roughRecordId 原石记录ID
     * @return 原石记录
     */
    public RoughRecord selectRoughRecordById(Long roughRecordId);

    /**
     * 查询原石记录列表
     *
     * @param roughRecord 原石记录
     * @return 原石记录集合
     */
    public List<RoughRecord> selectRoughRecordList(RoughRecord roughRecord);

    /**
     * 新增原石记录
     *
     * @param roughRecord 原石记录
     * @return 结果
     */
    public int insertRoughRecord(RoughRecord roughRecord);

    /**
     * 修改原石记录
     *
     * @param roughRecord 原石记录
     * @return 结果
     */
    public int updateRoughRecord(RoughRecord roughRecord);

    /**
     * 删除原石记录
     *
     * @param roughRecordId 原石记录ID
     * @return 结果
     */
    public int deleteRoughRecordById(Long roughRecordId);

    /**
     * 批量删除原石记录
     *
     * @param roughRecordIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteRoughRecordByIds(Long[] roughRecordIds);
}