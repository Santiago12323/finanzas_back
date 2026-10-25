package com.ingresos.back.domain.port;

import com.ingresos.back.domain.model.Expense;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseService {
    Expense createExpense(Expense expense);

    Expense findById(String id);

    List<Expense> findAllExpenses();

    List<Expense> findByUserId(String userId);

    List<Expense> findByAccountId(String accountId);

    Expense updateExpense(String id, Expense expense);

    void deleteExpense(String id);

    List<Expense> findByUserIdAndDateRange(String userId, LocalDate startDate, LocalDate endDate);

}
