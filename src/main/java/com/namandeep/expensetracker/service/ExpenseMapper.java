package com.namandeep.expensetracker.service;

import com.namandeep.expensetracker.dto.CreateExpenseRequest;
import com.namandeep.expensetracker.dto.ExpenseResponse;
import com.namandeep.expensetracker.dto.UpdateExpenseRequest;
import com.namandeep.expensetracker.entity.Category;
import com.namandeep.expensetracker.entity.Expense;
import com.namandeep.expensetracker.entity.User;
import org.springframework.stereotype.Component;

/** Maps expense API models without exposing persistence entities. */
@Component
public class ExpenseMapper {

    public Expense toEntity(CreateExpenseRequest request, User user, Category category) {
        return Expense.builder()
                .title(request.title().trim())
                .amount(request.amount())
                .description(normalizeDescription(request.description()))
                .expenseDate(request.expenseDate())
                .paymentMethod(request.paymentMethod())
                .category(category)
                .expenseType(request.expenseType())
                .user(user)
                .build();
    }

    public void updateEntity(Expense expense, UpdateExpenseRequest request, Category category) {
        expense.setTitle(request.title().trim());
        expense.setAmount(request.amount());
        expense.setDescription(normalizeDescription(request.description()));
        expense.setExpenseDate(request.expenseDate());
        expense.setPaymentMethod(request.paymentMethod());
        expense.setCategory(category);
        expense.setExpenseType(request.expenseType());
    }

    public ExpenseResponse toResponse(Expense expense) {
        return new ExpenseResponse(
                expense.getId(),
                expense.getTitle(),
                expense.getAmount(),
                expense.getDescription(),
                expense.getExpenseDate(),
                expense.getPaymentMethod(),
                expense.getCategory().getId(),
                expense.getCategory().getName(),
                expense.getExpenseType(),
                expense.getCreatedAt(),
                expense.getUpdatedAt(),
                expense.getUser().getId());
    }

    private String normalizeDescription(String description) {
        return description == null || description.isBlank() ? null : description.trim();
    }
}
