package com.ingresos.back.domain.mapper;

import com.ingresos.back.domain.model.Budget;
import com.ingresos.back.domain.valueobjects.Balance;
import com.ingresos.back.infrastructure.entity.account.AccountEntity;
import com.ingresos.back.infrastructure.entity.budget.BudgetEntity;
import com.ingresos.back.infrastructure.entity.category.CategoryEntity;
import com.ingresos.back.infrastructure.entity.user.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, imports = Balance.class)
public interface BudgetMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "account.id", target = "accountId")
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(target = "monthlyLimit", expression = "java(new Balance(entity.getMonthlyLimit(), entity.getCurrency()))")
    Budget toDomain(BudgetEntity entity);

    @Mapping(source = "userId", target = "user")
    @Mapping(source = "accountId", target = "account")
    @Mapping(source = "categoryId", target = "category")
    @Mapping(source = "monthlyLimit.amount", target = "monthlyLimit")
    @Mapping(source = "monthlyLimit.currency", target = "currency")
    BudgetEntity toEntity(Budget budget);

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

    default CategoryEntity toCategoryRef(String id) {
        if (id == null) return null;
        CategoryEntity category = new CategoryEntity();
        category.setId(id);
        return category;
    }
}