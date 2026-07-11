package com.namandeep.expensetracker.service;

import com.namandeep.expensetracker.dto.DashboardResponse;
import com.namandeep.expensetracker.entity.Budget;
import com.namandeep.expensetracker.entity.User;
import com.namandeep.expensetracker.exception.IncomeNotFoundException;
import com.namandeep.expensetracker.repository.BudgetRepository;
import com.namandeep.expensetracker.repository.ExpenseRepository;
import com.namandeep.expensetracker.repository.IncomeRepository;
import com.namandeep.expensetracker.repository.UserRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Aggregates financial summary and budget usage for the authenticated user's dashboard. */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    private final IncomeRepository incomeRepository;
    private final ExpenseRepository expenseRepository;
    private final BudgetRepository budgetRepository;
    private final UserRepository userRepository;

    @Override
    public DashboardResponse getDashboard(String userEmail) {
        User user = currentUser(userEmail);

        BigDecimal totalIncome = incomeRepository.sumAmountByUserId(user.getId());
        BigDecimal totalExpense = expenseRepository.sumAmountByUserId(user.getId());
        BigDecimal currentBalance = totalIncome.subtract(totalExpense);

        LocalDate today = LocalDate.now();
        int month = today.getMonthValue();
        int year = today.getYear();

        Optional<Budget> budget = budgetRepository.findByUserIdAndMonthAndYear(user.getId(), month, year);
        if (budget.isEmpty()) {
            return DashboardResponse.builder()
                    .totalIncome(totalIncome)
                    .totalExpense(totalExpense)
                    .currentBalance(currentBalance)
                    .monthlyBudget(BigDecimal.ZERO)
                    .remainingBudget(BigDecimal.ZERO)
                    .budgetUsagePercentage(0.0)
                    .build();
        }

        BigDecimal monthlyBudget = budget.get().getMonthlyLimit();
        LocalDate startOfMonth = LocalDate.of(year, month, 1);
        LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());
        BigDecimal monthlyExpense = expenseRepository.sumAmountByUserIdAndExpenseDateBetween(
                user.getId(), startOfMonth, endOfMonth);
        BigDecimal remainingBudget = monthlyBudget.subtract(monthlyExpense);

        return DashboardResponse.builder()
                .totalIncome(totalIncome)
                .totalExpense(totalExpense)
                .currentBalance(currentBalance)
                .monthlyBudget(monthlyBudget)
                .remainingBudget(remainingBudget)
                .budgetUsagePercentage(calculateUsagePercentage(monthlyExpense, monthlyBudget))
                .build();
    }

    private User currentUser(String userEmail) {
        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IncomeNotFoundException("Authenticated user was not found"));
    }

    private Double calculateUsagePercentage(BigDecimal monthlyExpense, BigDecimal monthlyBudget) {
        if (monthlyBudget.compareTo(BigDecimal.ZERO) == 0) {
            return 0.0;
        }
        return monthlyExpense
                .divide(monthlyBudget, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .doubleValue();
    }
}
