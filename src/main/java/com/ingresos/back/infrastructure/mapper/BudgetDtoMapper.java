package com.ingresos.back.infrastructure.mapper;

import com.ingresos.back.domain.model.Budget;
import com.ingresos.back.infrastructure.controller.dto.BudgetDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BudgetDtoMapper {
    BudgetDto toDto(Budget budget);
    Budget toDomain(BudgetDto dto);
}