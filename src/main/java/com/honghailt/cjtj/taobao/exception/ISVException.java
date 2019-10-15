package com.honghailt.cjtj.taobao.exception;

import com.taobao.api.ApiException;

/**
 * isv 业务异常，通常是参数错误，重复提交等
 */
public class ISVException extends ApiException {

    public ISVException(String errCode, String errMsg, String subErrCode, String subErrMsg) {
        super(errCode, errMsg, subErrCode, subErrMsg);
    }
}
