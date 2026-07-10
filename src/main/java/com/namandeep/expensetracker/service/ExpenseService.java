package com.namandeep.expensetracker.service;

import com.namandeep.expensetracker.dto.CreateExpenseRequest;
import com.namandeep.expensetracker.dto.ExpenseFilter;
import com.namandeep.expensetracker.dto.ExpenseResponse;
import com.namandeep.expensetracker.dto.PageResponse;
import com.namandeep.expensetracker.dto.UpdateExpenseRequest;
import org.springframework.data.domain.Pageable;

/** Contract for user-owned expense management operations. */
public interface ExpenseService {

    ExpenseResponse create(String userEmail, CreateExpenseRequest request);

    ExpenseResponse update(String userEmail, Long expenseId, UpdateExpenseRequest request);

    void delete(String userEmail, Long expenseId);

    ExpenseResponse getById(String userEmail, Long expenseId);

    PageResponse<ExpenseResponse> getAll(String userEmail, ExpenseFilter filter, Pageable pageable);
}
