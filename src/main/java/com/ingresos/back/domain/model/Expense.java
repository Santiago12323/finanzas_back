package com.ingresos.back.domain.model;

import com.ingresos.back.domain.enums.account.Currency;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Expense {

    private String id;
    private String description;
    private BigDecimal amount;
    private Currency currency;
    private String categoryId;
    private LocalDateTime expenseDate;
    private String accountId;
    private String userId;

    public Expense(
            String description,
            BigDecimal amount,
            Currency currency,
            String categoryId,
            LocalDateTime expenseDate,
            String accountId,
            String userId
    ) {
        this.description = description;
        this.amount = amount;
        this.currency = currency;
        this.categoryId = categoryId;
        this.expenseDate = expenseDate;
        this.accountId = accountId;
        this.userId = userId;
    }
}