package com.namandeep.expensetracker.controller;

import com.namandeep.expensetracker.dto.CategoryResponse;
import com.namandeep.expensetracker.dto.CreateCategoryRequest;
import com.namandeep.expensetracker.dto.PageResponse;
import com.namandeep.expensetracker.dto.UpdateCategoryRequest;
import com.namandeep.expensetracker.exception.InvalidCategoryRequestException;
import com.namandeep.expensetracker.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** Authenticated CRUD and search endpoints for the current user's categories. */
@RestController
@Validated
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
@Tag(name = "Categories", description = "User-owned expense categories")
@SecurityRequirement(name = "bearerAuth")
public class CategoryController {

    private static final Set<String> SORTABLE_FIELDS = Set.of("name", "createdAt", "updatedAt");

    private final CategoryService categoryService;

    @PostMapping
    @Operation(summary = "Create a category")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Category created", content = @Content(schema = @Schema(implementation = CategoryResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "409", description = "Category name already exists")
    })
    public ResponseEntity<CategoryResponse> create(
            @Valid @RequestBody CreateCategoryRequest request,
            Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.create(authentication.getName(), request));
    }

    @PutMapping("/{categoryId}")
    @Operation(summary = "Update a category")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category updated", content = @Content(schema = @Schema(implementation = CategoryResponse.class))),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "409", description = "Category name already exists")
    })
    public CategoryResponse update(
            @PathVariable @Min(value = 1, message = "Category ID must be positive") Long categoryId,
            @Valid @RequestBody UpdateCategoryRequest request,
            Authentication authentication) {
        return categoryService.update(authentication.getName(), categoryId, request);
    }

    @DeleteMapping("/{categoryId}")
    @Operation(summary = "Delete a category", description = "Referenced expenses must be reassigned through reassignToCategoryId.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Category deleted"),
            @ApiResponse(responseCode = "404", description = "Category or reassignment target not found"),
            @ApiResponse(responseCode = "409", description = "Category is referenced by expenses")
    })
    public ResponseEntity<Void> delete(
            @PathVariable @Min(value = 1, message = "Category ID must be positive") Long categoryId,
            @RequestParam(required = false) @Min(value = 1, message = "Reassignment category ID must be positive")
            Long reassignToCategoryId,
            Authentication authentication) {
        categoryService.delete(authentication.getName(), categoryId, reassignToCategoryId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{categoryId}")
    @Operation(summary = "Get a category by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category found", content = @Content(schema = @Schema(implementation = CategoryResponse.class))),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    public CategoryResponse getById(
            @PathVariable @Min(value = 1, message = "Category ID must be positive") Long categoryId,
            Authentication authentication) {
        return categoryService.getById(authentication.getName(), categoryId);
    }

    @GetMapping
    @Operation(summary = "List or search categories", description = "Returns paginated categories owned by the current user.")
    @ApiResponse(responseCode = "200", description = "Categories returned")
    public PageResponse<CategoryResponse> getAll(
            @RequestParam(required = false) @Size(max = 50, message = "Search must not exceed 50 characters") String search,
            @RequestParam(defaultValue = "0") @Min(value = 0, message = "Page must not be negative") int page,
            @RequestParam(defaultValue = "20") @Min(value = 1, message = "Size must be at least 1")
            @Max(value = 100, message = "Size must not exceed 100") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "ASC") String direction,
            Authentication authentication) {
        return categoryService.getAll(authentication.getName(), search, PageRequest.of(page, size, sort(sortBy, direction)));
    }

    private Sort sort(String sortBy, String direction) {
        if (!SORTABLE_FIELDS.contains(sortBy)) {
            throw new InvalidCategoryRequestException("Unsupported sort field: " + sortBy);
        }
        try {
            return Sort.by(Sort.Direction.fromString(direction), sortBy);
        } catch (IllegalArgumentException exception) {
            throw new InvalidCategoryRequestException("Sort direction must be ASC or DESC");
        }
    }
}
