package com.ingresos.back.infrastructure.entity.savings;

import com.ingresos.back.domain.enums.account.Currency;
import com.ingresos.back.domain.enums.savings.GoalStatus;
import com.ingresos.back.infrastructure.entity.account.AccountEntity;
import com.ingresos.back.infrastructure.entity.user.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "savings_goals", indexes = @Index(name = "idx_goals_user", columnList = "user_id"))
@Getter
@Setter
@NoArgsConstructor
public class SavingsGoalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(name = "target_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal targetAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Currency currency;

    @Column(name = "planned_monthly_amount", precision = 19, scale = 2)
    private BigDecimal plannedMonthlyAmount;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "target_date")
    private LocalDate targetDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GoalStatus status = GoalStatus.ACTIVE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private AccountEntity account;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
}