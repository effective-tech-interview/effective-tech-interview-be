package com.sparcs.teamf.exception;

import org.springframework.http.HttpStatus;

public class DuplicateEmailException extends HttpException {

    public DuplicateEmailException() {
        super(HttpStatus.CONFLICT, "the email is already registered");
    }
}
