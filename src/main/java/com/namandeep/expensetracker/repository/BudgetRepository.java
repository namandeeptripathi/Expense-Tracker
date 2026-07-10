package com.namandeep.expensetracker.repository;

import com.namandeep.expensetracker.entity.Budget;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BudgetRepository extends JpaRepository<Budget, Long> {

    Optional<Budget> findByIdAndUserId(Long budgetId, Long userId);

    Optional<Budget> findByUserIdAndMonthAndYear(Long userId, Integer month, Integer year);

    boolean existsByUserIdAndMonthAndYear(Long userId, Integer month, Integer year);
}