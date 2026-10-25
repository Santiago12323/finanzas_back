
package com.ingresos.back.infrastructure.mapper;

import com.ingresos.back.domain.model.Expense;
import com.ingresos.back.infrastructure.controller.dto.ExpenseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ExpenseDtoMapper {

    @Mapping(
            target = "expenseDate",
            expression = "java(toLocalDateTime(dto.getExpenseDate()))"
    )
    Expense toDomain(ExpenseDto dto);

    @Mapping(
            target = "expenseDate",
            expression = "java(toLocalDate(domain.getExpenseDate()))"
    )
    ExpenseDto toDto(Expense domain);

    default LocalDateTime toLocalDateTime(LocalDate date) {
        return date == null ? null : date.atStartOfDay();
    }

    default LocalDate toLocalDate(LocalDateTime date) {
        return date == null ? null : date.toLocalDate();
    }
}
