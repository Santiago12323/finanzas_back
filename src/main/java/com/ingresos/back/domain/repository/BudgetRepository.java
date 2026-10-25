package com.ingresos.back.domain.repository;

import com.ingresos.back.domain.mapper.BudgetMapper;
import com.ingresos.back.domain.model.Budget;
import com.ingresos.back.infrastructure.repository.budget.SpringDataBudgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class BudgetRepository {

    @Autowired
    private SpringDataBudgetRepository jpaRepository;

    @Autowired
    private BudgetMapper budgetMapper;

    public Budget save(Budget budget) {
        return budgetMapper.toDomain(jpaRepository.save(budgetMapper.toEntity(budget)));
    }

    public Optional<Budget> findById(String id) {
        return jpaRepository.findById(id).map(budgetMapper::toDomain);
    }

    public List<Budget> findByUserId(String userId) {
        return jpaRepository.findByUserId(userId).stream().map(budgetMapper::toDomain).toList();
    }

    public boolean existsByCategoryId(String categoryId) {
        return jpaRepository.existsByCategoryId(categoryId);
    }

    public void deleteById(String id) {
        jpaRepository.deleteById(id);
    }
}