package com.sparcs.teamf.exception;

import org.springframework.http.HttpStatus;

public class RefreshTokenValidationException extends HttpException {

    public RefreshTokenValidationException() {
        super(HttpStatus.UNAUTHORIZED, "invalid or expired refresh token");
    }
}
