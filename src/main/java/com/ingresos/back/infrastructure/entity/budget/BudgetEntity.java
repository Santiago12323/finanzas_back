package com.ingresos.back.infrastructure.entity.budget;

import com.ingresos.back.domain.enums.account.Currency;
import com.ingresos.back.infrastructure.entity.account.AccountEntity;
import com.ingresos.back.infrastructure.entity.category.CategoryEntity;
import com.ingresos.back.infrastructure.entity.user.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "budgets", indexes = {
        @Index(name = "idx_budgets_user", columnList = "user_id"),
        @Index(name = "idx_budgets_category", columnList = "category_id")
})
@Getter
@Setter
@NoArgsConstructor
public class BudgetEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(length = 500)
    private String note;

    @Column(name = "monthly_limit", nullable = false, precision = 19, scale = 2)
    private BigDecimal monthlyLimit;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Currency currency;

    @Column(name = "alert_threshold")
    private Integer alertThreshold = 80;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private AccountEntity account;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
}