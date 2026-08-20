package com.ingresos.back.domain.mapper;

import com.ingresos.back.domain.model.Subscription;
import com.ingresos.back.domain.valueobjects.Balance;
import com.ingresos.back.infrastructure.entity.suscription.SubscriptionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {UserMapper.class}
)
public interface SubscriptionMapper {

    @Mapping(target = "amount", source = "amount.amount")
    @Mapping(target = "currency", source = "amount.currency")
    SubscriptionEntity toEntity(Subscription domain);

    @Mapping(target = "amount", expression = "java(new Balance(entity.getAmount(), entity.getCurrency()))")
    Subscription toDomain(SubscriptionEntity entity);
}