package com.ingresos.back.domain.repository;

import com.ingresos.back.domain.model.Budget;
import com.ingresos.back.infrastructure.entity.budget.BudgetEntity;
import com.ingresos.back.domain.mapper.BudgetMapper;
import com.ingresos.back.infrastructure.repository.budget.SpringDataBudgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class BudgetRepository {

    @Autowired
    private SpringDataBudgetRepository springDataBudgetRepository;

    @Autowired
    private BudgetMapper budgetMapper;

    public Budget createBudget(Budget budget) {
        BudgetEntity entity = budgetMapper.toEntity(budget);
        BudgetEntity saved = springDataBudgetRepository.save(entity);
        return budgetMapper.toDomain(saved);
    }

    public Optional<Budget> findById(String id) {
        return springDataBudgetRepository.findById(id)
                .map(budgetMapper::toDomain);
    }

    public List<Budget> findByUserId(String userId) {
        return springDataBudgetRepository.findByUserId(userId).stream()
                .map(budgetMapper::toDomain).toList();
    }

    public List<Budget> findAll() {
        return springDataBudgetRepository.findAll()
                .stream()
                .map(budgetMapper::toDomain)
                .collect(Collectors.toList());
    }

    public Budget updateBudget(String id, Budget budget) {

        BudgetEntity entity = budgetMapper.toEntity(budget);
        entity.setId(id);

        if (entity.getExpenses() != null) {
            entity.getExpenses().forEach(expense ->
                    expense.setBudget(entity)
            );
        }

        BudgetEntity updated = springDataBudgetRepository.save(entity);

        return budgetMapper.toDomain(updated);
    }

    public void deleteById(String id) {
        springDataBudgetRepository.deleteById(id);
    }

}