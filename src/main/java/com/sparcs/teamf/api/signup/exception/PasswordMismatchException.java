package com.sparcs.teamf.api.signup.exception;

import com.sparcs.teamf.api.error.exception.HttpException;
import org.springframework.http.HttpStatus;

public class PasswordMismatchException extends HttpException {

    public PasswordMismatchException() {
        super(HttpStatus.BAD_REQUEST, "password mismatch");
    }
}
