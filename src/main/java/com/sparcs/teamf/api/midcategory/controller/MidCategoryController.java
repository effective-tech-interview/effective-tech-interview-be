package com.sparcs.teamf.api.midcategory.controller;

import com.sparcs.teamf.api.midcategory.dto.MidCategoriesResponse;
import com.sparcs.teamf.api.midcategory.dto.MidCategoryResponse;
import com.sparcs.teamf.api.midcategory.service.MidCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/categories/mid")
@RequiredArgsConstructor
@Tag(name = "Category")
public class MidCategoryController {

    private final MidCategoryService midCategoryService;

    @GetMapping
    @Operation(summary = "세부 카테고리 목록 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = MidCategoriesResponse.class))}),
            @ApiResponse(responseCode = "500", description = "internal server error", content = @Content),
            @ApiResponse(responseCode = "401", description = "unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "not found", content = @Content)})
    public MidCategoriesResponse getMidCategories(@RequestParam Long mainCategoryId) {
        return midCategoryService.getByMainCategory(mainCategoryId);
    }

    @GetMapping("/{midCategoryId}")
    @Operation(summary = "세부 카테고리 단일 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = MidCategoryResponse.class))}),
            @ApiResponse(responseCode = "500", description = "internal server error", content = @Content),
            @ApiResponse(responseCode = "401", description = "unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "not found", content = @Content)})
    public MidCategoryResponse getMidCategory(@PathVariable long midCategoryId) {
        return midCategoryService.getByMidCategoryId(midCategoryId);
    }
}
