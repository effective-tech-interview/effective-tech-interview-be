package com.sparcs.teamf.api.emailauth.error;

import com.sparcs.teamf.api.error.exception.HttpException;
import org.springframework.http.HttpStatus;

public class InvalidEmailOrVerificationCodeException extends HttpException {

    public InvalidEmailOrVerificationCodeException() {
        super(HttpStatus.BAD_REQUEST, "this email does not exist or the verification code has already been verified.");
    }
}
