package com.sparcs.teamf.exception;

import org.springframework.http.HttpStatus;

public class PasswordMismatchException extends HttpException {

    public PasswordMismatchException() {
        super(HttpStatus.BAD_REQUEST, "password mismatch");
    }
}
