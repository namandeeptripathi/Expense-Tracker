package com.namandeep.expensetracker.dto;

import java.util.List;

/** Standard paginated response envelope. */
public record PageResponse<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last,
        String sort) {
}
