package com.ingresos.back.domain.mapper;

import com.ingresos.back.domain.model.Account;
import com.ingresos.back.infrastructure.entity.account.AccountEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {UserMapper.class}
)
public interface AccountMapper {

    @Mapping(target = "balance", source = "balance.amount")
    @Mapping(target = "currency", source = "balance.currency")
    @Mapping(target = "last4", source = "last4.value")
    AccountEntity toEntity(Account domain);

    @Mapping(target = "balance", expression = "java(new Balance(entity.getBalance(), entity.getCurrency()))")
    @Mapping(target = "last4", expression = "java(new Last4(entity.getLast4()))")
    Account toDomain(AccountEntity entity);
}