package com.ingresos.back.aplication.usecase;

import com.ingresos.back.domain.model.Budget;
import com.ingresos.back.domain.model.BudgetExpense;
import com.ingresos.back.domain.model.User;
import com.ingresos.back.domain.port.BudgetService;
import com.ingresos.back.domain.repository.BudgetRepository;
import com.ingresos.back.domain.valueobjects.Balance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BudgetServiceImpl implements BudgetService {

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private UserServiceImpl userService;

    @Override
    public Budget createBudget(Budget budget, String userId) {

        User user = userService.findById(userId);

        budget.setUser(user);

        budget.setExpenses(new ArrayList<>());
        budget.setTotalSpent(new Balance(BigDecimal.ZERO));

        return budgetRepository.createBudget(budget);
    }

    @Override
    public Budget updateBudget(String id, Budget budget, String userId) {
        User user = userService.findById(userId);
        budget.setUser(user);
        return budgetRepository.updateBudget(id, budget);
    }

    @Override
    public Optional<Budget> findById(String id) {
        return budgetRepository.findById(id);
    }

    @Override
    public List<Budget> findByUserId(String userId) {
        return budgetRepository.findByUserId(userId);
    }

    @Override
    public List<Budget> findAll() {
        return budgetRepository.findAll();
    }

    @Override
    public void deleteById(String id) {
        budgetRepository.deleteById(id);
    }

    @Override
    public Budget addExpense(String budgetId, BudgetExpense expense) {

        Budget budget = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new RuntimeException("Budget not found"));
        System.out.println(budget.toString());
        budget.addExpense(expense);

        return budgetRepository.updateBudget(budgetId, budget);
    }

    @Override
    public Budget removeExpense(String budgetId, String expenseId) {
        Budget budget = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new RuntimeException("Budget not found"));

        if (budget.getExpenses() != null) {
            budget.getExpenses().stream()
                    .filter(expense -> expense.getId().equals(expenseId))
                    .findFirst()
                    .ifPresent(expense -> {
                        if (budget.getTotalSpent() != null && expense.getValue() != null) {
                            budget.setTotalSpent(budget.getTotalSpent().subtract(expense.getValue().getAmount()));
                        }
                    });

            budget.getExpenses()
                    .removeIf(expense -> expense.getId().equals(expenseId));
        }

        return budgetRepository.updateBudget(budgetId, budget);
    }

}