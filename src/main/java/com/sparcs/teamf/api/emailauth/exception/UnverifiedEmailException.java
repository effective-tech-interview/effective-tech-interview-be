package com.sparcs.teamf.api.emailauth.exception;

import com.sparcs.teamf.api.error.exception.HttpException;
import org.springframework.http.HttpStatus;

public class UnverifiedEmailException extends HttpException {

    public UnverifiedEmailException() {
        super(HttpStatus.UNAUTHORIZED, "unauthenticated email address");
    }
}
