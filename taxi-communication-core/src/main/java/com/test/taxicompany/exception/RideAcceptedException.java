package com.test.taxicompany.exception;

public class RideAcceptedException extends RuntimeException {

    BizErrorCodeEnum errorCode;

    public RideAcceptedException(BizErrorCodeEnum errorCode) {
        super();
        this.errorCode = errorCode;
    }

    public BizErrorCodeEnum getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorCode.getMessage();
    }
}
