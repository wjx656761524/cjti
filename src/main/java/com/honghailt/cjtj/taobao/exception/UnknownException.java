package com.honghailt.cjtj.taobao.exception;

import com.taobao.api.ApiException;

/**
 * 当前未知的api异常
 */
public class UnknownException extends ApiException {

    public UnknownException(String errCode, String errMsg, String subErrCode, String subErrMsg) {
        super(errCode, errMsg, subErrCode, subErrMsg);
    }
}
