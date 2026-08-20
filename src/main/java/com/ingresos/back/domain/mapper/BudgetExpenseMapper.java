package com.ingresos.back.domain.mapper;

import com.ingresos.back.domain.model.BudgetExpense;
import com.ingresos.back.domain.valueobjects.Balance;
import com.ingresos.back.infrastructure.entity.budget.BudgetExpenseEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BudgetExpenseMapper {

    @Mapping(target = "amount", source = "value.amount")
    @Mapping(target = "budget", ignore = true)
    BudgetExpenseEntity toEntity(BudgetExpense domain);

    @Mapping(
            target = "value",
            expression = "java(new Balance(entity.getAmount()))"
    )
    BudgetExpense toDomain(BudgetExpenseEntity entity);
}