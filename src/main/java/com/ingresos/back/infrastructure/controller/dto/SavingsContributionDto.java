package com.ingresos.back.infrastructure.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "SavingsContributionDto", description = "DTO para un aporte a una meta de ahorro")
public class SavingsContributionDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "ID único del aporte (generado automáticamente)", example = "9a8b7c6d-5e4f-3210-abcd-ef0123456789", accessMode = Schema.AccessMode.READ_ONLY)
    private String id;

    @Schema(description = "Monto del aporte", example = "800000.00", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal amount;

    @Schema(description = "Fecha del aporte (si no se envía, se usa la fecha actual)", example = "2026-10-24", nullable = true)
    private LocalDate contributionDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "ID de la meta a la que pertenece el aporte", accessMode = Schema.AccessMode.READ_ONLY)
    private String goalId;
}