package com.sparcs.teamf.answer.exception;

import com.sparcs.teamf.exception.HttpException;
import org.springframework.http.HttpStatus;

public class AnswerNotFoundException extends HttpException {

    public AnswerNotFoundException() {
        super(HttpStatus.NOT_FOUND, "Answer not found");
    }
}
