package com.ycs.community.spring.exception;

import com.ycs.community.basebo.constants.HiMsgCdConstants;

public class CustomizeException extends RuntimeException {
    protected String code;
    protected String message;
    protected Throwable throwableException;

    public CustomizeException (String message) {
        super(message);
        this.code = HiMsgCdConstants.FAIL;
    }

    public CustomizeException (String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public CustomizeException(Throwable throwableException) {
        super(throwableException);
        this.code = HiMsgCdConstants.FAIL;
        this.throwableException = throwableException;
    }

    public CustomizeException(String message, Throwable throwableException) {
        this(message);
        this.throwableException = throwableException;
    }

    public CustomizeException (String code, String message, Throwable throwableException) {
        this(message, throwableException);
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return this.code;
    }
}
