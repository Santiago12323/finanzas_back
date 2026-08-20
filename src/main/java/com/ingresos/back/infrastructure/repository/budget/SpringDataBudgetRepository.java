package com.ingresos.back.infrastructure.repository.budget;

import com.ingresos.back.infrastructure.entity.budget.BudgetEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataBudgetRepository extends JpaRepository<BudgetEntity, String> {

    List<BudgetEntity> findByUserId(String userId);

}