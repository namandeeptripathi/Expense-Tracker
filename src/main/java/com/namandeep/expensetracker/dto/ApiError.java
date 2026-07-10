package com.namandeep.expensetracker.dto;

import java.time.Instant;
import java.util.Map;

/** Stable error payload returned by the API. */
public record ApiError(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path,
        Map<String, String> validationErrors) {
}
