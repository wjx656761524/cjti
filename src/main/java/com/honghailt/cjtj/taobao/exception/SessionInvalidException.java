package com.honghailt.cjtj.taobao.exception;

/**
 * 无效session异常，一般是sessionkey错误或者超期等
 */
public class SessionInvalidException extends ISPException{

    public SessionInvalidException(String errCode, String errMsg, String subErrCode, String subErrMsg) {
        super(errCode, errMsg, subErrCode, subErrMsg);
    }

}
