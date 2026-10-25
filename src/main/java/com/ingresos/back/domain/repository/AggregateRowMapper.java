package com.ingresos.back.domain.repository;

import com.ingresos.back.domain.model.dashboard.CategoryAmount;
import com.ingresos.back.domain.model.dashboard.DailyAmount;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

final class AggregateRowMapper {

    private AggregateRowMapper() {
    }

    static List<DailyAmount> toDaily(List<Object[]> rows) {
        return rows.stream()
                .map(r -> new DailyAmount(
                        LocalDate.of(((Number) r[0]).intValue(), ((Number) r[1]).intValue(), ((Number) r[2]).intValue()),
                        (BigDecimal) r[3]))
                .toList();
    }

    static List<CategoryAmount> toCategory(List<Object[]> rows) {
        return rows.stream()
                .map(r -> new CategoryAmount((String) r[0], (String) r[1], (String) r[2], (String) r[3], (BigDecimal) r[4]))
                .toList();
    }
}