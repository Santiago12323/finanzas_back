package com.ingresos.back.infrastructure.entity.budget;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "budget_expenses")
@Getter
@Setter
public class BudgetExpenseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String description;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "budget_id")
    private BudgetEntity budget;
}