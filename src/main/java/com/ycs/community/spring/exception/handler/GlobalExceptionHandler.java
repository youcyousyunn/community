package com.ycs.community.spring.exception.handler;

import com.ycs.community.spring.exception.BadRequestException;
import com.ycs.community.spring.exception.CustomizeBusinessException;
import com.ycs.community.spring.exception.CustomizeRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理自定义请求异常
     * @param CustomizeRequestException
     * @return
     */
    @ExceptionHandler(value = CustomizeRequestException.class)
    public ResponseEntity<ApiError> customizeRequestException(CustomizeRequestException e) {
        // 打印堆栈信息
        logger.error(getStackTrace(e));
        ApiError apiError = new ApiError(e.getCode(), e.getMessage());
        return new ResponseEntity(apiError, HttpStatus.valueOf(apiError.getStatus()));
    }

    /**
     * 处理请求异常
     * @param BadRequestException
     * @return
     */
    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<ApiError> badRequestException(BadRequestException e) {
        // 打印堆栈信息
        logger.error(getStackTrace(e));
        ApiError apiError = new ApiError(e.getStatus(), e.getMessage());
        return new ResponseEntity(apiError, HttpStatus.valueOf(apiError.getStatus()));
    }

    /**
     * 处理业务异常
     * @param CustomizeBusinessException
     * @return
     */
    @ExceptionHandler(value = CustomizeBusinessException.class)
    public ResponseEntity<ApiError> customizeBusinessException(CustomizeBusinessException e) {
        // 打印堆栈信息
        logger.error(getStackTrace(e));
        ApiError apiError = new ApiError(e.getCode(), e.getMessage());
        return new ResponseEntity(apiError, HttpStatus.valueOf(apiError.getStatus()));
    }

    /**
     * 处理未知异常
     * @param Throwable
     * @return
     */
    @ExceptionHandler(Throwable.class)
    public ResponseEntity handleException(Throwable e){
        // 打印堆栈信息
        logger.error(getStackTrace(e));
        ApiError apiError = new ApiError(BAD_REQUEST.value(), e.getMessage());
        return new ResponseEntity(apiError, HttpStatus.valueOf(apiError.getStatus()));
    }

    /**
     * 获取堆栈信息
     * @param throwable
     * @return
     */
    private String getStackTrace(Throwable throwable){
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        try {
            throwable.printStackTrace(printWriter);
            return stringWriter.toString();
        } finally {
            printWriter.close();
        }
    }
}
