package com.sparcs.teamf.api.emailauth.exception;

import com.sparcs.teamf.api.error.exception.HttpException;
import org.springframework.http.HttpStatus;

public class EmailSenderException extends HttpException {

    public EmailSenderException() {
        super(HttpStatus.BAD_REQUEST, "email sending failed");
    }
}
