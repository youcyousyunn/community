package com.ycs.community.spring.exception;

import com.ycs.community.basebo.constants.HiMsgCdConstants;

public class CustomizeRollbackException extends CustomizeException {
    public CustomizeRollbackException(String message) {
        super(message);
        this.code = HiMsgCdConstants.FAIL;
    }

    public CustomizeRollbackException(String code, String message) {
        this(message);
        this.code = code;
    }

    public CustomizeRollbackException(Throwable throwableException) {
        super(throwableException);
        this.code = HiMsgCdConstants.FAIL;
        this.throwableException =throwableException;
    }

    public CustomizeRollbackException(String message, Throwable throwableException) {
        this(message);
        this.throwableException = throwableException;
    }

    public CustomizeRollbackException(String code, String message, Throwable throwableException) {
        this(message, throwableException);
        this.code = code;
    }
}
