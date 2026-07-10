package com.namandeep.expensetracker.security;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/** Configuration for signed access tokens and persisted refresh-token lifetime. */
@Validated
@ConfigurationProperties(prefix = "expense-tracker.security.jwt")
public record JwtProperties(
        @NotBlank(message = "JWT_SECRET must be configured")
        @Size(min = 44, message = "JWT_SECRET must be a Base64-encoded value of at least 32 bytes")
        String secret,
        @NotBlank String issuer,
        @NotNull Duration accessTokenTtl,
        @NotNull Duration refreshTokenTtl) {
}
