package com.sparcs.teamf.api.answer.exception;

import com.sparcs.teamf.api.error.exception.HttpException;
import org.springframework.http.HttpStatus;

public class PageQuestionNotFoundException extends HttpException {

    public PageQuestionNotFoundException() {
        super(HttpStatus.NOT_FOUND, "no data found for this page and question");
    }
}
