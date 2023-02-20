package com.sparcs.teamf.api.signup.error;

import com.sparcs.teamf.api.error.exception.HttpException;
import org.springframework.http.HttpStatus;

public class PasswordMismatchException extends HttpException {

    public PasswordMismatchException() {
        super(HttpStatus.UNAUTHORIZED, "password mismatch");
    }
}
