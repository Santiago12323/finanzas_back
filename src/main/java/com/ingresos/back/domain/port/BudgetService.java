package com.ingresos.back.domain.port;

import com.ingresos.back.domain.model.Budget;

import java.time.YearMonth;
import java.util.List;

public interface BudgetService {

    Budget createBudget(Budget budget);

    Budget findById(String id, YearMonth month);

    List<Budget> findByUserId(String userId, Boolean active, YearMonth month);

    Budget updateBudget(String id, Budget budget);

    void deleteBudget(String id);
}