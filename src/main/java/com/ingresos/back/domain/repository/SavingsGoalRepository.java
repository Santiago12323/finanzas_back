package com.ingresos.back.domain.repository;

import com.ingresos.back.domain.enums.savings.GoalStatus;
import com.ingresos.back.domain.mapper.SavingsGoalMapper;
import com.ingresos.back.domain.model.SavingsGoal;
import com.ingresos.back.infrastructure.repository.savings.SpringDataSavingsGoalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class SavingsGoalRepository {

    @Autowired
    private SpringDataSavingsGoalRepository jpaRepository;

    @Autowired
    private SavingsGoalMapper savingsGoalMapper;

    public SavingsGoal save(SavingsGoal goal) {
        return savingsGoalMapper.toDomain(jpaRepository.save(savingsGoalMapper.toEntity(goal)));
    }

    public Optional<SavingsGoal> findById(String id) {
        return jpaRepository.findById(id).map(savingsGoalMapper::toDomain);
    }

    public List<SavingsGoal> findByUserId(String userId) {
        return jpaRepository.findByUserId(userId).stream().map(savingsGoalMapper::toDomain).toList();
    }

    public List<SavingsGoal> findByUserIdAndStatus(String userId, GoalStatus status) {
        return jpaRepository.findByUserIdAndStatus(userId, status).stream().map(savingsGoalMapper::toDomain).toList();
    }

    public void deleteById(String id) {
        jpaRepository.deleteById(id);
    }
}