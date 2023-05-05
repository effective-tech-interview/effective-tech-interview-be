package com.sparcs.teamf.api.auth.handler;

import com.sparcs.teamf.dto.ErrorResponseDto;
import com.sparcs.teamf.exception.DuplicateEmailException;
import com.sparcs.teamf.exception.EmailRequestRequiredException;
import com.sparcs.teamf.exception.EmailSenderException;
import com.sparcs.teamf.exception.MemberNotFoundException;
import com.sparcs.teamf.exception.PasswordMismatchException;
import com.sparcs.teamf.exception.RefreshTokenValidationException;
import com.sparcs.teamf.exception.UnverifiedEmailException;
import com.sparcs.teamf.exception.VerificationCodeMismatchException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class AuthExceptionHandler {

    @ExceptionHandler
    private ResponseEntity<ErrorResponseDto> handleDuplicateEmail(DuplicateEmailException e) {
        log.warn("duplicateEmailException : {}", e);
        return ResponseEntity.badRequest()
                .body(new ErrorResponseDto(HttpStatus.BAD_REQUEST.value(), "the email is already registered"));
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponseDto> handleEmailRequestRequired(EmailRequestRequiredException e) {
        log.warn("emailRequestRequiredException : {}", e);
        return ResponseEntity.badRequest()
                .body(new ErrorResponseDto(HttpStatus.BAD_REQUEST.value(),
                        "email request required before verifying your verification code."));
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponseDto> handleEmailSender(EmailSenderException e) {
        log.error("emailSenderException : {}", e);
        return ResponseEntity.badRequest()
                .body(new ErrorResponseDto(HttpStatus.BAD_REQUEST.value(), "email sending failed"));
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponseDto> handleMemberNotFound(MemberNotFoundException e) {
        log.error("MemberNotFoundException : {}", e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponseDto(HttpStatus.NOT_FOUND.value(), "member not found"));
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponseDto> handlePasswordMismatch(PasswordMismatchException e) {
        log.error("PasswordMismatchException : {}", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseDto(HttpStatus.BAD_REQUEST.value(), "password mismatch"));
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponseDto> handleRefreshTokenValidation(RefreshTokenValidationException e) {
        log.error("RefreshTokenValidationException : {}", e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponseDto(HttpStatus.UNAUTHORIZED.value(), "invalid or expired refresh token"));
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponseDto> handleUnverifiedEmail(UnverifiedEmailException e) {
        log.error("UnverifiedEmailException : {}", e);
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponseDto(HttpStatus.FORBIDDEN.value(), "unauthenticated email address"));
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponseDto> handleVerificationCodeMismatch(VerificationCodeMismatchException e) {
        log.error("VerificationCodeMismatchException : {}", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseDto(HttpStatus.BAD_REQUEST.value(), "verification code mismatch"));
    }
}
