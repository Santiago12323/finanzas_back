package com.ingresos.back.aplication.usecase;

import com.ingresos.back.domain.enums.account.Currency;
import com.ingresos.back.domain.exception.NetWorthException;
import com.ingresos.back.domain.port.ExpenseService;
import com.ingresos.back.domain.port.IncomeService;
import com.ingresos.back.domain.port.NetWorthService;
import com.ingresos.back.infrastructure.controller.dto.NetWorthDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

@Service
public class NetWorthServiceImpl implements NetWorthService {

    @Autowired
    private IncomeService incomeService;

    @Autowired
    private ExpenseService expenseService;

    @Override
    @Transactional(readOnly = true)
    public NetWorthDto calculateNetWorth(String userId, LocalDate from, LocalDate to) {
        if (from != null && to != null && from.isAfter(to)) {
            throw new NetWorthException("La fecha 'from' no puede ser posterior a 'to'");
        }

        LocalDateTime start = from != null ? from.atStartOfDay() : null;
        LocalDateTime end = to != null ? to.plusDays(1).atStartOfDay() : null;

        Map<Currency, BigDecimal> incomes = new EnumMap<>(Currency.class);
        incomeService.findByUserId(userId).stream()
                .filter(i -> inRange(i.getIncomeDate(), start, end))
                .forEach(i -> incomes.merge(i.getCurrency(), i.getAmount(), BigDecimal::add));

        Map<Currency, BigDecimal> expenses = new EnumMap<>(Currency.class);
        expenseService.findByUserId(userId).stream()
                .filter(e -> inRange(e.getExpenseDate(), start, end))
                .forEach(e -> expenses.merge(e.getCurrency(), e.getAmount(), BigDecimal::add));

        Set<Currency> currencies = EnumSet.noneOf(Currency.class);
        currencies.addAll(incomes.keySet());
        currencies.addAll(expenses.keySet());

        Map<Currency, BigDecimal> netWorth = new EnumMap<>(Currency.class);
        for (Currency currency : currencies) {
            netWorth.put(currency,
                    incomes.getOrDefault(currency, BigDecimal.ZERO)
                            .subtract(expenses.getOrDefault(currency, BigDecimal.ZERO)));
        }

        return new NetWorthDto(userId, from, to, incomes, expenses, netWorth);
    }

    private boolean inRange(LocalDateTime date, LocalDateTime start, LocalDateTime end) {
        return (start == null || !date.isBefore(start))
                && (end == null || date.isBefore(end));
    }
}
