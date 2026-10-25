package com.ingresos.back.domain.mapper;

import com.ingresos.back.domain.model.Expense;
import com.ingresos.back.infrastructure.entity.expense.ExpenseEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ExpenseMapper {

    @Mapping(source = "accountId", target = "account.id")
    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "categoryId", target = "category.id")
    ExpenseEntity toEntity(Expense domain);

    @Mapping(source = "account.id", target = "accountId")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "category.id", target = "categoryId")
    Expense toDomain(ExpenseEntity entity);
}