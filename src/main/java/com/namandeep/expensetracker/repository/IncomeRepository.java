package com.namandeep.expensetracker.repository;

import com.namandeep.expensetracker.entity.Income;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/** Database access for user-owned income records. */
public interface IncomeRepository extends JpaRepository<Income, Long>, JpaSpecificationExecutor<Income> {

    @Override
    @EntityGraph(attributePaths = "user")
    Page<Income> findAll(Specification<Income> specification, Pageable pageable);

    @EntityGraph(attributePaths = "user")
    Optional<Income> findByIdAndUserId(Long id, Long userId);
}
