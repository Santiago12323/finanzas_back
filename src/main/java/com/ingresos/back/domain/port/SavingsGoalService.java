package com.ingresos.back.domain.port;

import com.ingresos.back.domain.enums.savings.GoalStatus;
import com.ingresos.back.domain.model.SavingsContribution;
import com.ingresos.back.domain.model.SavingsGoal;

import java.util.List;

public interface SavingsGoalService {

    SavingsGoal createGoal(SavingsGoal goal);

    SavingsGoal findById(String id);

    List<SavingsGoal> findByUserId(String userId, GoalStatus status);

    SavingsGoal updateGoal(String id, SavingsGoal goal);

    SavingsGoal changeStatus(String id, GoalStatus status);

    void deleteGoal(String id);

    SavingsContribution addContribution(String goalId, SavingsContribution contribution);

    List<SavingsContribution> findContributionsByGoalId(String goalId);

    void deleteContribution(String goalId, String contributionId);
}