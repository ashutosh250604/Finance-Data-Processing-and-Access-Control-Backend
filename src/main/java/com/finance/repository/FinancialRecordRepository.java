package com.finance.repository;

import com.finance.entity.FinancialRecord;
import com.finance.enums.RecordType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface FinancialRecordRepository extends JpaRepository<FinancialRecord, Long> {

       List<FinancialRecord> findByType(RecordType type);

       List<FinancialRecord> findByCategory(String category);

       List<FinancialRecord> findByDateBetween(LocalDate startDate, LocalDate endDate);

       List<FinancialRecord> findByTypeAndDateBetween(RecordType type, LocalDate startDate, LocalDate endDate);

       List<FinancialRecord> findByCategoryAndDateBetween(String category, LocalDate startDate, LocalDate endDate);

       List<FinancialRecord> findTop10ByOrderByDateDescCreatedAtDesc();

       @Query("SELECT r FROM FinancialRecord r WHERE " +
                      "(:type IS NULL OR r.type = :type) AND " +
                     "(:category IS NULL OR r.category = :category) AND " +
                     "(:startDate IS NULL OR r.date >= :startDate) AND " +
                     "(:endDate IS NULL OR r.date <= :endDate) " +
                     "ORDER BY r.date DESC, r.createdAt DESC")
       Page<FinancialRecord> findWithFilters(
                     @Param("type") RecordType type,
                     @Param("category") String category,
                     @Param("startDate") LocalDate startDate,
                     @Param("endDate") LocalDate endDate,
                     Pageable pageable);

       @Query("SELECT COALESCE(SUM(r.amount), 0) FROM FinancialRecord r WHERE r.type = :type")
       BigDecimal sumByType(@Param("type") RecordType type);

       @Query("SELECT COALESCE(SUM(r.amount), 0) FROM FinancialRecord r " +
                      "WHERE r.type = :type AND r.date BETWEEN :startDate AND :endDate")
       BigDecimal sumByTypeAndDateBetween(
                     @Param("type") RecordType type,
                     @Param("startDate") LocalDate startDate,
                     @Param("endDate") LocalDate endDate);

       @Query("SELECT r.category, r.type, SUM(r.amount) FROM FinancialRecord r " +
                      "GROUP BY r.category, r.type ORDER BY r.category")
       List<Object[]> getCategoryTotals();

       @Query("SELECT FUNCTION('MONTH', r.date), FUNCTION('YEAR', r.date), r.type, SUM(r.amount) " +
                      "FROM FinancialRecord r " +
                     "WHERE r.date >= :startDate " +
                     "GROUP BY FUNCTION('YEAR', r.date), FUNCTION('MONTH', r.date), r.type " +
                     "ORDER BY FUNCTION('YEAR', r.date), FUNCTION('MONTH', r.date)")
       List<Object[]> getMonthlyTrends(@Param("startDate") LocalDate startDate);

       @Query("SELECT DISTINCT r.category FROM FinancialRecord r ORDER BY r.category")
       List<String> findDistinctCategories();

       long countByType(RecordType type);
}
