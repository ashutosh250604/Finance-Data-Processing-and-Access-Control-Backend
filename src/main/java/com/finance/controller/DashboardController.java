package com.finance.controller;

import com.finance.dto.response.ApiResponse;
import com.finance.dto.response.CategoryTotalResponse;
import com.finance.dto.response.DashboardSummaryResponse;
import com.finance.dto.response.FinancialRecordResponse;
import com.finance.dto.response.MonthlyTrendResponse;
import com.finance.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@Tag(name = "Dashboard", description = "Analytics and summary endpoints")
@SecurityRequirement(name = "bearerAuth")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/summary")
    @Operation(summary = "Get dashboard summary", description = "Returns total income, expenses, and net balance")
    public ResponseEntity<ApiResponse<DashboardSummaryResponse>> getSummary() {
        DashboardSummaryResponse summary = dashboardService.getSummary();
        return ResponseEntity.ok(
                ApiResponse.success("Dashboard summary retrieved successfully", summary));
    }

    @GetMapping("/category-totals")
    @PreAuthorize("hasAnyRole('ANALYST', 'ADMIN')")
    @Operation(summary = "Get category totals", description = "Returns income/expense totals grouped by category (Analyst/Admin only)")
    public ResponseEntity<ApiResponse<List<CategoryTotalResponse>>> getCategoryTotals() {
        List<CategoryTotalResponse> totals = dashboardService.getCategoryTotals();
        return ResponseEntity.ok(
                ApiResponse.success("Category totals retrieved successfully", totals));
    }

    @GetMapping("/monthly-trends")
    @PreAuthorize("hasAnyRole('ANALYST', 'ADMIN')")
    @Operation(summary = "Get monthly trends", description = "Returns monthly income/expense trends for last 12 months (Analyst/Admin only)")
    public ResponseEntity<ApiResponse<List<MonthlyTrendResponse>>> getMonthlyTrends() {
        List<MonthlyTrendResponse> trends = dashboardService.getMonthlyTrends();
        return ResponseEntity.ok(
                ApiResponse.success("Monthly trends retrieved successfully", trends));
    }

    @GetMapping("/recent-activity")
    @Operation(summary = "Get recent activity", description = "Returns last 10 transactions")
    public ResponseEntity<ApiResponse<List<FinancialRecordResponse>>> getRecentActivity() {
        List<FinancialRecordResponse> activity = dashboardService.getRecentActivity();
        return ResponseEntity.ok(
                ApiResponse.success("Recent activity retrieved successfully", activity));
    }
}
