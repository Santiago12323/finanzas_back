package com.ingresos.back.infrastructure.mapper;

import com.ingresos.back.domain.model.Account;
import com.ingresos.back.domain.model.User;
import com.ingresos.back.domain.valueobjects.Balance;
import com.ingresos.back.domain.valueobjects.Last4;
import com.ingresos.back.infrastructure.controller.dto.AccountDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface AccountDtoMapper {

    @Mapping(target = "user", source = "userId", qualifiedByName = "mapUser")
    @Mapping(target = "balance", source = "balance", qualifiedByName = "mapBalance")
    @Mapping(target = "last4", source = "last4", qualifiedByName = "mapLast4")
    Account toDomain(AccountDto dto);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "balance", source = "balance.amount")
    @Mapping(target = "last4", source = "last4.value")
    AccountDto toDto(Account account);

    @Named("mapUser")
    default User mapUser(String userId) {
        if (userId == null) return null;
        User user = new User();
        user.setId(userId);
        return user;
    }

    @Named("mapBalance")
    default Balance mapBalance(BigDecimal balance) {
        return balance != null ? new Balance(balance) : null;
    }

    @Named("mapLast4")
    default Last4 mapLast4(String last4) {
        return last4 != null ? new Last4(last4) : null;
    }
}