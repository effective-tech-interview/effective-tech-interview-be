package com.sparcs.teamf.question.exception;

import com.sparcs.teamf.exception.HttpException;
import org.springframework.http.HttpStatus;

public class IllegalMidCategoryException extends HttpException {

    public IllegalMidCategoryException() {
        super(HttpStatus.NOT_FOUND, "중분류 카테고리를 선택해주세요");
    }
}
