package com.ingresos.back.domain.model;

import com.ingresos.back.domain.enums.budget.BudgetStatus;
import com.ingresos.back.domain.valueobjects.Balance;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class Budget {
    private String id;
    private String name;
    private String note;
    private String categoryId;
    private Balance monthlyLimit;
    private Integer alertThreshold;
    private Boolean active;
    private String accountId;
    private String userId;
    private String categoryName;
    private Balance totalSpent;
    private BigDecimal remaining;
    private BigDecimal percentUsed;
    private BudgetStatus status;
}