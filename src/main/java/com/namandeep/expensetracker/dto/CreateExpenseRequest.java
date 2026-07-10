package com.namandeep.expensetracker.dto;

import com.namandeep.expensetracker.entity.ExpenseType;
import com.namandeep.expensetracker.entity.PaymentMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

/** Validated input used to create an expense. */
public record CreateExpenseRequest(
        @Schema(example = "Weekly groceries")
        @NotBlank(message = "Title is required")
        @Size(max = 150, message = "Title must not exceed 150 characters")
        String title,

        @Schema(example = "1250.50")
        @NotNull(message = "Amount is required")
        @DecimalMin(value = "0.01", message = "Amount must be positive")
        @Digits(integer = 17, fraction = 2, message = "Amount must have at most 17 integer digits and 2 decimal places")
        BigDecimal amount,

        @Schema(example = "Monthly household groceries")
        @Size(max = 1000, message = "Description must not exceed 1000 characters")
        String description,

        @Schema(example = "2026-07-10")
        @NotNull(message = "Expense date is required")
        LocalDate expenseDate,

        @NotNull(message = "Payment method is required")
        PaymentMethod paymentMethod,

        @Schema(example = "Groceries")
        @NotBlank(message = "Category is required")
        @Size(max = 100, message = "Category must not exceed 100 characters")
        String category,

        @NotNull(message = "Expense type is required")
        ExpenseType expenseType) {
}
