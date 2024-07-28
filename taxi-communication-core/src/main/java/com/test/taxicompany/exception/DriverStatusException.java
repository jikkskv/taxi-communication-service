package com.test.taxicompany.exception;

public class DriverStatusException extends RuntimeException {

    BizErrorCodeEnum errorCode;

    public DriverStatusException(BizErrorCodeEnum errorCode) {
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
