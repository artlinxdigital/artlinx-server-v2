package com.nft.mall.domain.vo;

import com.nft.common.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 藏品前端展示对象
 *
 * @author nft
 * @date 2021-05-17
 */
@Data
public class ProductBase {

    /** 主键ID */
    private Long productId;

    /** Token ID */
    private String tokenId;

    /** 合约地址 */
    private String contractAddress;

    /** 收藏夹ID */
    private Long collectDirId;

    /** 收藏夹名称 */
    private String collectDirName;

    /** 分类ID */
    private Long productCategoryId;

    /** 分类名称 */
    private String productCategoryName;

    /** 藏品单价 */
    private BigDecimal price;

    /** 藏品名称 */
    private String productName;

    /** 藏品作者 */
    private String productAuthor;

    /** 藏品创建人 */
    private String productCreator;

    /** 藏品创建人昵称 */
    private String productCreatorNickName;

    /** 藏品作者简介 */
    private String productAuthorDesc;

    /** 藏品创建人简介 */
    private String productCreatorIntro;

    /** 藏品创建人头像 */
    private String productCreatorAvatar;

    /** 藏品封面图 */
    private String coverImage;

    /** 藏品图片 */
    private String productImage;

    /** 藏品介绍 */
    private String productDesc;

    /** 服务费率 */
    private BigDecimal serviceRate;

    /** 版税 */
    private BigDecimal copyrightRate;

    /** 藏品点赞数量 */
    private Long favoriteCount = 0L;

    /** 藏品收藏数量 */
    private Long collectionCount = 0L;

    /** 是否点赞 0-未点赞 1-已点赞 */
    private Integer isFavorite = 0;

    /** 是否收藏 0-未收藏 1-已收藏 */
    private Integer isCollection = 0;

    /** 创建时间 */
    private String tokenTime;

    /** 余额 */
    private BigDecimal balance;

    /** 藏品类型：1.普通文件 2.音频 3.视频 */
    private Integer productType;

    /** 来源类型：1.创建 2.Buy */
    private Integer productFrom;

    /** 来源类型：1.创建 2.Buy */
    private String productFromDesc;

    /** 藏品状态：0.Normal 1.In Transaction  2.Successful Transaction  3.Has Been Removed  */
    private Integer status;

    /** 创建人id */
    private Long createId;

    /** 邀请人地址 */
    private String invitorAddress;

    /** 父藏品ID */
    private Long parentProductId;

    /** 交易状态 0.未被交易 1.被我交易 2.被他人交易 */
    private Integer tradeStatus = 0;

    /** ETH,ERC20,ERC721,ERC1155 */
    private String coinType;

    /** 卖家fixprice签名 */
    private String signatureLeft;

    /** 卖家订单信息(json) */
    private String orderLeft;

    /** 备注 */
    private String remark;

    /** 操作类型：1.铸造 2.上架 3.下架 4.交易 5.导入 */
    private Integer opeType;

}
