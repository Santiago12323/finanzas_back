package com.ingresos.back.domain.model.dashboard;

import com.ingresos.back.domain.enums.account.Currency;
import com.ingresos.back.domain.enums.dashboard.Granularity;
import com.ingresos.back.domain.enums.dashboard.PeriodType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class DashboardQuery {
    private String userId;
    private PeriodType period;
    private LocalDate date;
    private LocalDate from;
    private LocalDate to;
    private Currency currency;
    private Granularity granularity;
    private Integer topN;
}