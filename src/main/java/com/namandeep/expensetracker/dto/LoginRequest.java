package com.namandeep.expensetracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** Credentials submitted to obtain an access and refresh token pair. */
@Schema(description = "Email and password credentials")
public record LoginRequest(
        @Schema(example = "naman@example.com")
        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        @Size(max = 254, message = "Email must not exceed 254 characters")
        String email,

        @Schema(example = "StrongPassword123!", format = "password")
        @NotBlank(message = "Password is required")
        String password) {
}
