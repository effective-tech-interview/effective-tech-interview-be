package com.sparcs.teamf.api.emailauth.error;

import com.sparcs.teamf.api.error.exception.HttpException;
import org.springframework.http.HttpStatus;

/**
 * 인증만 하고 회원 가입은 하지 않은 상태에서 다시 같은 이메일 링크로 인증을 받는 경우 호출되는 에러 클래스
 */
public class EmailVerificationExpiredException extends HttpException {

    public EmailVerificationExpiredException() {
        super(HttpStatus.UNAUTHORIZED, "this email verification request has expired. please send an email request for verification again.");
    }

}
