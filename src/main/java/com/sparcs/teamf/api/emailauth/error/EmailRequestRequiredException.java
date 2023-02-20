package com.sparcs.teamf.api.emailauth.error;

import com.sparcs.teamf.api.error.exception.HttpException;
import org.springframework.http.HttpStatus;

public class EmailRequestRequiredException extends HttpException {

    public EmailRequestRequiredException() {
        super(HttpStatus.UNAUTHORIZED, "email request required before verifying your verification code.");
    }
}
