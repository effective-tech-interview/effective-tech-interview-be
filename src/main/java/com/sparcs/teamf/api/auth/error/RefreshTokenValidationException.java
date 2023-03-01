package com.sparcs.teamf.api.auth.error;

import com.sparcs.teamf.api.error.exception.HttpException;
import org.springframework.http.HttpStatus;

public class RefreshTokenValidationException extends HttpException {

    public RefreshTokenValidationException() {
        super(HttpStatus.UNAUTHORIZED, "invalid or expired refresh token");
    }
}
