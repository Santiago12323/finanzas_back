package com.ingresos.back.infrastructure.controller.dto;

import com.ingresos.back.domain.enums.budget.BudgetCategory;
import com.ingresos.back.domain.model.BudgetExpense;
import com.ingresos.back.domain.valueobjects.Balance;
import lombok.Data;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BudgetDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String id;

    private String name;

    private String note;

    private BudgetCategory category;

    private Balance monthlyLimit;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<BudgetExpense> expenses;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Balance totalSpent;

    private String userId;
}