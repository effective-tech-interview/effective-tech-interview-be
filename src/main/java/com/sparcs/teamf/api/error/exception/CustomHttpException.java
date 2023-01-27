package com.sparcs.teamf.api.error.exception;

import org.springframework.http.HttpStatus;

/**
 * HttpException 을 상속 받는 예외 처리 예시 클래스
 */
public class CustomHttpException extends HttpException{

     CustomHttpException() {
        super(HttpStatus.BAD_REQUEST, "요청을 처리하는 과정에서 예상치 못한 오류가 발생하였습니다.");
    }


}
