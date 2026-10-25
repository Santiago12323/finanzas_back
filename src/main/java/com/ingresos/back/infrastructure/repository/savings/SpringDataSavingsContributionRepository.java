package com.ingresos.back.infrastructure.repository.savings;

import com.ingresos.back.infrastructure.entity.savings.SavingsContributionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface SpringDataSavingsContributionRepository extends JpaRepository<SavingsContributionEntity, String> {

    List<SavingsContributionEntity> findByGoalIdOrderByContributionDateDesc(String goalId);

    @Query("SELECT COALESCE(SUM(c.amount), 0) FROM SavingsContributionEntity c WHERE c.goal.id = :goalId")
    BigDecimal sumByGoalId(@Param("goalId") String goalId);

    void deleteByGoalId(String goalId);
}