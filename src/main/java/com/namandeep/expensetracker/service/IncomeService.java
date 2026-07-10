package com.namandeep.expensetracker.service;

import com.namandeep.expensetracker.dto.CreateIncomeRequest;
import com.namandeep.expensetracker.dto.IncomeFilter;
import com.namandeep.expensetracker.dto.IncomeResponse;
import com.namandeep.expensetracker.dto.PageResponse;
import com.namandeep.expensetracker.dto.UpdateIncomeRequest;
import org.springframework.data.domain.Pageable;

/** Contract for user-owned income management operations. */
public interface IncomeService {

    IncomeResponse create(String userEmail, CreateIncomeRequest request);

    IncomeResponse update(String userEmail, Long incomeId, UpdateIncomeRequest request);

    void delete(String userEmail, Long incomeId);

    IncomeResponse getById(String userEmail, Long incomeId);

    PageResponse<IncomeResponse> getAll(String userEmail, IncomeFilter filter, Pageable pageable);
}
