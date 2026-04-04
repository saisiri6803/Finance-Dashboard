package com.finance.service;

import com.finance.dto.response.DashboardSummaryResponse;
import com.finance.dto.response.MonthlyTrendResponse;
import com.finance.dto.response.RecordResponse;
import com.finance.model.RecordType;
import com.finance.repository.FinancialRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final FinancialRecordRepository financialRecordRepository;

    public DashboardSummaryResponse getDashboardSummary() {
        BigDecimal totalIncome = toBigDecimal(financialRecordRepository.getTotalByType(RecordType.INCOME));
        BigDecimal totalExpenses = toBigDecimal(financialRecordRepository.getTotalByType(RecordType.EXPENSE));
        BigDecimal netBalance = totalIncome.subtract(totalExpenses);

        Map<String, BigDecimal> incomeByCategory =
                toCategoryMap(financialRecordRepository.getCategoryTotalsByType(RecordType.INCOME));

        Map<String, BigDecimal> expenseByCategory =
                toCategoryMap(financialRecordRepository.getCategoryTotalsByType(RecordType.EXPENSE));

        List<RecordResponse> recentActivity = financialRecordRepository
                .findRecentActivities(PageRequest.of(0, 5))
                .stream()
                .map(RecordResponse::fromEntity)
                .toList();

        List<MonthlyTrendResponse> monthlyTrends = financialRecordRepository
                .getMonthlyTrends()
                .stream()
                .map(row -> {
                    Integer year = ((Number) row[0]).intValue();
                    Integer month = ((Number) row[1]).intValue();
                    BigDecimal income = toBigDecimal(row[2]);
                    BigDecimal expense = toBigDecimal(row[3]);
                    BigDecimal net = income.subtract(expense);

                    return MonthlyTrendResponse.builder()
                            .year(year)
                            .month(month)
                            .totalIncome(income)
                            .totalExpense(expense)
                            .net(net)
                            .build();
                })
                .toList();

        return DashboardSummaryResponse.builder()
                .totalIncome(totalIncome)
                .totalExpenses(totalExpenses)
                .netBalance(netBalance)
                .incomeByCategory(incomeByCategory)
                .expenseByCategory(expenseByCategory)
                .recentActivity(recentActivity)
                .monthlyTrends(monthlyTrends)
                .build();
    }

    private Map<String, BigDecimal> toCategoryMap(List<Object[]> rows) {
        Map<String, BigDecimal> map = new LinkedHashMap<>();
        if (rows == null) {
            return map;
        }

        for (Object[] row : rows) {
            String category = (String) row[0];
            BigDecimal total = toBigDecimal(row[1]);
            map.put(category, total);
        }
        return map;
    }

    private BigDecimal toBigDecimal(Object value) {
        if (value == null) {
            return BigDecimal.ZERO;
        }
        if (value instanceof BigDecimal bd) {
            return bd;
        }
        if (value instanceof Number number) {
            return BigDecimal.valueOf(number.doubleValue());
        }
        return BigDecimal.ZERO;
    }
}