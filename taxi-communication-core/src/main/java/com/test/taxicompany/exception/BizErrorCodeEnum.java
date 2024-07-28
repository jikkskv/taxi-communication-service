package com.test.taxicompany.exception;

import lombok.Getter;

@Getter
public enum BizErrorCodeEnum implements ErrorCode {
    RIDE_ALREADY_ACCEPTED(10001, "This ride has been already accepted by another user"),
    INVALID_DRIVER(10002, "Invalid driver Id"),
    INVALID_DRIVER_STATUS(10002, "Invalid driver status"),
    RIDE_NOT_COMPLETED(10002, "Driver has not completed the existing ride."),
    INVALID_RIDE_INFO(10003, "Invalid driver info"),
    EMAIL_ID_ALREADY_USED(10004, "This email Id has already been used."),
    SYSTEM_ERROR(500, "System Error");
    private int code;
    private String message;

    BizErrorCodeEnum(final int code) {
        this.code = code;
    }

    BizErrorCodeEnum(final int code, final String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String toString() {
        return "[" + this.getCode() + "]" + this.getMessage();
    }
}
