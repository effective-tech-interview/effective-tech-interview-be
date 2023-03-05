package com.sparcs.teamf.api.error.handler;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.sparcs.teamf.api.error.dto.ErrorResponseDto;
import com.sparcs.teamf.api.error.exception.HttpException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

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

    @ExceptionHandler(HttpException.class)
    public ResponseEntity<ErrorResponseDto> httpExceptionHandle(HttpException e) {
        log.error("httpException : {} {}", e, e.getMessage());
        return ResponseEntity.status(e.getCode())
                .body(new ErrorResponseDto(e.getCode(), e.getMessage()));
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
}
