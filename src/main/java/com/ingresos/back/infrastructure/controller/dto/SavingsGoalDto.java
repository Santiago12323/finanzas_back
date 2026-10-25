package com.ingresos.back.infrastructure.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ingresos.back.domain.enums.account.Currency;
import com.ingresos.back.domain.enums.savings.GoalStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "SavingsGoalDto", description = "DTO para la representación de una meta de ahorro")
public class SavingsGoalDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "ID único de la meta (generado automáticamente)", example = "d1e2f3a4-b5c6-7890-1234-567890abcdef", accessMode = Schema.AccessMode.READ_ONLY)
    private String id;

    @Schema(description = "Nombre de la meta", example = "Viaje a Japón", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Schema(description = "Monto objetivo a ahorrar", example = "12000000.00", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal targetAmount;

    @Schema(description = "Moneda de la meta", requiredMode = Schema.RequiredMode.REQUIRED)
    private Currency currency;

    @Schema(description = "Monto que planeas ahorrar cada mes", example = "800000.00", nullable = true)
    private BigDecimal plannedMonthlyAmount;

    @Schema(description = "Fecha de inicio (si no se envía, se usa la fecha actual)", example = "2026-10-24", nullable = true)
    private LocalDate startDate;

    @Schema(description = "Fecha en la que quieres lograr la meta", example = "2027-12-31", nullable = true)
    private LocalDate targetDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Estado de la meta. Cambia con el endpoint de estado o automáticamente al completarse", accessMode = Schema.AccessMode.READ_ONLY)
    private GoalStatus status;

    @Schema(description = "ID del usuario dueño de la meta", example = "f47ac10b-58cc-4372-a567-0e02b2c3d479", requiredMode = Schema.RequiredMode.REQUIRED)
    private String userId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Total ahorrado hasta ahora (calculado)", example = "3200000.00", accessMode = Schema.AccessMode.READ_ONLY)
    private BigDecimal savedAmount;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Monto que falta para lograr la meta (calculado)", example = "8800000.00", accessMode = Schema.AccessMode.READ_ONLY)
    private BigDecimal remainingAmount;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Porcentaje de avance, máximo 100 (calculado)", example = "26.67", accessMode = Schema.AccessMode.READ_ONLY)
    private BigDecimal progressPercent;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Meses que faltan al ritmo del ahorro mensual planeado (calculado)", example = "11", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer monthsLeft;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Ahorro mensual necesario para llegar a la fecha objetivo (calculado)", example = "586666.67", accessMode = Schema.AccessMode.READ_ONLY)
    private BigDecimal requiredMonthlyAmount;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Indica si el ahorro mensual planeado alcanza para llegar a la fecha objetivo (calculado)", accessMode = Schema.AccessMode.READ_ONLY)
    private Boolean onTrack;
}