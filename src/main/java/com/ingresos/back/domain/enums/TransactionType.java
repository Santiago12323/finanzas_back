package com.ingresos.back.domain.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Tipos de transacciones")
public enum TransactionType {
    @Schema(description = "Transacción de ingreso")
    INCOME,
    @Schema(description = "Transacción de egreso")
    EXPENSE,
    @Schema(description = "Transacción de transferencia")
    TRANSFER
}
