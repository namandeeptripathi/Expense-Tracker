package com.namandeep.expensetracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/** Refresh token used to rotate an authentication session. */
public record RefreshTokenRequest(
        @Schema(description = "Opaque refresh token", format = "password")
        @NotBlank(message = "Refresh token is required")
        String refreshToken) {
}
