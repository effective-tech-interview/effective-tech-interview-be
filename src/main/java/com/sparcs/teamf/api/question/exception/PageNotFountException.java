package com.sparcs.teamf.api.question.exception;

import com.sparcs.teamf.api.error.exception.HttpException;
import org.springframework.http.HttpStatus;

public class PageNotFountException extends HttpException {

    public PageNotFountException() {
        super(HttpStatus.NOT_FOUND, "page not found");
    }
}
