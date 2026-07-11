package com.namandeep.expensetracker.service;

import com.namandeep.expensetracker.dto.DashboardResponse;

/** Contract for dashboard summary of the authenticated user. */
public interface DashboardService {

    DashboardResponse getDashboard(String userEmail);
}
