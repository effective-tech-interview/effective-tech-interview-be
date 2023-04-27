package com.sparcs.teamf.exception;

import org.springframework.http.HttpStatus;

public class UnverifiedEmailException extends HttpException {

    public UnverifiedEmailException() {
        super(HttpStatus.FORBIDDEN, "unauthenticated email address");
    }
}
