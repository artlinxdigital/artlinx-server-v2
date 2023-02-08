package com.nft.system.mapper;

import com.nft.system.domain.SysOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 藏品交易Mapper接口
 *
 * @author nft
 * @date 2021-05-18
 */
public interface SysOrderMapper {

    /**
     * 查询藏品交易
     *
     * @param sysOrderId 藏品交易ID
     * @return 藏品交易
     */
    public SysOrder selectSysOrderById(Long sysOrderId);

    /**
     * 查询藏品交易列表
     *
     * @param sysOrder 藏品交易
     * @return 藏品交易集合
     */
    public List<SysOrder> selectSysOrderList(SysOrder sysOrder);

    /**
     * 新增藏品交易
     *
     * @param sysOrder 藏品交易
     * @return 结果
     */
    public int insertSysOrder(SysOrder sysOrder);

    /**
     * 修改藏品交易
     *
     * @param sysOrder 藏品交易
     * @return 结果
     */
    public int updateSysOrder(SysOrder sysOrder);

    /**
     * 删除藏品交易
     *
     * @param sysOrderId 藏品交易ID
     * @return 结果
     */
    public int deleteSysOrderById(Long sysOrderId);

    /**
     * 批量删除藏品交易
     *
     * @param sysOrderIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysOrderByIds(Long[] sysOrderIds);

    /**
     * 批量查询藏品交易
     *
     * @param certificationIdSet 需要查询的数据ID
     * @return 结果
     */
    List<SysOrder> selectByCertificationIdSet(@Param("certificationIdSet") Set<Long> certificationIdSet);

    /**
     * 查询藏品交易记录列表
     *
     * @param contractAddress 合约地址
     * @param tokenId         Token ID
     * @return 藏品交易集合
     */
    public List<SysOrder> selectByContractAddressAndTokenId(@Param("contractAddress") String contractAddress, @Param("tokenId") Long tokenId);

    /**
     * 批量查询藏品交易
     *
     * @param address 需要查询的地址
     * @return 结果
     */
    List<SysOrder> selectByAddress(String address);

}
