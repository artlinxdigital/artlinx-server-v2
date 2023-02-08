package com.nft.common.exception.user;

import com.nft.common.exception.BaseException;

/**
 * 用户信息异常类
 * 
 * @author nft
 */
public class UserException extends BaseException
{
    private static final long serialVersionUID = 1L;

    public UserException(String code, Object[] args)
    {
        super("user", code, args, null);
    }
}
