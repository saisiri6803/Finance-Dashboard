package com.finance.repository;

import com.finance.model.FinancialRecord;
import com.finance.model.RecordType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface FinancialRecordRepository extends JpaRepository<FinancialRecord, Long> {

    @Query("""
        SELECT r FROM FinancialRecord r
        WHERE (:type IS NULL OR r.type = :type)
          AND (:category IS NULL OR LOWER(r.category) = LOWER(:category))
          AND (:startDate IS NULL OR r.date >= :startDate)
          AND (:endDate IS NULL OR r.date <= :endDate)
        ORDER BY r.date DESC, r.createdAt DESC
    """)
    List<FinancialRecord> findByFilters(
            @Param("type") RecordType type,
            @Param("category") String category,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("SELECT COALESCE(SUM(f.amount), 0) FROM FinancialRecord f WHERE f.type = :type")
    BigDecimal getTotalByType(RecordType type);

    @Query("""
        SELECT r.category, COALESCE(SUM(r.amount), 0)
        FROM FinancialRecord r
        WHERE r.type = :type
        GROUP BY r.category
        ORDER BY SUM(r.amount) DESC
    """)
    List<Object[]> getCategoryTotalsByType(@Param("type") RecordType type);

    @Query("""
        SELECT r
        FROM FinancialRecord r
        ORDER BY r.date DESC, r.createdAt DESC
    """)
    List<FinancialRecord> findRecentActivities(Pageable pageable);

    @Query("""
    SELECT YEAR(r.date), MONTH(r.date),
           SUM(CASE WHEN r.type = com.finance.model.RecordType.INCOME THEN r.amount ELSE 0 END),
           SUM(CASE WHEN r.type = com.finance.model.RecordType.EXPENSE THEN r.amount ELSE 0 END)
    FROM FinancialRecord r
    GROUP BY YEAR(r.date), MONTH(r.date)
    ORDER BY YEAR(r.date) DESC, MONTH(r.date) DESC
""")
    List<Object[]> getMonthlyTrends();
}