package com.nft.common.constant;

/**
 * 通用常量信息
 * 
 * @author nft
 */
public class Constants
{
    /**
     * UTF-8 字符集
     */
    public static final String UTF8 = "UTF-8";

    /**
     * GBK 字符集
     */
    public static final String GBK = "GBK";

    /**
     * http请求
     */
    public static final String HTTP = "http://";

    /**
     * https请求
     */
    public static final String HTTPS = "https://";

    /**
     * 通用成功标识
     */
    public static final String SUCCESS = "0";

    /**
     * 通用失败标识
     */
    public static final String FAIL = "1";

    /**
     * 登录成功
     */
    public static final String LOGIN_SUCCESS = "Success";

    /**
     * 注销
     */
    public static final String LOGOUT = "Logout";

    /**
     * 登录失败
     */
    public static final String LOGIN_FAIL = "Error";

    /**
     * 验证码 redis key
     */
    public static final String CAPTCHA_CODE_KEY = "captcha_codes:";

    /**
     * 登录用户 redis key
     */
    public static final String LOGIN_TOKEN_KEY = "login_tokens:";
    
    /**
     * 防重提交 redis key
     */
    public static final String REPEAT_SUBMIT_KEY = "repeat_submit:";

    /**
     * 验证码有效期（分钟）
     */
    public static final Integer CAPTCHA_EXPIRATION = 2;

    /**
     * 令牌
     */
    public static final String TOKEN = "token";

    /**
     * 令牌前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * 令牌前缀
     */
    public static final String LOGIN_USER_KEY = "login_user_key";

    /**
     * 用户ID
     */
    public static final String JWT_USERID = "userid";

    /**
     * 用户名称
     */
    public static final String JWT_USERNAME = "sub";

    /**
     * 用户头像
     */
    public static final String JWT_AVATAR = "avatar";

    /**
     * 创建时间
     */
    public static final String JWT_CREATED = "created";

    /**
     * 用户权限
     */
    public static final String JWT_AUTHORITIES = "authorities";

    /**
     * 参数管理 cache key
     */
    public static final String SYS_CONFIG_KEY = "sys_config:";

    /**
     * 字典管理 cache key
     */
    public static final String SYS_DICT_KEY = "sys_dict:";

    /**
     * 资源映射路径 前缀
     */
    public static final String RESOURCE_PREFIX = "/profile";

    /**
     * 手机号正则表达式
     */
    public static final String MOBILE_REGEX = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";

    /**
     * 邮箱正则表达式
     */
    public static final String EMAIL_REGEX = "^[\\da-z]+([\\-\\.\\_]?[\\da-z]+)*@[\\da-z]+([\\-\\.]?[\\da-z]+)*(\\.[a-z]{2,})+$";

    /**
     * 文件上传的路径
     */
    public static final String FILE_PUBLIC_URL = "/public/";

    /**
     * 文件上传硬盘的路径
     */
    public static final String DISK_FILE_URL = "/nftdir/resources/uploads";
    public static final String WIN_FILE_URL = "D:/resources/uploads";

    public static final String DISK_EDITOR_FILE_URL = "/nftdir";
    public static final String WIN_EDITOR_FILE_URL = "D:/resources";

    /**
     * 开放资源文件路劲
     */
    public static final String DISK_PUBLIC_FILE_URL = "/nftdir/resources/public";
    public static final String WIN_PUBLIC_FILE_URL = "D:/resources/public";

    /**
     * 文件下载硬盘的路径
     */
    public static final String DISK_FILE_URL_DOWN = "/nftdir/resources/downloads";
    public static final String WIN_FILE_URL_DOWN = "D:/resources/downloads";

    /**
     * 人脸识别成功后的回调路径，前端路径（法人授权机构，pc端个人实名）
     * H5_LEADER_SUCCESS_URL：TxCertificationController中的startFaceForLeader，机构实名时选择法人人脸识别认证
     */
    public static final String H5_LEADER_SUCCESS_URL = "https://dapp.ctsign.cn/Auth";

    /**
     * H5注册机构，法人人脸识别成功后的回调路径（当前操作人是法人）
     * H5_LEADER_SUCCESS_URL：TxCertificationController中的startFaceForLeader，移动端机构实名时选择法人人脸识别认证
     */
    public static final String H5_PERSON_LEADER_URL_ORG = "https://orgh5.ctsign.cn/realname-success";
    /**
     * H5注册机构，法人授权人脸识别成功后的回调路径（当前操作人不是法人）
     * H5_LEADER_SUCCESS_URL：TxCertificationController中的startFaceForLeader，移动端机构实名时选择法人人脸识别认证
     */
    public static final String H5_LEADER_URL_ORG = "https://orgh5.ctsign.cn/sqSuccess";

    /**
     * H5个人注册并签署合同，人脸识别成功后的回调路径
     */
//    public static final String H5_LEADER_URL_PERSON = "https://h5.ctsign.cn/CertificationSuccessH5";
    public static final String H5_LEADER_URL_PERSON = "https://rbg123.com/auth-success";

    /**
     * PC_PERSON_SUCCESS_URL:TxCertificationController中的startPcFace，个人实名（机构端）
     */
    public static final String PC_PERSON_SUCCESS_URL = "https://rbg123.com/realname";

    /**
     * H5_PERSON_SUCCESS_URL:TxCertificationController中的startH5Face，个人实名（手机端）
     */
    public static final String H5_PERSON_SUCCESS_URL = "https://rbg123.com/realname";

}
