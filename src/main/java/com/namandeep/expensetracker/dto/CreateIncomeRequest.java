package com.namandeep.expensetracker.dto;

import com.namandeep.expensetracker.entity.IncomeType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

/** Validated input used to create an income record. */
public record CreateIncomeRequest(
        @Schema(example = "Acme Corp salary")
        @NotBlank(message = "Source is required")
        @Size(max = 150, message = "Source must not exceed 150 characters")
        String source,

        @Schema(example = "85000.00")
        @NotNull(message = "Amount is required")
        @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
        @Digits(integer = 17, fraction = 2, message = "Amount must have at most 17 integer digits and 2 decimal places")
        BigDecimal amount,

        @Size(max = 1000, message = "Description must not exceed 1000 characters")
        String description,

        @Schema(example = "2026-07-10")
        @NotNull(message = "Income date is required")
        LocalDate incomeDate,

        @NotNull(message = "Income type is required")
        IncomeType incomeType) {
}
