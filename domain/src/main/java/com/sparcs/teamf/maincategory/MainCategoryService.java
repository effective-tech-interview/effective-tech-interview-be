package com.sparcs.teamf.maincategory;

import com.sparcs.teamf.maincategory.dto.MainCategoriesResponse;
import com.sparcs.teamf.maincategory.dto.MainCategoryResponse;
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
        return new MainCategoriesResponse(mainCategoryDtos);
    }
}
