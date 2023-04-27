package com.sparcs.teamf.maincategory.exception;

import com.sparcs.teamf.exception.HttpException;
import org.springframework.http.HttpStatus;

public class MainCategoryNotFoundException extends HttpException {

    public MainCategoryNotFoundException() {
        super(HttpStatus.NOT_FOUND, "MainCategory not found");
    }
}
