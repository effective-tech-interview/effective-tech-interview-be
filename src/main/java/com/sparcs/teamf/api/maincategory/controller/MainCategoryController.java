package com.sparcs.teamf.api.maincategory.controller;

import com.sparcs.teamf.api.maincategory.dto.MainCategoriesResponse;
import com.sparcs.teamf.api.maincategory.service.MainCategoryService;
import com.sparcs.teamf.domain.maincategory.MainCategory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/categories/main")
@RequiredArgsConstructor
public class MainCategoryController {

    private final MainCategoryService mainCategoryService;

    @GetMapping
    public MainCategoriesResponse getMainCategories() {
        return mainCategoryService.getMainCategories();
    }
}
