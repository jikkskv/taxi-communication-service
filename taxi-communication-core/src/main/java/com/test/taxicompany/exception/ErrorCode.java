package com.test.taxicompany.exception;

public interface ErrorCode {
    String ERROR_CODE = "errorCode";
    String ERROR_MESSAGE = "errorMessage";

    int getCode();

    String getMessage();
}
