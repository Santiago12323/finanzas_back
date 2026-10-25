package com.ingresos.back.infrastructure.controller.dto;

import com.ingresos.back.domain.enums.account.Currency;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "NetWorthDto", description = "DTO que representa el patrimonio neto calculado para un usuario")
public class NetWorthDto {
    @Schema(description = "ID del usuario para el cual se calculó el patrimonio neto", example = "f47ac10b-58cc-4372-a567-0e02b2c3d479", requiredMode = Schema.RequiredMode.REQUIRED)
    private String userId;

    @Schema(description = "Fecha de inicio del periodo de cálculo (DD-MM-YYYY)", example = "01-01-2026", nullable = true)
    private LocalDate from;

    @Schema(description = "Fecha de fin del periodo de cálculo (DD-MM-YYYY)", example = "31-12-2026", nullable = true)
    private LocalDate to;

    @Schema(description = "Mapa de ingresos totales agrupados por moneda", example = "{\"USD\": 5000.00, \"EUR\": 1000.00}", requiredMode = Schema.RequiredMode.REQUIRED)
    private Map<Currency, BigDecimal> totalIncomes;

    @Schema(description = "Mapa de gastos totales agrupados por moneda", example = "{\"USD\": 2000.00, \"EUR\": 500.00}", requiredMode = Schema.RequiredMode.REQUIRED)
    private Map<Currency, BigDecimal> totalExpenses;

    @Schema(description = "Mapa del patrimonio neto (ingresos - gastos) agrupado por moneda", example = "{\"USD\": 3000.00, \"EUR\": 500.00}", requiredMode = Schema.RequiredMode.REQUIRED)
    private Map<Currency, BigDecimal> netWorth;
}
