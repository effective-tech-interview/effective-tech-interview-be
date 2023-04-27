package com.sparcs.teamf.exception;


import org.springframework.http.HttpStatus;

public class MemberNotFoundException extends HttpException {

    public MemberNotFoundException() {
        super(HttpStatus.NOT_FOUND, "member not found");
    }
}
