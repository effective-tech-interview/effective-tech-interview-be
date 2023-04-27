package com.sparcs.teamf.exception;

import org.springframework.http.HttpStatus;

public class EmailSenderException extends HttpException {

    public EmailSenderException() {
        super(HttpStatus.BAD_REQUEST, "email sending failed");
    }
}
