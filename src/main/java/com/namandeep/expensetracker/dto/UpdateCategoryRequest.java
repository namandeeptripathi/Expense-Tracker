package com.namandeep.expensetracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/** Full replacement input for an existing user-owned category. */
public record UpdateCategoryRequest(
        @NotBlank(message = "Name is required")
        @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
        String name,

        @Size(max = 500, message = "Description must not exceed 500 characters")
        String description,

        @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "Color must be a six-digit hex value, for example #16A34A")
        String color,

        @Size(max = 64, message = "Icon must not exceed 64 characters")
        String icon) {
}
