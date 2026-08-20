package com.ingresos.back.domain.model;

import com.ingresos.back.domain.valueobjects.Balance;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Subscription {
    private String id;
    private String name;
    private String description;
    private Balance amount;
    private LocalDate nextPaymentDate;
    private Boolean automated;
    private User user;
}