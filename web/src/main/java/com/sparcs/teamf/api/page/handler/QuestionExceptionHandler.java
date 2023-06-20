package com.sparcs.teamf.api.page.handler;

import com.sparcs.teamf.dto.ErrorResponseDto;
import com.sparcs.teamf.page.exception.IllegalMidCategoryException;
import com.sparcs.teamf.page.exception.MemberNotFoundException;
import com.sparcs.teamf.page.exception.PageNotFountException;
import com.sparcs.teamf.page.exception.PageOwnerMismatchException;
import com.sparcs.teamf.page.exception.PageQuestionMismatchException;
import com.sparcs.teamf.page.exception.QuestionNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class QuestionExceptionHandler {

    @ExceptionHandler
    private ResponseEntity<ErrorResponseDto> handleIllegalMidCategory(IllegalMidCategoryException e) {
        log.warn("illegalMidCategoryException : {}", e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponseDto(HttpStatus.NOT_FOUND.value(), "중분류 카테고리를 선택해주세요"));
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponseDto> handleMemberNotFound(MemberNotFoundException e) {
        log.warn("MemberNotFoundException : {}", e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponseDto(HttpStatus.NOT_FOUND.value(), "member not found"));
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponseDto> handleQuestionNotFound(QuestionNotFoundException e) {
        log.warn("QuestionNotFoundException : {}", e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponseDto(HttpStatus.NOT_FOUND.value(), "Question not found"));
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponseDto> handlePageNotFound(PageNotFountException e) {
        log.warn("PageNotFountException : {}", e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponseDto(HttpStatus.NOT_FOUND.value(), "Page not found"));
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponseDto> handlePageOwnerMismatch(PageOwnerMismatchException e) {
        log.warn("PageOwnerMismatchException : {}", e);
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponseDto(HttpStatus.FORBIDDEN.value(),
                        "page owner and authenticated member do not match."));
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponseDto> handlePageQuestionMismatch(PageQuestionMismatchException e) {
        log.warn("PageQuestionMismatchException : {}", e);
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponseDto(HttpStatus.FORBIDDEN.value(),
                        "page and page question do not match."));
    }
}
