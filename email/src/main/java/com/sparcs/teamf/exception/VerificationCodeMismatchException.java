package com.sparcs.teamf.exception;

import org.springframework.http.HttpStatus;

public class VerificationCodeMismatchException extends HttpException {

    public VerificationCodeMismatchException() {
        super(HttpStatus.BAD_REQUEST, "verification code mismatch");
    }
}
