package com.sparcs.teamf.exception;

import org.springframework.http.HttpStatus;

public class EmailRequestRequiredException extends HttpException {

    public EmailRequestRequiredException() {
        super(HttpStatus.BAD_REQUEST, "email request required before verifying your verification code.");
    }
}
