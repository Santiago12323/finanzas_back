package com.ingresos.back.aplication.usecase;

import com.ingresos.back.domain.enums.category.CategoryType;
import com.ingresos.back.domain.exception.ExpenseException;
import com.ingresos.back.domain.model.Expense;
import com.ingresos.back.domain.port.AccountService;
import com.ingresos.back.domain.port.CategoryService;
import com.ingresos.back.domain.port.ExpenseService;
import com.ingresos.back.domain.port.UseService;
import com.ingresos.back.domain.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UseService useService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private CategoryService categoryService;

    @Override
    public Expense createExpense(Expense expense) {
        validate(expense);
        return expenseRepository.save(expense);
    }

    @Override
    public Expense findById(String id) {
        return expenseRepository.findById(id)
                .orElseThrow(() -> new ExpenseException("Gasto no encontrado con ID: " + id));
    }

    @Override
    public List<Expense> findAllExpenses() {
        return expenseRepository.findAll();
    }

    @Override
    public List<Expense> findByUserId(String userId) {
        useService.findById(userId);
        return expenseRepository.findByUserId(userId);
    }

    @Override
    public List<Expense> findByAccountId(String accountId) {
        accountService.findById(accountId);
        return expenseRepository.findByAccountId(accountId);
    }

    @Override
    public Expense updateExpense(String id, Expense expense) {
        findById(id);
        validate(expense);
        expense.setId(id);
        return expenseRepository.save(expense);
    }

    @Override
    public void deleteExpense(String id) {
        findById(id);
        expenseRepository.deleteById(id);
    }


    @Override
    public List<Expense> findByUserIdAndDateRange(String userId, LocalDate startDate, LocalDate endDate) {
        useService.findById(userId);
        if (startDate == null || endDate == null) {
            throw new ExpenseException("Las fechas mínima y máxima son obligatorias");
        }
        if (startDate.isAfter(endDate)) {
            throw new ExpenseException("La fecha mínima no puede ser posterior a la fecha máxima");
        }
        return expenseRepository.findByUserIdAndExpenseDateBetween(
                userId, startDate.atStartOfDay(), endDate.atTime(LocalTime.MAX));
    }


    private void validate(Expense expense) {
        if (expense.getAmount() == null || expense.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ExpenseException("El monto del gasto debe ser mayor a cero");
        }
        useService.findById(expense.getUserId());
        accountService.findById(expense.getAccountId());
        if (expense.getCategoryId() != null) {
            categoryService.findByIdForUser(expense.getCategoryId(), expense.getUserId(), CategoryType.EXPENSE);
        }
    }
}
