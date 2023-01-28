package com.sparcs.teamf.api.maincategory.service;

import com.sparcs.teamf.api.maincategory.dto.MainCategoriesResponse;
import com.sparcs.teamf.api.maincategory.dto.MainCategoryResponse;
import com.sparcs.teamf.domain.maincategory.MainCategory;
import com.sparcs.teamf.domain.maincategory.MainCategoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MainCategoryService {

    private final MainCategoryRepository mainCategoryRepository;

    public MainCategoriesResponse getAll() {
        List<MainCategory> mainCategories = mainCategoryRepository.findAll();
        List<MainCategoryResponse> mainCategoryDtos = mainCategories.stream()
                .map(mainCategory -> new MainCategoryResponse(mainCategory.getId(), mainCategory.getName()))
                .toList();

        System.out.println(mainCategoryDtos);
        return new MainCategoriesResponse(mainCategoryDtos);
    }
}
