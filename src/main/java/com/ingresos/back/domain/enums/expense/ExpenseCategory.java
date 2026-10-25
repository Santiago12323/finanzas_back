package com.ingresos.back.domain.enums.expense;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Categorías de gastos")
public enum ExpenseCategory {
    @Schema(description = "Gastos de supermercado")
    GROCERIES,
    @Schema(description = "Gastos de transporte")
    TRANSPORTATION,
    @Schema(description = "Gastos en comida (restaurantes, etc.)")
    FOOD,
    @Schema(description = "Gastos de servicios públicos")
    UTILITIES,
    @Schema(description = "Gastos de cuidado personal")
    PERSONAL_CARE,
    @Schema(description = "Gastos de ocio y entretenimiento")
    LEISURE,
    @Schema(description = "Gastos de salud")
    HEALTH,
    @Schema(description = "Otros gastos")
    OTHER
}
