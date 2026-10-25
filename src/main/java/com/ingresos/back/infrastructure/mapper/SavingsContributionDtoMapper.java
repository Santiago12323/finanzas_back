package com.ingresos.back.infrastructure.mapper;

import com.ingresos.back.domain.model.SavingsContribution;
import com.ingresos.back.infrastructure.controller.dto.SavingsContributionDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SavingsContributionDtoMapper {
    SavingsContributionDto toDto(SavingsContribution contribution);
    SavingsContribution toDomain(SavingsContributionDto dto);
}