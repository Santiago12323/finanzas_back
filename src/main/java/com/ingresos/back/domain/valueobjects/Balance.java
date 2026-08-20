package com.ingresos.back.domain.valueobjects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ingresos.back.domain.enums.account.Currency;

import java.math.BigDecimal;
import java.util.Objects;

public final class Balance {

    private final BigDecimal amount;
    private final Currency currency;

    public Balance(BigDecimal amount) {
        this(amount, Currency.COP);
    }

    @JsonCreator
    public Balance(
            @JsonProperty("amount") BigDecimal amount,
            @JsonProperty("currency") Currency currency) {

        if (amount == null) {
            throw new IllegalArgumentException("El balance es obligatorio.");
        }

        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El balance no puede ser negativo.");
        }

        this.amount = amount;
        this.currency = currency == null ? Currency.COP : currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public Balance add(BigDecimal value) {
        return new Balance(amount.add(value), currency);
    }

    public Balance subtract(BigDecimal value) {
        BigDecimal result = amount.subtract(value);

        if (result.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Fondos insuficientes.");
        }

        return new Balance(result, currency);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Balance balance)) return false;
        return amount.compareTo(balance.amount) == 0 &&
                currency == balance.currency;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount.stripTrailingZeros(), currency);
    }
}