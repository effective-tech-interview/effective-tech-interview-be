package com.sparcs.teamf.api.answer.exception;

import com.sparcs.teamf.api.error.exception.HttpException;
import org.springframework.http.HttpStatus;

public class QuestionNotFoundException extends HttpException {

    public QuestionNotFoundException() {
        super(HttpStatus.NOT_FOUND, "question not found");
    }
}
