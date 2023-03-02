package com.sparcs.teamf.api.member.exception;

import com.sparcs.teamf.api.error.exception.HttpException;
import org.springframework.http.HttpStatus;

public class DuplicateEmailException extends HttpException {

    public DuplicateEmailException() {
        super(HttpStatus.CONFLICT, "the email is already registered");
    }
}
