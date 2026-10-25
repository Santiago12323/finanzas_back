package com.ingresos.back.domain.model;

import com.ingresos.back.domain.enums.account.Currency;
import com.ingresos.back.domain.enums.savings.GoalStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class SavingsGoal {
    private String id;
    private String name;
    private BigDecimal targetAmount;
    private Currency currency;
    private BigDecimal plannedMonthlyAmount;
    private LocalDate startDate;
    private LocalDate targetDate;
    private GoalStatus status;
    private String userId;
    private BigDecimal savedAmount;
    private BigDecimal remainingAmount;
    private BigDecimal progressPercent;
    private Integer monthsLeft;
    private BigDecimal requiredMonthlyAmount;
    private Boolean onTrack;
}