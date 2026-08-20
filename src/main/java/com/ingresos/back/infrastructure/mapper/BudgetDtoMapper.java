package com.ingresos.back.infrastructure.mapper;

import com.ingresos.back.domain.model.Budget;
import com.ingresos.back.infrastructure.controller.dto.BudgetDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BudgetDtoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    Budget toDomain(BudgetDto dto);

    @Mapping(target = "userId", source = "user.id")
    BudgetDto toDto(Budget budget);
}