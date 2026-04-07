package com.finance.dto.response;

import java.math.BigDecimal;

/**
 * Category Total Response DTO
 * 
 * Used for category-wise breakdown of income/expenses
 */
public class CategoryTotalResponse {

    private String category;
    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private BigDecimal netAmount;

    // Default constructor
    public CategoryTotalResponse() {
    }

    // Constructor
    public CategoryTotalResponse(String category, BigDecimal totalIncome,
            BigDecimal totalExpense) {
        this.category = category;
        this.totalIncome = totalIncome != null ? totalIncome : BigDecimal.ZERO;
        this.totalExpense = totalExpense != null ? totalExpense : BigDecimal.ZERO;
        this.netAmount = this.totalIncome.subtract(this.totalExpense);
    }

    // Getters and Setters
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(BigDecimal totalIncome) {
        this.totalIncome = totalIncome;
    }

    public BigDecimal getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(BigDecimal totalExpense) {
        this.totalExpense = totalExpense;
    }

    public BigDecimal getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(BigDecimal netAmount) {
        this.netAmount = netAmount;
    }
}
