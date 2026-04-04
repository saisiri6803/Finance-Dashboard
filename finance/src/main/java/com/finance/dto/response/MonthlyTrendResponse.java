package com.finance.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyTrendResponse {
    private Integer year;
    private Integer month;
    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private BigDecimal net;
}