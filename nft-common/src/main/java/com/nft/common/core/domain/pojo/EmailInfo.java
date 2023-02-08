package com.nft.common.core.domain.pojo;

import lombok.Data;

/**
 * 邮件信息
 * @author nft
 */
@Data
public class EmailInfo {

    /** SMTP服务器地址 **/
    private String smtpServer;

    /** 端口 **/
    private String port;

    /** 登录SMTP服务器的用户名,发送人邮箱地址 **/
    private String fromUserName;

    /** 登录SMTP服务器的密码 **/
    private String fromUserPassword;

    /** 收件人 **/
    private String toUser;

    /** 邮件主题 **/
    private String subject;

    /** 邮件正文 **/
    private String content;

    public EmailInfo() {

    }

    public EmailInfo(String toUser, String subject, String content) {
        this.toUser = toUser;
        this.subject = subject;
        this.content = content;
        this.smtpServer = "tuesday.mxrouting.net";
        this.port = "465";
        this.fromUserName = "clientservice@artlinx.hk";
        this.fromUserPassword = "o&=LyD=:3~50V&sjyh#xU}*!4AUg-1?pnEpZ~TuBX2q]Q4Zs9y";
    }
}
