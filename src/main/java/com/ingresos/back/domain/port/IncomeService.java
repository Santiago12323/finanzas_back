package com.ingresos.back.domain.port;

import com.ingresos.back.domain.model.Income;

import java.time.LocalDate;
import java.util.List;

public interface IncomeService {
    Income createIncome(Income income);

    Income findById(String id);

    List<Income> findAllIncomes();

    List<Income> findByUserId(String userId);

    List<Income> findByAccountId(String accountId);

    Income updateIncome(String id, Income income);

    List<Income> findByUserIdAndDateRange(String userId, LocalDate startDate, LocalDate endDate);

    void deleteIncome(String id);
}
