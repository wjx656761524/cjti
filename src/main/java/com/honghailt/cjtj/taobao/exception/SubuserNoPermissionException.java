package com.honghailt.cjtj.taobao.exception;

/**
 * 子帐号权限异常
 */
public class SubuserNoPermissionException extends ISPException{

    public SubuserNoPermissionException(String errCode, String errMsg, String subErrCode, String subErrMsg) {
        super(errCode, errMsg, subErrCode, subErrMsg);
    }

}
