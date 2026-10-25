package com.ingresos.back.domain.model;

import com.ingresos.back.domain.enums.account.Currency;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Income {

    private String id;
    private String description;
    private BigDecimal amount;
    private Currency currency;
    private String categoryId;
    private LocalDateTime incomeDate;
    private String accountId;
    private String userId;

    public Income(
            String description,
            BigDecimal amount,
            Currency currency,
            String categoryId,
            LocalDateTime incomeDate,
            String accountId,
            String userId
    ) {
        this.description = description;
        this.amount = amount;
        this.currency = currency;
        this.categoryId = categoryId;
        this.incomeDate = incomeDate;
        this.accountId = accountId;
        this.userId = userId;
    }
}