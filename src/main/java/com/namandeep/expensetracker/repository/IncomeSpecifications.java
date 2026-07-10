package com.namandeep.expensetracker.repository;

import com.namandeep.expensetracker.dto.IncomeFilter;
import com.namandeep.expensetracker.entity.Income;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.springframework.data.jpa.domain.Specification;

/** Builds composable, user-scoped predicates for income queries. */
public final class IncomeSpecifications {

    private IncomeSpecifications() {
    }

    public static Specification<Income> forUserAndFilter(Long userId, IncomeFilter filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("user").get("id"), userId));

            if (hasText(filter.source())) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("source")),
                        "%" + filter.source().trim().toLowerCase(Locale.ROOT) + "%"));
            }
            if (filter.incomeType() != null) {
                predicates.add(criteriaBuilder.equal(root.get("incomeType"), filter.incomeType()));
            }
            if (filter.minAmount() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("amount"), filter.minAmount()));
            }
            if (filter.maxAmount() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("amount"), filter.maxAmount()));
            }
            if (filter.startDate() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("incomeDate"), filter.startDate()));
            }
            if (filter.endDate() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("incomeDate"), filter.endDate()));
            }
            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }

    private static boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
