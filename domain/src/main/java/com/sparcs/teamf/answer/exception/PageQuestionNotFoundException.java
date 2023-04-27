package com.sparcs.teamf.answer.exception;

import com.sparcs.teamf.exception.HttpException;
import org.springframework.http.HttpStatus;

public class PageQuestionNotFoundException extends HttpException {

    public PageQuestionNotFoundException() {
        super(HttpStatus.NOT_FOUND, "no data found for this page and question");
    }
}
