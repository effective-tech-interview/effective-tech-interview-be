package com.sparcs.teamf.api.question.exception;

import com.sparcs.teamf.api.error.exception.HttpException;
import org.springframework.http.HttpStatus;

public class MaximumTailQuestionExceededException extends HttpException {

    public MaximumTailQuestionExceededException() {
        super(HttpStatus.BAD_REQUEST, "maximum number of tail questions exceeded");
    }
}
