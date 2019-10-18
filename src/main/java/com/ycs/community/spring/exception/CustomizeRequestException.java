package com.ycs.community.spring.exception;

public class CustomizeRequestException extends CustomizeRollbackException {
    public CustomizeRequestException(String code, String message) {
        super(message);
        this.code = code;
    }

    public CustomizeRequestException(String code, String message, Throwable throwableException) {
        super(message, throwableException);
        this.code = code;
    }
}
