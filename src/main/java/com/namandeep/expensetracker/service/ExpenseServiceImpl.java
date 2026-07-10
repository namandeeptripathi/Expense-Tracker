package com.namandeep.expensetracker.service;

import com.namandeep.expensetracker.dto.CreateExpenseRequest;
import com.namandeep.expensetracker.dto.ExpenseFilter;
import com.namandeep.expensetracker.dto.ExpenseResponse;
import com.namandeep.expensetracker.dto.PageResponse;
import com.namandeep.expensetracker.dto.UpdateExpenseRequest;
import com.namandeep.expensetracker.entity.Category;
import com.namandeep.expensetracker.entity.Expense;
import com.namandeep.expensetracker.entity.User;
import com.namandeep.expensetracker.exception.ExpenseNotFoundException;
import com.namandeep.expensetracker.repository.CategoryRepository;
import com.namandeep.expensetracker.repository.ExpenseRepository;
import com.namandeep.expensetracker.repository.ExpenseSpecifications;
import com.namandeep.expensetracker.repository.UserRepository;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Transactional implementation of expense management for the current user. */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ExpenseMapper expenseMapper;

    @Override
    @Transactional
    public ExpenseResponse create(String userEmail, CreateExpenseRequest request) {
        User user = currentUser(userEmail);
        Category category = resolveCategory(request.category(), user);
        Expense expense = expenseMapper.toEntity(request, user, category);
        return expenseMapper.toResponse(expenseRepository.save(expense));
    }

    @Override
    @Transactional
    public ExpenseResponse update(String userEmail, Long expenseId, UpdateExpenseRequest request) {
        User user = currentUser(userEmail);
        Expense expense = findOwnedExpense(expenseId, user.getId());
        expenseMapper.updateEntity(expense, request, resolveCategory(request.category(), user));
        return expenseMapper.toResponse(expense);
    }

    @Override
    @Transactional
    public void delete(String userEmail, Long expenseId) {
        User user = currentUser(userEmail);
        expenseRepository.delete(findOwnedExpense(expenseId, user.getId()));
    }

    @Override
    public ExpenseResponse getById(String userEmail, Long expenseId) {
        User user = currentUser(userEmail);
        return expenseMapper.toResponse(findOwnedExpense(expenseId, user.getId()));
    }

    @Override
    public PageResponse<ExpenseResponse> getAll(String userEmail, ExpenseFilter filter, Pageable pageable) {
        User user = currentUser(userEmail);
        Page<ExpenseResponse> page = expenseRepository
                .findAll(ExpenseSpecifications.forUserAndFilter(user.getId(), filter), pageable)
                .map(expenseMapper::toResponse);
        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast(),
                pageable.getSort().toString());
    }

    private User currentUser(String userEmail) {
        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ExpenseNotFoundException("Authenticated user was not found"));
    }

    private Expense findOwnedExpense(Long expenseId, Long userId) {
        return expenseRepository.findByIdAndUserId(expenseId, userId)
                .orElseThrow(() -> new ExpenseNotFoundException("Expense not found: " + expenseId));
    }

    private Category resolveCategory(String categoryName, User user) {
        String normalizedName = categoryName.trim().toLowerCase(Locale.ROOT);
        return categoryRepository.findByUserIdAndNameIgnoreCase(user.getId(), normalizedName)
                .orElseGet(() -> categoryRepository.save(Category.builder()
                        .name(categoryName.trim())
                        .user(user)
                        .build()));
    }
}
