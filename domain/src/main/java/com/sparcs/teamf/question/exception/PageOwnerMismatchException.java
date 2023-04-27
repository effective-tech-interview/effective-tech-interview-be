package com.sparcs.teamf.question.exception;

import com.sparcs.teamf.exception.HttpException;
import org.springframework.http.HttpStatus;

public class PageOwnerMismatchException extends HttpException {

    public PageOwnerMismatchException() {
        super(HttpStatus.FORBIDDEN, "page owner and authenticated member do not match.");
    }
}
