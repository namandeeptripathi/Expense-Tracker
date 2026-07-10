package com.namandeep.expensetracker.service;

import com.namandeep.expensetracker.dto.BudgetResponse;
import com.namandeep.expensetracker.dto.CreateBudgetRequest;
import com.namandeep.expensetracker.dto.UpdateBudgetRequest;

/** Contract for management of budgets owned by the authenticated user. */
public interface BudgetService {

    BudgetResponse create(String userEmail, CreateBudgetRequest request);

    BudgetResponse update(String userEmail, Long budgetId, UpdateBudgetRequest request);

    void delete(String userEmail, Long budgetId);

    BudgetResponse getById(String userEmail, Long budgetId);

}