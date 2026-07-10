package com.namandeep.expensetracker.service;

import com.namandeep.expensetracker.dto.BudgetResponse;
import com.namandeep.expensetracker.dto.CreateBudgetRequest;
import com.namandeep.expensetracker.dto.UpdateBudgetRequest;
import com.namandeep.expensetracker.entity.Budget;
import com.namandeep.expensetracker.entity.User;
import com.namandeep.expensetracker.repository.BudgetRepository;
import com.namandeep.expensetracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BudgetServiceImpl implements BudgetService {

    private final BudgetRepository budgetRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public BudgetResponse create(String userEmail, CreateBudgetRequest request) {

        User user = currentUser(userEmail);

        Budget budget = Budget.builder()
                .monthlyLimit(request.getMonthlyLimit())
                .month(request.getMonth())
                .year(request.getYear())
                .user(user)
                .build();

        return toResponse(budgetRepository.save(budget));
    }

    @Override
    @Transactional
    public BudgetResponse update(String userEmail, Long budgetId, UpdateBudgetRequest request) {

        User user = currentUser(userEmail);

        Budget budget = budgetRepository.findByIdAndUserId(budgetId, user.getId())
                .orElseThrow(() -> new RuntimeException("Budget not found"));

        budget.setMonthlyLimit(request.getMonthlyLimit());
        budget.setMonth(request.getMonth());
        budget.setYear(request.getYear());

        return toResponse(budget);
    }

    @Override
    @Transactional
    public void delete(String userEmail, Long budgetId) {

        User user = currentUser(userEmail);

        Budget budget = budgetRepository.findByIdAndUserId(budgetId, user.getId())
                .orElseThrow(() -> new RuntimeException("Budget not found"));

        budgetRepository.delete(budget);
    }

    @Override
    public BudgetResponse getById(String userEmail, Long budgetId) {

        User user = currentUser(userEmail);

        Budget budget = budgetRepository.findByIdAndUserId(budgetId, user.getId())
                .orElseThrow(() -> new RuntimeException("Budget not found"));

        return toResponse(budget);
    }

    private User currentUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private BudgetResponse toResponse(Budget budget) {
        return BudgetResponse.builder()
                .id(budget.getId())
                .monthlyLimit(budget.getMonthlyLimit())
                .month(budget.getMonth())
                .year(budget.getYear())
                .createdAt(budget.getCreatedAt())
                .updatedAt(budget.getUpdatedAt())
                .build();
    }
}