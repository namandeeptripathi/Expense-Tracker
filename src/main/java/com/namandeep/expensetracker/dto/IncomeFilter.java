package com.namandeep.expensetracker.dto;

import com.namandeep.expensetracker.entity.IncomeType;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

/** Optional query filters for the current user's income records. */
public record IncomeFilter(
        @Size(max = 150, message = "Source search must not exceed 150 characters")
        String source,
        IncomeType incomeType,
        @DecimalMin(value = "0.00", message = "Minimum amount cannot be negative")
        BigDecimal minAmount,
        @DecimalMin(value = "0.00", message = "Maximum amount cannot be negative")
        BigDecimal maxAmount,
        LocalDate startDate,
        LocalDate endDate) {

    @AssertTrue(message = "Start date must be on or before end date")
    public boolean isDateRangeValid() {
        return startDate == null || endDate == null || !startDate.isAfter(endDate);
    }

    @AssertTrue(message = "Minimum amount must not exceed maximum amount")
    public boolean isAmountRangeValid() {
        return minAmount == null || maxAmount == null || minAmount.compareTo(maxAmount) <= 0;
    }
}
