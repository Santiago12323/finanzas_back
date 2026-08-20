package com.ingresos.back.infrastructure.entity.user;

import com.ingresos.back.infrastructure.entity.account.AccountEntity;
import com.ingresos.back.infrastructure.entity.budget.BudgetEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<AccountEntity> accounts = new ArrayList<>();

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<BudgetEntity> budgets = new ArrayList<>();

    private String password;

}