package com.sparcs.teamf.error.handler;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.sparcs.teamf.dto.ErrorResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ErrorResponseDto> onMissingRequestHeader(MissingRequestHeaderException e) {
        return ResponseEntity.badRequest()
            .body(new ErrorResponseDto(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDto> onHttpMessageNotReadable(HttpMessageNotReadableException e) {
        if (e.getCause() instanceof MismatchedInputException mismatchedInputException) {
            return ResponseEntity.badRequest()
                .body(new ErrorResponseDto(
                    HttpStatus.BAD_REQUEST.value(),
                    mismatchedInputException.getPath().get(0).getFieldName() + " 필드의 값이 잘못되었습니다."));
        }
        return ResponseEntity.badRequest()
            .body(new ErrorResponseDto(HttpStatus.BAD_REQUEST.value(), "확인할 수 없는 형태의 데이터가 들어왔습니다"));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponseDto> onHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
            .body(new ErrorResponseDto(HttpStatus.METHOD_NOT_ALLOWED.value(), "지원하지 않는 HTTP 메서드입니다."));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> allUncaughtHandle(Exception e) {
        log.error("allUncaughtHandle : {}", e);
        return ResponseEntity.internalServerError().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponseDto> methodArgumentNotValidExceptionHandle(
        MethodArgumentNotValidException e) {
        log.error("methodArgumentNotValidException : {}", e);
        return ResponseEntity.badRequest()
            .body(new ErrorResponseDto(HttpStatus.BAD_REQUEST.value(),
                e.getFieldErrors().get(0).getDefaultMessage()));
    }

    @ExceptionHandler(MissingRequestCookieException.class)
    protected ResponseEntity<ErrorResponseDto> missingRequestCookieExceptionHandle(
        MissingRequestCookieException e) {
        log.error("missingRequestCookieException : {}", e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(new ErrorResponseDto(HttpStatus.UNAUTHORIZED.value(), e.getMessage()));
    }
}
