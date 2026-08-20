package com.ingresos.back.domain.mapper;

import com.ingresos.back.domain.model.Budget;
import com.ingresos.back.infrastructure.entity.budget.BudgetEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import com.ingresos.back.domain.mapper.BudgetExpenseMapper;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {
                UserMapper.class,
                BudgetExpenseMapper.class
        }
)
public interface BudgetMapper {

    @Mapping(
            target = "monthlyLimit",
            expression = "java(new Balance(entity.getMonthlyLimit(), entity.getCurrency()))"
    )
    @Mapping(
            target = "totalSpent",
            expression = "java(new Balance(entity.getTotalSpent(), entity.getCurrency()))"
    )
    Budget toDomain(BudgetEntity entity);

    @Mapping(
            target = "monthlyLimit",
            source = "monthlyLimit.amount"
    )
    @Mapping(
            target = "totalSpent",
            source = "totalSpent.amount"
    )
    @Mapping(
            target = "currency",
            source = "monthlyLimit.currency"
    )
    BudgetEntity toEntity(Budget domain);
}