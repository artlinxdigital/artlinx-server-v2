package com.nft.system.service;

import java.util.List;

import com.nft.system.domain.SysCharge;

/**
 * 入账记录Service接口
 *
 * @author nft
 * @date 2021-07-25
 */
public interface ISysChargeService {

    /**
     * 查询入账记录
     *
     * @param id 入账记录ID
     * @return 入账记录
     */
    public SysCharge selectSysChargeById(String id);

    /**
     * 查询入账记录列表
     *
     * @param sysCharge 入账记录
     * @return 入账记录集合
     */
    public List<SysCharge> selectSysChargeList(SysCharge sysCharge);

    /**
     * 新增入账记录
     *
     * @param sysCharge 入账记录
     * @return 结果
     */
    public int insertSysCharge(SysCharge sysCharge);

    /**
     * 修改入账记录
     *
     * @param sysCharge 入账记录
     * @return 结果
     */
    public int updateSysCharge(SysCharge sysCharge);

    /**
     * 批量删除入账记录
     *
     * @param ids 需要删除的入账记录ID
     * @return 结果
     */
    public int deleteSysChargeByIds(String[] ids);

    /**
     * 删除入账记录信息
     *
     * @param id 入账记录ID
     * @return 结果
     */
    public int deleteSysChargeById(String id);
}