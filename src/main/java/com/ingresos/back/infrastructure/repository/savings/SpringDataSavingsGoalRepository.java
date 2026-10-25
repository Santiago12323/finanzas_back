package com.ingresos.back.infrastructure.repository.savings;

import com.ingresos.back.domain.enums.savings.GoalStatus;
import com.ingresos.back.infrastructure.entity.savings.SavingsGoalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataSavingsGoalRepository extends JpaRepository<SavingsGoalEntity, String> {

    List<SavingsGoalEntity> findByUserId(String userId);

    List<SavingsGoalEntity> findByUserIdAndStatus(String userId, GoalStatus status);
}