package com.namandeep.expensetracker.service;

import com.namandeep.expensetracker.dto.ReportResponse;
import com.namandeep.expensetracker.entity.User;
import com.namandeep.expensetracker.exception.IncomeNotFoundException;
import com.namandeep.expensetracker.repository.ExpenseRepository;
import com.namandeep.expensetracker.repository.IncomeRepository;
import com.namandeep.expensetracker.repository.UserRepository;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Aggregates income and expense totals for the authenticated user's financial report. */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportServiceImpl implements ReportService {

    private final IncomeRepository incomeRepository;
    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;

    @Override
    public ReportResponse generateReport(String userEmail) {
        User user = currentUser(userEmail);

        BigDecimal totalIncome = incomeRepository.sumAmountByUserId(user.getId());
        BigDecimal totalExpense = expenseRepository.sumAmountByUserId(user.getId());
        long incomeCount = incomeRepository.countByUserId(user.getId());
        long expenseCount = expenseRepository.countByUserId(user.getId());

        return ReportResponse.builder()
                .totalIncome(totalIncome)
                .totalExpense(totalExpense)
                .netSavings(totalIncome.subtract(totalExpense))
                .totalTransactions(Math.toIntExact(incomeCount + expenseCount))
                .build();
    }

    private User currentUser(String userEmail) {
        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IncomeNotFoundException("Authenticated user was not found"));
    }
}
