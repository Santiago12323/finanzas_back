package com.ingresos.back.infrastructure.entity.account;

import com.ingresos.back.domain.enums.account.AccountType;
import com.ingresos.back.domain.enums.account.BankType;
import com.ingresos.back.domain.enums.account.Currency;
import com.ingresos.back.infrastructure.entity.user.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "accounts")
@Getter
@Setter
public class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 3)
    private Currency currency;

    @Column(name = "last4", nullable = false, length = 4)
    private String last4;

    @Enumerated(EnumType.STRING)
    @Column(name = "bank_type", nullable = false)
    private BankType bankType;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false)
    private AccountType accountType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserEntity user;
}