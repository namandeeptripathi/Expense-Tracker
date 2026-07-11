package com.namandeep.expensetracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportResponse {

    private BigDecimal totalIncome;

    private BigDecimal totalExpense;

    private BigDecimal netSavings;

    private Integer totalTransactions;

}