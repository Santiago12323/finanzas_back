package com.ingresos.back.domain.repository;

import com.ingresos.back.domain.enums.account.Currency;
import com.ingresos.back.domain.mapper.IncomeMapper;
import com.ingresos.back.domain.model.Income;
import com.ingresos.back.domain.model.dashboard.CategoryAmount;
import com.ingresos.back.domain.model.dashboard.DailyAmount;
import com.ingresos.back.infrastructure.repository.income.SpringDataIncomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class IncomeRepository{

    @Autowired
    private SpringDataIncomeRepository jpaRepository;

    @Autowired
    private IncomeMapper incomeMapper;

    public Income save(Income income) {
        return incomeMapper.toDomain(jpaRepository.save(incomeMapper.toEntity(income)));
    }

    public Optional<Income> findById(String id) {
        return jpaRepository.findById(id).map(incomeMapper::toDomain);
    }

    public List<Income> findAll() {
        return jpaRepository.findAll().stream().map(incomeMapper::toDomain).toList();
    }

    public List<Income> findByUserId(String userId) {
        return jpaRepository.findByUserId(userId).stream().map(incomeMapper::toDomain).toList();
    }

    public List<Income> findByAccountId(String accountId) {
        return jpaRepository.findByAccountId(accountId).stream().map(incomeMapper::toDomain).toList();
    }

    public void deleteById(String id) {
        jpaRepository.deleteById(id);
    }

    public List<Income> findByUserIdAndIncomeDateBetween(String userId, LocalDateTime start, LocalDateTime end) {
        return jpaRepository.findByUserIdAndIncomeDateBetween(userId, start, end).stream().map(incomeMapper::toDomain).toList();
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
