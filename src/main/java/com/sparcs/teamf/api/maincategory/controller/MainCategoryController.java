package com.sparcs.teamf.api.maincategory.controller;

import com.sparcs.teamf.api.maincategory.dto.MainCategoriesResponse;
import com.sparcs.teamf.api.maincategory.service.MainCategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/categories/main")
@RequiredArgsConstructor
@Tag(name = "Category")
public class MainCategoryController {

    private final MainCategoryService mainCategoryService;

    @GetMapping
    public MainCategoriesResponse getMainCategories() {
        return mainCategoryService.getAll();
    }
}
