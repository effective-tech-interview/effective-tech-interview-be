package com.sparcs.teamf.api.maincategory.exception;

import com.sparcs.teamf.api.error.exception.HttpException;
import org.springframework.http.HttpStatus;

public class MainCategoryNotFoundException extends HttpException {

    public MainCategoryNotFoundException() {
        super(HttpStatus.NOT_FOUND, "MainCategory not found");
    }
}
