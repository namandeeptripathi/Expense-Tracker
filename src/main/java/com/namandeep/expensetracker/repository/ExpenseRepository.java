package com.namandeep.expensetracker.repository;

import com.namandeep.expensetracker.entity.Expense;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.domain.Specification;

/** Database access for expenses and their query specifications. */
public interface ExpenseRepository extends JpaRepository<Expense, Long>, JpaSpecificationExecutor<Expense> {

    @Override
    @EntityGraph(attributePaths = {"user", "category"})
    Page<Expense> findAll(Specification<Expense> specification, Pageable pageable);

    @EntityGraph(attributePaths = {"user", "category"})
    Optional<Expense> findByIdAndUserId(Long id, Long userId);
}
