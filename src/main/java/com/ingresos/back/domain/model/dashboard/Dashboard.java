package com.ingresos.back.domain.model.dashboard;

import com.ingresos.back.domain.enums.account.Currency;
import com.ingresos.back.domain.enums.dashboard.Granularity;
import com.ingresos.back.domain.enums.dashboard.MovementType;
import com.ingresos.back.domain.enums.dashboard.PeriodType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Dashboard {

    private String userId;
    private PeriodType period;
    private Currency currency;
    private DateRange range;
    private DateRange previousRange;
    private Summary summary;
    private Cashflow cashflow;
    private Distribution expenseDistribution;
    private Distribution incomeDistribution;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DateRange {
        private LocalDate from;
        private LocalDate to;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Summary {
        private BigDecimal totalIncome;
        private BigDecimal totalExpense;
        private BigDecimal net;
        private BigDecimal savingsRate;
        private BigDecimal avgDailyExpense;
        private BigDecimal projectedExpense;
        private Comparison comparison;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Comparison {
        private BigDecimal previousIncome;
        private BigDecimal previousExpense;
        private BigDecimal previousNet;
        private BigDecimal incomeVariation;
        private BigDecimal expenseVariation;
        private BigDecimal netVariation;
        private BigDecimal savingsRatePoints;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Cashflow {
        private Granularity granularity;
        private List<CashflowBucket> buckets;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CashflowBucket {
        private String key;
        private LocalDate from;
        private LocalDate to;
        private BigDecimal income;
        private BigDecimal expense;
        private BigDecimal net;
        private BigDecimal cumulativeNet;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Distribution {
        private MovementType type;
        private BigDecimal total;
        private int categoriesCount;
        private List<DistributionItem> items;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DistributionItem {
        private String categoryId;
        private String name;
        private String icon;
        private String color;
        private BigDecimal amount;
        private BigDecimal percentOfTotal;
        private BigDecimal percentOfIncome;
        private BigDecimal previousAmount;
        private BigDecimal variationVsPrevious;
        private boolean grouped;
    }
}