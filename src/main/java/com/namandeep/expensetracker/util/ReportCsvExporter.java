package com.namandeep.expensetracker.util;

import com.namandeep.expensetracker.dto.ReportResponse;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

/** Builds a CSV representation of a financial report. */
public final class ReportCsvExporter {

    private static final String HEADER = "Total Income,Total Expense,Net Savings,Total Transactions";

    private ReportCsvExporter() {
    }

    public static byte[] toCsv(ReportResponse report) {
        String csv = HEADER + "\n"
                + format(report.getTotalIncome()) + ","
                + format(report.getTotalExpense()) + ","
                + format(report.getNetSavings()) + ","
                + report.getTotalTransactions() + "\n";
        return csv.getBytes(StandardCharsets.UTF_8);
    }

    private static String format(BigDecimal value) {
        return value == null ? "" : value.toPlainString();
    }
}
