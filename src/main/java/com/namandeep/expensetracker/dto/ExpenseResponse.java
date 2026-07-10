package com.namandeep.expensetracker.dto;

import com.namandeep.expensetracker.entity.ExpenseType;
import com.namandeep.expensetracker.entity.PaymentMethod;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

/** Expense representation returned by the API. */
public record ExpenseResponse(
        Long id,
        String title,
        BigDecimal amount,
        String description,
        LocalDate expenseDate,
        PaymentMethod paymentMethod,
        Long categoryId,
        String category,
        ExpenseType expenseType,
        Instant createdAt,
        Instant updatedAt,
        Long userId) {
}
