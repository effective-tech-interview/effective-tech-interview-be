package com.sparcs.teamf.question.exception;

import com.sparcs.teamf.exception.HttpException;
import org.springframework.http.HttpStatus;

public class PageQuestionMismatchException extends HttpException {

    public PageQuestionMismatchException() {
        super(HttpStatus.FORBIDDEN, "page and page question do not match.");
    }
}
