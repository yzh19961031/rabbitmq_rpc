package com.yzh.rabbitmq.rpc.exception;

/**
 * @author yuanzhihao
 * @since 2020/12/16
 */
public class IllegalParamException extends RuntimeException {
    private static final long serialVersionUID = -1285894096962354206L;

    public IllegalParamException(String message) {
        super(message);
    }

    public IllegalParamException(FailureInfo failureInfo) {
        super(failureInfo.getErrMessage());
    }
}
