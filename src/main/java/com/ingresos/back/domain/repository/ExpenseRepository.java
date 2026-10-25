package com.ingresos.back.domain.repository;

import com.ingresos.back.domain.enums.account.Currency;
import com.ingresos.back.domain.mapper.ExpenseMapper;
import com.ingresos.back.domain.model.Expense;
import com.ingresos.back.domain.model.dashboard.CategoryAmount;
import com.ingresos.back.domain.model.dashboard.DailyAmount;
import com.ingresos.back.infrastructure.repository.expense.SpringDataExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class ExpenseRepository {

    @Autowired
    private SpringDataExpenseRepository jpaRepository;

    @Autowired
    private ExpenseMapper expenseMapper;

    public Expense save(Expense expense) {
        return expenseMapper.toDomain(jpaRepository.save(expenseMapper.toEntity(expense)));
    }

    public Optional<Expense> findById(String id) {
        return jpaRepository.findById(id).map(expenseMapper::toDomain);
    }

    public List<Expense> findAll() {
        return jpaRepository.findAll().stream().map(expenseMapper::toDomain).toList();
    }

    public List<Expense> findByUserId(String userId) {
        return jpaRepository.findByUserId(userId).stream().map(expenseMapper::toDomain).toList();
    }

    public List<Expense> findByAccountId(String accountId) {
        return jpaRepository.findByAccountId(accountId).stream().map(expenseMapper::toDomain).toList();
    }

    public void deleteById(String id) {
        jpaRepository.deleteById(id);
    }

    public List<Expense> findByUserIdAndExpenseDateBetween(String userId, LocalDateTime start, LocalDateTime end) {
        return jpaRepository.findByUserIdAndExpenseDateBetween(userId, start, end).stream().map(expenseMapper::toDomain).toList();
    }

    public boolean existsByCategoryId(String categoryId) {
        return jpaRepository.existsByCategoryId(categoryId);
    }

    public BigDecimal sumByUserAndCategoryBetween(String userId, String categoryId, Currency currency,
                                                  LocalDateTime start, LocalDateTime end) {
        return jpaRepository.sumByUserAndCategoryBetween(userId, categoryId, currency, start, end);
    }

    public BigDecimal sumByUserBetween(String userId, Currency currency, LocalDateTime start, LocalDateTime end) {
        return jpaRepository.sumByUserBetween(userId, currency, start, end);
    }

    public List<DailyAmount> sumGroupedByDay(String userId, Currency currency, LocalDateTime start, LocalDateTime end) {
        return AggregateRowMapper.toDaily(jpaRepository.sumGroupedByDay(userId, currency, start, end));
    }

    public List<CategoryAmount> sumGroupedByCategory(String userId, Currency currency, LocalDateTime start, LocalDateTime end) {
        return AggregateRowMapper.toCategory(jpaRepository.sumGroupedByCategory(userId, currency, start, end));
    }
}
