package com.ingresos.back.infrastructure.mapper;

import com.ingresos.back.domain.model.Income;
import com.ingresos.back.infrastructure.controller.dto.IncomeDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface IncomeDtoMapper {

    @Mapping(target = "incomeDate", expression = "java(toLocalDateTime(dto.getIncomeDate()))")
    Income toDomain(IncomeDto dto);

    @Mapping(target = "incomeDate", expression = "java(toLocalDate(domain.getIncomeDate()))")
    IncomeDto toDto(Income domain);

    default LocalDateTime toLocalDateTime(LocalDate date) {
        return date == null ? null : date.atStartOfDay();
    }

    default LocalDate toLocalDate(LocalDateTime date) {
        return date == null ? null : date.toLocalDate();
    }
}