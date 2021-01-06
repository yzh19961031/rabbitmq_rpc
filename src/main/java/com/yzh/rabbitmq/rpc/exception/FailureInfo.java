package com.yzh.rabbitmq.rpc.exception;

/**
 * 错误信息
 *
 * @author yuanzhihao
 * @since 2020/12/16
 */
public enum FailureInfo {

    UNKNOWN_INFO(1001, "unknown error!"),
    ILLEGAL_PARAM(1002, "illegal param");

    private final int errCode;
    private final String errMessage;

    FailureInfo(int errCode, String errMessage) {
        this.errCode = errCode;
        this.errMessage = errMessage;
    }

    public int getErrCode() {
        return errCode;
    }

    public String getErrMessage() {
        return errMessage;
    }
}
