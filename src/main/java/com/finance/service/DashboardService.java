package com.finance.service;

import com.finance.dto.response.CategoryTotalResponse;
import com.finance.dto.response.DashboardSummaryResponse;
import com.finance.dto.response.FinancialRecordResponse;
import com.finance.dto.response.MonthlyTrendResponse;
import com.finance.enums.RecordType;
import com.finance.repository.FinancialRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Dashboard Service - Provides aggregated analytics data
 * 
 * This service handles:
 * - Overall summary (total income, expenses, net balance)
 * - Category-wise breakdowns
 * - Monthly trends
 * - Recent activity
 */
@Service
public class DashboardService {

    @Autowired
    private FinancialRecordRepository recordRepository;

    @Autowired
    private FinancialRecordService recordService;

    /**
     * Get overall dashboard summary
     */
    public DashboardSummaryResponse getSummary() {
        // Get totals using repository methods
        BigDecimal totalIncome = recordRepository.sumByType(RecordType.INCOME);
        BigDecimal totalExpenses = recordRepository.sumByType(RecordType.EXPENSE);

        // Handle null values
        totalIncome = totalIncome != null ? totalIncome : BigDecimal.ZERO;
        totalExpenses = totalExpenses != null ? totalExpenses : BigDecimal.ZERO;

        // Calculate net balance
        BigDecimal netBalance = totalIncome.subtract(totalExpenses);

        // Get counts
        long totalRecords = recordRepository.count();
        long incomeCount = recordRepository.countByType(RecordType.INCOME);
        long expenseCount = recordRepository.countByType(RecordType.EXPENSE);

        return new DashboardSummaryResponse(
                totalIncome,
                totalExpenses,
                netBalance,
                totalRecords,
                incomeCount,
                expenseCount);
    }

    /**
     * Get category-wise totals
     * Groups income and expenses by category
     */
    public List<CategoryTotalResponse> getCategoryTotals() {
        List<Object[]> results = recordRepository.getCategoryTotals();

        // Group results by category
        Map<String, BigDecimal[]> categoryMap = new HashMap<>();

        for (Object[] row : results) {
            String category = (String) row[0];
            RecordType type = (RecordType) row[1];
            BigDecimal amount = (BigDecimal) row[2];

            // Initialize if not exists [income, expense]
            categoryMap.computeIfAbsent(category, k -> new BigDecimal[] { BigDecimal.ZERO, BigDecimal.ZERO });

            if (type == RecordType.INCOME) {
                categoryMap.get(category)[0] = amount;
            } else {
                categoryMap.get(category)[1] = amount;
            }
        }

        // Convert to response objects
        return categoryMap.entrySet().stream()
                .map(entry -> new CategoryTotalResponse(
                        entry.getKey(),
                        entry.getValue()[0], // income
                        entry.getValue()[1] // expense
                ))
                .sorted(Comparator.comparing(CategoryTotalResponse::getCategory))
                .collect(Collectors.toList());
    }

    /**
     * Get monthly trends for the last 12 months
     */
    public List<MonthlyTrendResponse> getMonthlyTrends() {
        // Get data for last 12 months
        LocalDate startDate = LocalDate.now().minusMonths(12);
        List<Object[]> results = recordRepository.getMonthlyTrends(startDate);

        // Group results by month-year
        Map<String, BigDecimal[]> monthlyMap = new LinkedHashMap<>();

        for (Object[] row : results) {
            int month = ((Number) row[0]).intValue();
            int year = ((Number) row[1]).intValue();
            RecordType type = (RecordType) row[2];
            BigDecimal amount = (BigDecimal) row[3];

            String key = year + "-" + String.format("%02d", month);

            monthlyMap.computeIfAbsent(key, k -> new BigDecimal[] { BigDecimal.ZERO, BigDecimal.ZERO });

            if (type == RecordType.INCOME) {
                monthlyMap.get(key)[0] = amount;
            } else {
                monthlyMap.get(key)[1] = amount;
            }
        }

        // Convert to response objects
        return monthlyMap.entrySet().stream()
                .map(entry -> {
                    String[] parts = entry.getKey().split("-");
                    int year = Integer.parseInt(parts[0]);
                    int month = Integer.parseInt(parts[1]);
                    return new MonthlyTrendResponse(
                            month,
                            year,
                            entry.getValue()[0], // income
                            entry.getValue()[1] // expense
                    );
                })
                .collect(Collectors.toList());
    }

    /**
     * Get recent activity (last 10 transactions)
     */
    public List<FinancialRecordResponse> getRecentActivity() {
        return recordService.getRecentRecords();
    }

    /**
     * Get summary for a specific date range
     */
    public DashboardSummaryResponse getSummaryByDateRange(LocalDate startDate, LocalDate endDate) {
        BigDecimal totalIncome = recordRepository.sumByTypeAndDateBetween(
                RecordType.INCOME, startDate, endDate);
        BigDecimal totalExpenses = recordRepository.sumByTypeAndDateBetween(
                RecordType.EXPENSE, startDate, endDate);

        totalIncome = totalIncome != null ? totalIncome : BigDecimal.ZERO;
        totalExpenses = totalExpenses != null ? totalExpenses : BigDecimal.ZERO;

        BigDecimal netBalance = totalIncome.subtract(totalExpenses);

        return new DashboardSummaryResponse(
                totalIncome,
                totalExpenses,
                netBalance,
                0, 0, 0 // Counts not calculated for date range
        );
    }
}
