package com.honghailt.cjtj.taobao.exception;

/**
 * 应用调用次数超限，包含调用频率超限
 */
public class CallLimitedException extends ISPException implements CanRetryException {

    public CallLimitedException(String errCode, String errMsg, String subErrCode, String subErrMsg) {
        super(errCode, errMsg, subErrCode, subErrMsg);
    }

}
