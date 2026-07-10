package com.namandeep.expensetracker.service;

import com.namandeep.expensetracker.dto.CategoryResponse;
import com.namandeep.expensetracker.dto.CreateCategoryRequest;
import com.namandeep.expensetracker.dto.PageResponse;
import com.namandeep.expensetracker.dto.UpdateCategoryRequest;
import com.namandeep.expensetracker.entity.Category;
import com.namandeep.expensetracker.entity.User;
import com.namandeep.expensetracker.exception.CategoryAlreadyExistsException;
import com.namandeep.expensetracker.exception.CategoryInUseException;
import com.namandeep.expensetracker.exception.CategoryNotFoundException;
import com.namandeep.expensetracker.exception.InvalidCategoryRequestException;
import com.namandeep.expensetracker.repository.CategoryRepository;
import com.namandeep.expensetracker.repository.ExpenseRepository;
import com.namandeep.expensetracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Transactional category management with expense-reference safety checks. */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public CategoryResponse create(String userEmail, CreateCategoryRequest request) {
        User user = currentUser(userEmail);
        String name = request.name().trim();
        if (categoryRepository.existsByUserIdAndNameIgnoreCase(user.getId(), name)) {
            throw new CategoryAlreadyExistsException(name);
        }

        Category category = categoryRepository.save(Category.builder()
                .name(name)
                .description(normalize(request.description()))
                .color(normalize(request.color()))
                .icon(normalize(request.icon()))
                .user(user)
                .build());
        return toResponse(category);
    }

    @Override
    @Transactional
    public CategoryResponse update(String userEmail, Long categoryId, UpdateCategoryRequest request) {
        User user = currentUser(userEmail);
        Category category = findOwnedCategory(categoryId, user.getId());
        String name = request.name().trim();
        if (categoryRepository.existsByUserIdAndNameIgnoreCaseAndIdNot(user.getId(), name, categoryId)) {
            throw new CategoryAlreadyExistsException(name);
        }

        category.setName(name);
        category.setDescription(normalize(request.description()));
        category.setColor(normalize(request.color()));
        category.setIcon(normalize(request.icon()));
        return toResponse(category);
    }

    @Override
    @Transactional
    public void delete(String userEmail, Long categoryId, Long reassignToCategoryId) {
        User user = currentUser(userEmail);
        Category source = findOwnedCategory(categoryId, user.getId());
        if (expenseRepository.countByCategoryId(categoryId) > 0) {
            if (reassignToCategoryId == null) {
                throw new CategoryInUseException();
            }
            if (categoryId.equals(reassignToCategoryId)) {
                throw new InvalidCategoryRequestException("A category cannot be reassigned to itself");
            }
            Category target = findOwnedCategory(reassignToCategoryId, user.getId());
            expenseRepository.reassignCategory(source.getId(), target);
        }
        categoryRepository.delete(source);
    }

    @Override
    public CategoryResponse getById(String userEmail, Long categoryId) {
        User user = currentUser(userEmail);
        return toResponse(findOwnedCategory(categoryId, user.getId()));
    }

    @Override
    public PageResponse<CategoryResponse> getAll(String userEmail, String search, Pageable pageable) {
        User user = currentUser(userEmail);
        Page<Category> page = search == null || search.isBlank()
                ? categoryRepository.findByUserId(user.getId(), pageable)
                : categoryRepository.findByUserIdAndNameContainingIgnoreCase(user.getId(), search.trim(), pageable);
        Page<CategoryResponse> responsePage = page.map(this::toResponse);
        return new PageResponse<>(
                responsePage.getContent(),
                responsePage.getNumber(),
                responsePage.getSize(),
                responsePage.getTotalElements(),
                responsePage.getTotalPages(),
                responsePage.isFirst(),
                responsePage.isLast(),
                pageable.getSort().toString());
    }

    private User currentUser(String userEmail) {
        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new CategoryNotFoundException("Authenticated user was not found"));
    }

    private Category findOwnedCategory(Long categoryId, Long userId) {
        return categoryRepository.findByIdAndUserId(categoryId, userId)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found: " + categoryId));
    }

    private CategoryResponse toResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getColor(),
                category.getIcon(),
                category.getCreatedAt(),
                category.getUpdatedAt(),
                category.getUser().getId());
    }

    private String normalize(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
