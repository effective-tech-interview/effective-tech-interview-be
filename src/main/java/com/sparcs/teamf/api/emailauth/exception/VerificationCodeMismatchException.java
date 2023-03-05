package com.sparcs.teamf.api.emailauth.exception;

import com.sparcs.teamf.api.error.exception.HttpException;
import org.springframework.http.HttpStatus;

public class VerificationCodeMismatchException extends HttpException {

    public VerificationCodeMismatchException() {
        super(HttpStatus.UNPROCESSABLE_ENTITY, "verification code mismatch");
    }
}
