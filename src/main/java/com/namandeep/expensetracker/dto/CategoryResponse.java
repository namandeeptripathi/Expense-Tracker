package com.namandeep.expensetracker.dto;

import java.time.Instant;

/** Category representation returned by the API. */
public record CategoryResponse(
        Long id,
        String name,
        String description,
        String color,
        String icon,
        Instant createdAt,
        Instant updatedAt,
        Long userId) {
}
