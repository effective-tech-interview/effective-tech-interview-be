package com.sparcs.teamf.api.member.exception;

import com.sparcs.teamf.api.error.exception.HttpException;
import org.springframework.http.HttpStatus;

public class MemberNotFoundException extends HttpException {

    public MemberNotFoundException() {
        super(HttpStatus.NOT_FOUND, "member not found");
    }
}
