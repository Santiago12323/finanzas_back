package com.ingresos.back.infrastructure.mapper;

import com.ingresos.back.domain.model.BudgetExpense;
import com.ingresos.back.infrastructure.controller.dto.BudgetExpenseDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BudgetExpenseDtoMapper {

    BudgetExpense toDomain(BudgetExpenseDto dto);

    BudgetExpenseDto toDto(BudgetExpense expense);
}