package com.ingresos.back.infrastructure.mapper;

import com.ingresos.back.domain.model.dashboard.Dashboard;
import com.ingresos.back.infrastructure.controller.dto.DashboardDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DashboardDtoMapper {

    DashboardDto toDto(Dashboard dashboard);

    DashboardDto.DistributionDto toDistributionDto(Dashboard.Distribution distribution);
}