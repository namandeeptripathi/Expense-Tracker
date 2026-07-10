package com.namandeep.expensetracker.controller;

import com.namandeep.expensetracker.dto.CreateExpenseRequest;
import com.namandeep.expensetracker.dto.ExpenseFilter;
import com.namandeep.expensetracker.dto.ExpenseResponse;
import com.namandeep.expensetracker.dto.PageResponse;
import com.namandeep.expensetracker.dto.UpdateExpenseRequest;
import com.namandeep.expensetracker.exception.InvalidExpenseQueryException;
import com.namandeep.expensetracker.service.ExpenseService;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** Authenticated CRUD and query endpoints for the current user's expenses. */
@RestController
@Validated
@RequestMapping("/api/v1/expenses")
@RequiredArgsConstructor
@Tag(name = "Expenses", description = "User-owned expense management")
@SecurityRequirement(name = "bearerAuth")
public class ExpenseController {

    private static final Set<String> SORTABLE_FIELDS =
            Set.of("title", "amount", "expenseDate", "createdAt", "updatedAt");

    private final ExpenseService expenseService;

    @PostMapping
    @Operation(summary = "Create an expense")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Expense created", content = @Content(schema = @Schema(implementation = ExpenseResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "401", description = "Not authenticated")
    })
    public ResponseEntity<ExpenseResponse> create(
            @Valid @RequestBody CreateExpenseRequest request,
            Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED).body(expenseService.create(authentication.getName(), request));
    }

    @PutMapping("/{expenseId}")
    @Operation(summary = "Update an expense", description = "Fully replaces an expense owned by the current user.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Expense updated", content = @Content(schema = @Schema(implementation = ExpenseResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "Expense not found")
    })
    public ExpenseResponse update(
            @PathVariable @Min(value = 1, message = "Expense ID must be positive") Long expenseId,
            @Valid @RequestBody UpdateExpenseRequest request,
            Authentication authentication) {
        return expenseService.update(authentication.getName(), expenseId, request);
    }

    @DeleteMapping("/{expenseId}")
    @Operation(summary = "Delete an expense")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Expense deleted"),
            @ApiResponse(responseCode = "404", description = "Expense not found")
    })
    public ResponseEntity<Void> delete(
            @PathVariable @Min(value = 1, message = "Expense ID must be positive") Long expenseId,
            Authentication authentication) {
        expenseService.delete(authentication.getName(), expenseId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{expenseId}")
    @Operation(summary = "Get an expense by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Expense found", content = @Content(schema = @Schema(implementation = ExpenseResponse.class))),
            @ApiResponse(responseCode = "404", description = "Expense not found")
    })
    public ExpenseResponse getById(
            @PathVariable @Min(value = 1, message = "Expense ID must be positive") Long expenseId,
            Authentication authentication) {
        return expenseService.getById(authentication.getName(), expenseId);
    }

    @GetMapping
    @Operation(summary = "List expenses", description = "Returns paginated, sortable expenses for the current user with optional filters.")
    @ApiResponse(responseCode = "200", description = "Expenses returned")
    public PageResponse<ExpenseResponse> getAll(
            @Valid @ModelAttribute ExpenseFilter filter,
            @RequestParam(defaultValue = "0") @Min(value = 0, message = "Page must not be negative") int page,
            @RequestParam(defaultValue = "20") @Min(value = 1, message = "Size must be at least 1")
            @Max(value = 100, message = "Size must not exceed 100") int size,
            @RequestParam(defaultValue = "expenseDate") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction,
            Authentication authentication) {
        return expenseService.getAll(authentication.getName(), filter, PageRequest.of(page, size, sort(sortBy, direction)));
    }

    private Sort sort(String sortBy, String direction) {
        if (!SORTABLE_FIELDS.contains(sortBy)) {
            throw new InvalidExpenseQueryException("Unsupported sort field: " + sortBy);
        }
        try {
            return Sort.by(Sort.Direction.fromString(direction), sortBy);
        } catch (IllegalArgumentException exception) {
            throw new InvalidExpenseQueryException("Sort direction must be ASC or DESC");
        }
    }
}
