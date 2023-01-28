package com.sparcs.teamf.api.midcategory.controller;

import com.sparcs.teamf.api.midcategory.dto.MidCategoriesResponse;
import com.sparcs.teamf.api.midcategory.dto.MidCategoryResponse;
import com.sparcs.teamf.api.midcategory.service.MidCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/categories/mid")
@RequiredArgsConstructor
public class MidCategoryController {

    private final MidCategoryService midCategoryService;

    @GetMapping
    public MidCategoriesResponse getMidCategories(@RequestParam Long mainCategoryId) {
        return midCategoryService.getByMainCategory(mainCategoryId);
    }

    @GetMapping("/{midCategoryId}")
    public MidCategoryResponse getMidCategories(@PathVariable long midCategoryId) {
        return midCategoryService.getByMidCategoryId(midCategoryId);
    }
}
