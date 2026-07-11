package com.namandeep.expensetracker.service;

import com.namandeep.expensetracker.dto.ReportResponse;

/** Contract for aggregated financial reports owned by the authenticated user. */
public interface ReportService {

    ReportResponse generateReport(String userEmail);
}