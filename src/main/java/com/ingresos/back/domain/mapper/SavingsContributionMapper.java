package com.ingresos.back.domain.mapper;

import com.ingresos.back.domain.model.SavingsContribution;
import com.ingresos.back.infrastructure.entity.account.AccountEntity;
import com.ingresos.back.infrastructure.entity.savings.SavingsContributionEntity;
import com.ingresos.back.infrastructure.entity.savings.SavingsGoalEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SavingsContributionMapper {

    @Mapping(source = "goal.id", target = "goalId")
    SavingsContribution toDomain(SavingsContributionEntity entity);

    @Mapping(source = "goalId", target = "goal")
    SavingsContributionEntity toEntity(SavingsContribution contribution);

    default SavingsGoalEntity toGoalRef(String id) {
        if (id == null) return null;
        SavingsGoalEntity goal = new SavingsGoalEntity();
        goal.setId(id);
        return goal;
    }

    default AccountEntity toAccountRef(String id) {
        if (id == null) return null;
        AccountEntity account = new AccountEntity();
        account.setId(id);
        return account;
    }
}