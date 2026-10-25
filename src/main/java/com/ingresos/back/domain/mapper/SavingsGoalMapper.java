package com.ingresos.back.domain.mapper;

import com.ingresos.back.domain.model.SavingsGoal;
import com.ingresos.back.infrastructure.entity.account.AccountEntity;
import com.ingresos.back.infrastructure.entity.savings.SavingsGoalEntity;
import com.ingresos.back.infrastructure.entity.user.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SavingsGoalMapper {

    @Mapping(source = "user.id", target = "userId")
    SavingsGoal toDomain(SavingsGoalEntity entity);

    @Mapping(source = "userId", target = "user")
    SavingsGoalEntity toEntity(SavingsGoal goal);

    default UserEntity toUserRef(String id) {
        if (id == null) return null;
        UserEntity user = new UserEntity();
        user.setId(id);
        return user;
    }

    default AccountEntity toAccountRef(String id) {
        if (id == null) return null;
        AccountEntity account = new AccountEntity();
        account.setId(id);
        return account;
    }
}