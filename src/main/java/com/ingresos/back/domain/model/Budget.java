package com.ingresos.back.domain.model;

import com.ingresos.back.domain.enums.budget.BudgetCategory;
import com.ingresos.back.domain.valueobjects.Balance;
import lombok.Data;

import java.util.List;

@Data
public class Budget {

    private String id;

    private String name;

    private BudgetCategory category;

    private String note;

    private Balance monthlyLimit;

    private List<BudgetExpense> expenses;

    private Balance totalSpent;

    private User user;

    public void addExpense(BudgetExpense expense) {

        Balance newTotal = totalSpent.add(expense.getValue().getAmount());

        if (newTotal.getAmount().compareTo(monthlyLimit.getAmount()) > 0) {
            throw new IllegalArgumentException("El gasto supera el límite mensual del presupuesto.");
        }

        expenses.add(expense);
        totalSpent = newTotal;
    }

}