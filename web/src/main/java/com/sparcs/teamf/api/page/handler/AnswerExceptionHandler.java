package com.sparcs.teamf.api.page.handler;

import com.sparcs.teamf.answer.exception.AnswerNotFoundException;
import com.sparcs.teamf.answer.exception.PageQuestionNotFoundException;
import com.sparcs.teamf.dto.ErrorResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class AnswerExceptionHandler {

    @ExceptionHandler
    private ResponseEntity<ErrorResponseDto> handleAnswerNotFound(AnswerNotFoundException e) {
        log.warn("AnswerNotFoundException : {}", e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponseDto(HttpStatus.NOT_FOUND.value(), "Answer not found"));
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponseDto> handlePageQuestionNotFound(PageQuestionNotFoundException e) {
        log.warn("PageQuestionNotFoundException : {}", e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponseDto(HttpStatus.NOT_FOUND.value(), "no data found for this page and question"));
    }
}
