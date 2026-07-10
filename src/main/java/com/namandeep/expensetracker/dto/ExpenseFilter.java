package com.namandeep.expensetracker.dto;

import com.namandeep.expensetracker.entity.PaymentMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

/** Optional query filters applied to the current user's expenses. */
public record ExpenseFilter(
        @Schema(description = "Partial, case-insensitive title match")
        @Size(max = 150, message = "Title search must not exceed 150 characters")
        String title,
        @Size(max = 100, message = "Category filter must not exceed 100 characters")
        String category,
        PaymentMethod paymentMethod,
        LocalDate startDate,
        LocalDate endDate,
        @DecimalMin(value = "0.00", message = "Minimum amount cannot be negative")
        BigDecimal minAmount,
        @DecimalMin(value = "0.00", message = "Maximum amount cannot be negative")
        BigDecimal maxAmount) {

    @AssertTrue(message = "Start date must be on or before end date")
    public boolean isDateRangeValid() {
        return startDate == null || endDate == null || !startDate.isAfter(endDate);
    }

    @AssertTrue(message = "Minimum amount must not exceed maximum amount")
    public boolean isAmountRangeValid() {
        return minAmount == null || maxAmount == null || minAmount.compareTo(maxAmount) <= 0;
    }
}
