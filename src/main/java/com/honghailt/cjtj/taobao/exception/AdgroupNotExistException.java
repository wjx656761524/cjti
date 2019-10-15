package com.honghailt.cjtj.taobao.exception;

/**
 * 推广组不存在异常
 */
public class AdgroupNotExistException extends ISVException {

    public AdgroupNotExistException(String errCode, String errMsg, String subErrCode, String subErrMsg) {
        super(errCode, errMsg, subErrCode, subErrMsg);
    }
}
