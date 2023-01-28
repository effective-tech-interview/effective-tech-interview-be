package com.sparcs.teamf.api.answer.exception;

import com.sparcs.teamf.api.error.exception.HttpException;
import org.springframework.http.HttpStatus;

public class AnswerNotFoundException extends HttpException {

    public AnswerNotFoundException() {
        super(HttpStatus.NOT_FOUND, "Answer not found");
    }
}
