package com.ycs.community.spring.exception;

public class CustomizeBusinessException  extends CustomizeRollbackException{
    public CustomizeBusinessException(String code, String message) {
        super(message);
        this.code = code;
    }

    public CustomizeBusinessException(String code, String message, Throwable throwableException) {
        super(message, throwableException);
        this.code = code;
    }
}
