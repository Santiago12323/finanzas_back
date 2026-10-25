package com.ingresos.back.domain.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class SavingsContribution {
    private String id;
    private BigDecimal amount;
    private LocalDate contributionDate;
    private String goalId;
}