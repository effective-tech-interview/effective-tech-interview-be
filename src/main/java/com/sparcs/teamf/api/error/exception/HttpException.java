package com.sparcs.teamf.api.error.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class HttpException extends RuntimeException {

    private final int code;

    protected HttpException(HttpStatus httpStatus, String message) {
        super(message);
        code = httpStatus.value();
    }
}
