package com.namandeep.expensetracker.service;

import com.namandeep.expensetracker.dto.CategoryResponse;
import com.namandeep.expensetracker.dto.CreateCategoryRequest;
import com.namandeep.expensetracker.dto.PageResponse;
import com.namandeep.expensetracker.dto.UpdateCategoryRequest;
import org.springframework.data.domain.Pageable;

/** Contract for management of categories owned by the authenticated user. */
public interface CategoryService {

    CategoryResponse create(String userEmail, CreateCategoryRequest request);

    CategoryResponse update(String userEmail, Long categoryId, UpdateCategoryRequest request);

    void delete(String userEmail, Long categoryId, Long reassignToCategoryId);

    CategoryResponse getById(String userEmail, Long categoryId);

    PageResponse<CategoryResponse> getAll(String userEmail, String search, Pageable pageable);
}
