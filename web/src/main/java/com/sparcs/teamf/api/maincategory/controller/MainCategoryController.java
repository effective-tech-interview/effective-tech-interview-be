package com.sparcs.teamf.api.maincategory.controller;

import com.sparcs.teamf.maincategory.MainCategoryService;
import com.sparcs.teamf.maincategory.dto.MainCategoriesResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
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
    @Operation(summary = "메인 카테고리 목록 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = MainCategoriesResponse.class))}),
            @ApiResponse(responseCode = "500", description = "internal server error", content = @Content),
            @ApiResponse(responseCode = "401", description = "unauthorized", content = @Content)})
    public MainCategoriesResponse getMainCategories() {
        return mainCategoryService.getAll();
    }
}
