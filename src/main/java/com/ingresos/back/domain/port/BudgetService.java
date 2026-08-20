package com.ingresos.back.domain.port;

import com.ingresos.back.domain.model.Budget;
import com.ingresos.back.domain.model.BudgetExpense;

import java.util.List;
import java.util.Optional;

public interface BudgetService {

    Budget createBudget(Budget budget, String userId);

    Budget updateBudget(String id,  Budget budget , String userId);

    Optional<Budget> findById(String id);

    List<Budget> findByUserId(String userId);

    List<Budget> findAll();

    void deleteById(String id);

    Budget addExpense(String budgetId, BudgetExpense expense);

    Budget removeExpense(String budgetId, String expenseId);
}