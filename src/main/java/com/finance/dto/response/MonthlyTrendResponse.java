package com.finance.dto.response;

import java.math.BigDecimal;

/**
 * Monthly Trend Response DTO
 * 
 * Used for monthly income/expense trends
 */
public class MonthlyTrendResponse {

    private int month;
    private int year;
    private String monthName;
    private BigDecimal income;
    private BigDecimal expense;
    private BigDecimal netAmount;

    // Month names for display
    private static final String[] MONTH_NAMES = {
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
    };

    // Default constructor
    public MonthlyTrendResponse() {
    }

    // Constructor
    public MonthlyTrendResponse(int month, int year, BigDecimal income, BigDecimal expense) {
        this.month = month;
        this.year = year;
        this.monthName = month >= 1 && month <= 12 ? MONTH_NAMES[month - 1] : "Unknown";
        this.income = income != null ? income : BigDecimal.ZERO;
        this.expense = expense != null ? expense : BigDecimal.ZERO;
        this.netAmount = this.income.subtract(this.expense);
    }

    // Getters and Setters
    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
        this.monthName = month >= 1 && month <= 12 ? MONTH_NAMES[month - 1] : "Unknown";
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getMonthName() {
        return monthName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }

    public BigDecimal getExpense() {
        return expense;
    }

    public void setExpense(BigDecimal expense) {
        this.expense = expense;
    }

    public BigDecimal getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(BigDecimal netAmount) {
        this.netAmount = netAmount;
    }
}
