package com.namandeep.expensetracker.dto;

import com.namandeep.expensetracker.entity.IncomeType;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

/** Income representation returned by the API. */
public record IncomeResponse(
        Long id,
        String source,
        BigDecimal amount,
        String description,
        LocalDate incomeDate,
        IncomeType incomeType,
        Instant createdAt,
        Instant updatedAt,
        Long userId) {
}
