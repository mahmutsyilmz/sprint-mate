package com.yilmaz.sprintmate.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Base Application Exception
 * 
 * All custom exceptions extend this class.
 * Error code system returns meaningful errors to frontend.
 */
@Getter
public class BaseException extends RuntimeException {

    private final HttpStatus status;
    private final String errorCode;

    public BaseException(String message, HttpStatus status, String errorCode) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
    }

    public BaseException(String message, HttpStatus status, String errorCode, Throwable cause) {
        super(message, cause);
        this.status = status;
        this.errorCode = errorCode;
    }
}
