package com.test.taxicompany.exception;

public class UserRegisterException extends RuntimeException {

    BizErrorCodeEnum errorCode;

    public UserRegisterException(BizErrorCodeEnum errorCode) {
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
