package com.finance.dto.response;

import java.math.BigDecimal;

/**
 * Dashboard Summary Response DTO
 * 
 * Contains overall financial summary data:
 * - Total income
 * - Total expenses
 * - Net balance (income - expenses)
 * - Record counts
 */
public class DashboardSummaryResponse {

    private BigDecimal totalIncome;
    private BigDecimal totalExpenses;
    private BigDecimal netBalance;
    private long totalRecords;
    private long incomeCount;
    private long expenseCount;

    // Default constructor
    public DashboardSummaryResponse() {
    }

    // Constructor
    public DashboardSummaryResponse(BigDecimal totalIncome, BigDecimal totalExpenses,
            BigDecimal netBalance, long totalRecords,
            long incomeCount, long expenseCount) {
        this.totalIncome = totalIncome;
        this.totalExpenses = totalExpenses;
        this.netBalance = netBalance;
        this.totalRecords = totalRecords;
        this.incomeCount = incomeCount;
        this.expenseCount = expenseCount;
    }

    // Getters and Setters
    public BigDecimal getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(BigDecimal totalIncome) {
        this.totalIncome = totalIncome;
    }

    public BigDecimal getTotalExpenses() {
        return totalExpenses;
    }

    public void setTotalExpenses(BigDecimal totalExpenses) {
        this.totalExpenses = totalExpenses;
    }

    public BigDecimal getNetBalance() {
        return netBalance;
    }

    public void setNetBalance(BigDecimal netBalance) {
        this.netBalance = netBalance;
    }

    public long getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(long totalRecords) {
        this.totalRecords = totalRecords;
    }

    public long getIncomeCount() {
        return incomeCount;
    }

    public void setIncomeCount(long incomeCount) {
        this.incomeCount = incomeCount;
    }

    public long getExpenseCount() {
        return expenseCount;
    }

    public void setExpenseCount(long expenseCount) {
        this.expenseCount = expenseCount;
    }
}
