package com.ingresos.back.infrastructure.mapper;

import com.ingresos.back.domain.model.Subscription;
import com.ingresos.back.domain.model.User;
import com.ingresos.back.domain.valueobjects.Balance;
import com.ingresos.back.infrastructure.controller.dto.suscription.SubscriptionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface SubscriptionDtoMapper {

    @Mapping(target = "user", source = "userId", qualifiedByName = "mapUserSub")
    @Mapping(target = "amount", source = "dto", qualifiedByName = "mapBalanceFromDto")
    Subscription toDomain(SubscriptionDto dto);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "amount", source = "amount.amount")
    @Mapping(target = "currency", source = "amount.currency")
    SubscriptionDto toDto(Subscription subscription);

    @Named("mapUserSub")
    default User mapUserSub(String userId) {
        if (userId == null) return null;
        User user = new User();
        user.setId(userId);
        return user;
    }

    @Named("mapBalanceFromDto")
    default Balance mapBalanceFromDto(SubscriptionDto dto) {
        if (dto.getAmount() == null) return null;
        return new Balance(dto.getAmount(), dto.getCurrency());
    }
}