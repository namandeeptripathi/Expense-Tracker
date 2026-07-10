package com.namandeep.expensetracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/** Token pair returned after successful registration, login, or refresh. */
@Schema(description = "JWT access token and opaque refresh token")
public record AuthResponse(
        @Schema(description = "Signed JWT access token")
        String accessToken,
        @Schema(description = "Opaque refresh token; store securely", format = "password")
        String refreshToken,
        @Schema(example = "Bearer")
        String tokenType,
        @Schema(description = "Access-token lifetime in seconds", example = "900")
        long expiresIn) {
}
