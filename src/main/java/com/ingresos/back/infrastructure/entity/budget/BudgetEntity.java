package com.ingresos.back.infrastructure.entity.budget;

import com.ingresos.back.domain.enums.account.Currency;
import com.ingresos.back.domain.enums.budget.BudgetCategory;
import com.ingresos.back.infrastructure.entity.user.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "budgets")
@Getter
@Setter
public class BudgetEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    @Enumerated(EnumType.STRING)
    private BudgetCategory category;

    @Column(length = 500)
    private String note;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal monthlyLimit;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal totalSpent = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    @OneToMany(
            mappedBy = "budget",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<BudgetExpenseEntity> expenses;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
}