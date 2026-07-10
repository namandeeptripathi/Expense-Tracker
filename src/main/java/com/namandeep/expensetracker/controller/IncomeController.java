package com.namandeep.expensetracker.controller;

import com.namandeep.expensetracker.dto.CreateIncomeRequest;
import com.namandeep.expensetracker.dto.IncomeFilter;
import com.namandeep.expensetracker.dto.IncomeResponse;
import com.namandeep.expensetracker.dto.PageResponse;
import com.namandeep.expensetracker.dto.UpdateIncomeRequest;
import com.namandeep.expensetracker.exception.InvalidIncomeQueryException;
import com.namandeep.expensetracker.service.IncomeService;
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

/** Authenticated CRUD and query endpoints for the current user's incomes. */
@RestController
@Validated
@RequestMapping("/api/v1/incomes")
@RequiredArgsConstructor
@Tag(name = "Income", description = "User-owned income management")
@SecurityRequirement(name = "bearerAuth")
public class IncomeController {

    private static final Set<String> SORTABLE_FIELDS =
            Set.of("source", "amount", "incomeDate", "incomeType", "createdAt", "updatedAt");

    private final IncomeService incomeService;

    @PostMapping
    @Operation(summary = "Create an income record")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Income created", content = @Content(schema = @Schema(implementation = IncomeResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "401", description = "Not authenticated")
    })
    public ResponseEntity<IncomeResponse> create(
            @Valid @RequestBody CreateIncomeRequest request,
            Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED).body(incomeService.create(authentication.getName(), request));
    }

    @PutMapping("/{incomeId}")
    @Operation(summary = "Update an income record", description = "Fully replaces an income record owned by the current user.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Income updated", content = @Content(schema = @Schema(implementation = IncomeResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "Income not found")
    })
    public IncomeResponse update(
            @PathVariable @Min(value = 1, message = "Income ID must be positive") Long incomeId,
            @Valid @RequestBody UpdateIncomeRequest request,
            Authentication authentication) {
        return incomeService.update(authentication.getName(), incomeId, request);
    }

    @DeleteMapping("/{incomeId}")
    @Operation(summary = "Delete an income record")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Income deleted"),
            @ApiResponse(responseCode = "404", description = "Income not found")
    })
    public ResponseEntity<Void> delete(
            @PathVariable @Min(value = 1, message = "Income ID must be positive") Long incomeId,
            Authentication authentication) {
        incomeService.delete(authentication.getName(), incomeId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{incomeId}")
    @Operation(summary = "Get an income record by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Income found", content = @Content(schema = @Schema(implementation = IncomeResponse.class))),
            @ApiResponse(responseCode = "404", description = "Income not found")
    })
    public IncomeResponse getById(
            @PathVariable @Min(value = 1, message = "Income ID must be positive") Long incomeId,
            Authentication authentication) {
        return incomeService.getById(authentication.getName(), incomeId);
    }

    @GetMapping
    @Operation(summary = "List income records", description = "Returns paginated, sortable income records for the current user with optional filters.")
    @ApiResponse(responseCode = "200", description = "Income records returned")
    public PageResponse<IncomeResponse> getAll(
            @Valid @ModelAttribute IncomeFilter filter,
            @RequestParam(defaultValue = "0") @Min(value = 0, message = "Page must not be negative") int page,
            @RequestParam(defaultValue = "20") @Min(value = 1, message = "Size must be at least 1")
            @Max(value = 100, message = "Size must not exceed 100") int size,
            @RequestParam(defaultValue = "incomeDate") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction,
            Authentication authentication) {
        return incomeService.getAll(authentication.getName(), filter, PageRequest.of(page, size, sort(sortBy, direction)));
    }

    private Sort sort(String sortBy, String direction) {
        if (!SORTABLE_FIELDS.contains(sortBy)) {
            throw new InvalidIncomeQueryException("Unsupported sort field: " + sortBy);
        }
        try {
            return Sort.by(Sort.Direction.fromString(direction), sortBy);
        } catch (IllegalArgumentException exception) {
            throw new InvalidIncomeQueryException("Sort direction must be ASC or DESC");
        }
    }
}
