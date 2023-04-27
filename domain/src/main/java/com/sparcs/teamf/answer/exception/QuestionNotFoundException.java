package com.sparcs.teamf.answer.exception;

import com.sparcs.teamf.exception.HttpException;
import org.springframework.http.HttpStatus;

public class QuestionNotFoundException extends HttpException {

    public QuestionNotFoundException() {
        super(HttpStatus.NOT_FOUND, "question not found");
    }
}
