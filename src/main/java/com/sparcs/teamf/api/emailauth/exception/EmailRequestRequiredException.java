package com.sparcs.teamf.api.emailauth.exception;

import com.sparcs.teamf.api.error.exception.HttpException;
import org.springframework.http.HttpStatus;

public class EmailRequestRequiredException extends HttpException {

    public EmailRequestRequiredException() {
        super(HttpStatus.BAD_REQUEST, "email request required before verifying your verification code.");
    }
}
