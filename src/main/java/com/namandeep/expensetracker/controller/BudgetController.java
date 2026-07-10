package com.namandeep.expensetracker.controller;

import com.namandeep.expensetracker.dto.BudgetResponse;
import com.namandeep.expensetracker.dto.CreateBudgetRequest;
import com.namandeep.expensetracker.dto.UpdateBudgetRequest;
import com.namandeep.expensetracker.service.BudgetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/budgets")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class BudgetController {

    private final BudgetService budgetService;

    @PostMapping
    @Operation(summary = "Create Budget")
    public ResponseEntity<BudgetResponse> create(
            @Valid @RequestBody CreateBudgetRequest request,
            Authentication authentication) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(budgetService.create(authentication.getName(), request));
    }

    @PutMapping("/{budgetId}")
    @Operation(summary = "Update Budget")
    public BudgetResponse update(
            @PathVariable Long budgetId,
            @Valid @RequestBody UpdateBudgetRequest request,
            Authentication authentication) {

        return budgetService.update(authentication.getName(), budgetId, request);
    }

    @DeleteMapping("/{budgetId}")
    @Operation(summary = "Delete Budget")
    public ResponseEntity<Void> delete(
            @PathVariable Long budgetId,
            Authentication authentication) {

        budgetService.delete(authentication.getName(), budgetId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{budgetId}")
    @Operation(summary = "Get Budget By Id")
    public BudgetResponse getById(
            @PathVariable Long budgetId,
            Authentication authentication) {

        return budgetService.getById(authentication.getName(), budgetId);
    }
}