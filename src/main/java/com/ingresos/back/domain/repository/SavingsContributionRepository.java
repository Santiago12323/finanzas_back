package com.ingresos.back.domain.repository;

import com.ingresos.back.domain.mapper.SavingsContributionMapper;
import com.ingresos.back.domain.model.SavingsContribution;
import com.ingresos.back.infrastructure.repository.savings.SpringDataSavingsContributionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public class SavingsContributionRepository {

    @Autowired
    private SpringDataSavingsContributionRepository jpaRepository;

    @Autowired
    private SavingsContributionMapper contributionMapper;

    public SavingsContribution save(SavingsContribution contribution) {
        return contributionMapper.toDomain(jpaRepository.save(contributionMapper.toEntity(contribution)));
    }

    public Optional<SavingsContribution> findById(String id) {
        return jpaRepository.findById(id).map(contributionMapper::toDomain);
    }

    public List<SavingsContribution> findByGoalId(String goalId) {
        return jpaRepository.findByGoalIdOrderByContributionDateDesc(goalId).stream()
                .map(contributionMapper::toDomain).toList();
    }

    public BigDecimal sumByGoalId(String goalId) {
        return jpaRepository.sumByGoalId(goalId);
    }

    public void deleteById(String id) {
        jpaRepository.deleteById(id);
    }

    public void deleteByGoalId(String goalId) {
        jpaRepository.deleteByGoalId(goalId);
    }
}