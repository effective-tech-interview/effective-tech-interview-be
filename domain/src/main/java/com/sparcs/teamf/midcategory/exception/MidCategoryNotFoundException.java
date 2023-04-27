package com.sparcs.teamf.midcategory.exception;

import com.sparcs.teamf.exception.HttpException;
import org.springframework.http.HttpStatus;

public class MidCategoryNotFoundException extends HttpException {

    public MidCategoryNotFoundException() {
        super(HttpStatus.NOT_FOUND, "midCategory not found");
    }
}
