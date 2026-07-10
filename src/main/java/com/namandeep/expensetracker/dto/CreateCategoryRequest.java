package com.namandeep.expensetracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/** Validated input for creating a category owned by the current user. */
public record CreateCategoryRequest(
        @Schema(example = "Groceries")
        @NotBlank(message = "Name is required")
        @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
        String name,

        @Schema(example = "Food and household shopping")
        @Size(max = 500, message = "Description must not exceed 500 characters")
        String description,

        @Schema(example = "#16A34A")
        @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "Color must be a six-digit hex value, for example #16A34A")
        String color,

        @Schema(example = "shopping-cart")
        @Size(max = 64, message = "Icon must not exceed 64 characters")
        String icon) {
}
