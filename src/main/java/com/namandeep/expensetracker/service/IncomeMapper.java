package com.namandeep.expensetracker.service;

import com.namandeep.expensetracker.dto.CreateIncomeRequest;
import com.namandeep.expensetracker.dto.IncomeResponse;
import com.namandeep.expensetracker.dto.UpdateIncomeRequest;
import com.namandeep.expensetracker.entity.Income;
import com.namandeep.expensetracker.entity.User;
import org.springframework.stereotype.Component;

/** Maps income API contracts without exposing JPA entities. */
@Component
public class IncomeMapper {

    public Income toEntity(CreateIncomeRequest request, User user) {
        return Income.builder()
                .source(request.source().trim())
                .amount(request.amount())
                .description(normalizeDescription(request.description()))
                .incomeDate(request.incomeDate())
                .incomeType(request.incomeType())
                .user(user)
                .build();
    }

    public void updateEntity(Income income, UpdateIncomeRequest request) {
        income.setSource(request.source().trim());
        income.setAmount(request.amount());
        income.setDescription(normalizeDescription(request.description()));
        income.setIncomeDate(request.incomeDate());
        income.setIncomeType(request.incomeType());
    }

    public IncomeResponse toResponse(Income income) {
        return new IncomeResponse(
                income.getId(),
                income.getSource(),
                income.getAmount(),
                income.getDescription(),
                income.getIncomeDate(),
                income.getIncomeType(),
                income.getCreatedAt(),
                income.getUpdatedAt(),
                income.getUser().getId());
    }

    private String normalizeDescription(String description) {
        return description == null || description.isBlank() ? null : description.trim();
    }
}
