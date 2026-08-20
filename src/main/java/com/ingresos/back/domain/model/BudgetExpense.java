package com.ingresos.back.domain.model;

import com.ingresos.back.domain.valueobjects.Balance;
import lombok.Data;


@Data
public class BudgetExpense {

    private String id;

    private String description;

    private Balance value;

}