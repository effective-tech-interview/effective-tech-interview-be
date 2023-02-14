package com.sparcs.teamf.api.midcategory.exception;

import com.sparcs.teamf.api.error.exception.HttpException;
import org.springframework.http.HttpStatus;

public class MidCategoryNotFoundException extends HttpException {

    public MidCategoryNotFoundException() {
        super(HttpStatus.NOT_FOUND, "midCategory not found");
    }
}
