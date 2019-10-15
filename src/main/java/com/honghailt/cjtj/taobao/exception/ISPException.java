package com.honghailt.cjtj.taobao.exception;

import com.taobao.api.ApiException;

/**
 * api平台错误
 */
public class ISPException extends ApiException implements CanRetryException {

    public ISPException(String errCode, String errMsg, String subErrCode, String subErrMsg) {
        super(errCode, errMsg, subErrCode, subErrMsg);
    }
}
