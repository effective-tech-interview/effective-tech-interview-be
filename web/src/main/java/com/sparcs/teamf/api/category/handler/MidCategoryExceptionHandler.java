package com.sparcs.teamf.api.category.handler;

import com.sparcs.teamf.dto.ErrorResponseDto;
import com.sparcs.teamf.midcategory.exception.MidCategoryNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class MidCategoryExceptionHandler {

    @ExceptionHandler
    private ResponseEntity<ErrorResponseDto> handleMainCategoryNotFound(MidCategoryNotFoundException e) {
        log.error("MidCategoryNotFoundException", e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponseDto(HttpStatus.NOT_FOUND.value(), "MainCategory not found"));
    }
}
