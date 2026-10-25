package com.ingresos.back.infrastructure.mapper;

import com.ingresos.back.domain.model.Category;
import com.ingresos.back.infrastructure.controller.dto.CategoryDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryDtoMapper {
    CategoryDto toDto(Category category);
    Category toDomain(CategoryDto dto);
}