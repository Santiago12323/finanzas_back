package com.ingresos.back.infrastructure.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ingresos.back.domain.valueobjects.Balance;
import lombok.Data;

@Data
public class BudgetExpenseDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String id;

    private String description;

    private Balance value;
}