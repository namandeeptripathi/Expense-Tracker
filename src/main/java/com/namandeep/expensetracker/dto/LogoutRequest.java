package com.namandeep.expensetracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/** Refresh token to revoke when ending the current session. */
public record LogoutRequest(
        @Schema(description = "Opaque refresh token to revoke", format = "password")
        @NotBlank(message = "Refresh token is required")
        String refreshToken) {
}
