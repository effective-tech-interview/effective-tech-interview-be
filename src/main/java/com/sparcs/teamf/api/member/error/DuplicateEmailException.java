package com.sparcs.teamf.api.member.error;

import com.sparcs.teamf.api.error.exception.HttpException;
import org.springframework.http.HttpStatus;

public class DuplicateEmailException extends HttpException {

    public DuplicateEmailException() {
        super(HttpStatus.CONFLICT, "email address already exists");
    }
}
