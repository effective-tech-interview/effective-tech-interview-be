package com.sparcs.teamf.api.error.handler;

import com.sparcs.teamf.api.error.dto.ErrorResponseDto;
import com.sparcs.teamf.api.error.exception.HttpException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpException.class)
    public ResponseEntity<ErrorResponseDto> httpExceptionHandle(HttpException e) {
        log.error("httpException : {}", e);
        return ResponseEntity.status(e.getCode())
                .body(new ErrorResponseDto(e.getCode(), e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> allUncaughtHandle(Exception e) {
        log.error("allUncaughtHandle : {}", e);
        return ResponseEntity.internalServerError().build();
    }
}
