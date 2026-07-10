package com.namandeep.expensetracker.repository;

import com.namandeep.expensetracker.dto.ExpenseFilter;
import com.namandeep.expensetracker.entity.Expense;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.springframework.data.jpa.domain.Specification;

/** Builds composable, ownership-scoped expense query predicates. */
public final class ExpenseSpecifications {

    private ExpenseSpecifications() {
    }

    public static Specification<Expense> forUserAndFilter(Long userId, ExpenseFilter filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("user").get("id"), userId));

            if (hasText(filter.title())) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("title")),
                        "%" + filter.title().trim().toLowerCase(Locale.ROOT) + "%"));
            }
            if (hasText(filter.category())) {
                predicates.add(criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("category").get("name")),
                        filter.category().trim().toLowerCase(Locale.ROOT)));
            }
            if (filter.paymentMethod() != null) {
                predicates.add(criteriaBuilder.equal(root.get("paymentMethod"), filter.paymentMethod()));
            }
            if (filter.startDate() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("expenseDate"), filter.startDate()));
            }
            if (filter.endDate() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("expenseDate"), filter.endDate()));
            }
            if (filter.minAmount() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("amount"), filter.minAmount()));
            }
            if (filter.maxAmount() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("amount"), filter.maxAmount()));
            }

            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }

    private static boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
