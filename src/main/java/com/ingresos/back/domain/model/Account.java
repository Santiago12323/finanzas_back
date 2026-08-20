package com.ingresos.back.domain.model;

import com.ingresos.back.domain.enums.account.AccountType;
import com.ingresos.back.domain.enums.account.BankType;
import com.ingresos.back.domain.valueobjects.Balance;
import com.ingresos.back.domain.valueobjects.Last4;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Account {
    private String id;
    private Balance balance;
    private Last4 last4;
    private BankType bankType;
    private AccountType accountType;
    private User user;
}