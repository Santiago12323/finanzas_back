package com.ingresos.back.infrastructure.mapper;

import com.ingresos.back.domain.model.SavingsGoal;
import com.ingresos.back.infrastructure.controller.dto.SavingsGoalDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SavingsGoalDtoMapper {
    SavingsGoalDto toDto(SavingsGoal goal);
    SavingsGoal toDomain(SavingsGoalDto dto);
}