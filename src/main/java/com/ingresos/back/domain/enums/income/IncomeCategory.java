package com.ingresos.back.domain.enums.income;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Categorías de ingresos")
public enum IncomeCategory {
    @Schema(description = "Salario o sueldo")
    SALARY,
    @Schema(description = "Regalo o donación")
    GIFT,
    @Schema(description = "Otros ingresos")
    OTHER
}
