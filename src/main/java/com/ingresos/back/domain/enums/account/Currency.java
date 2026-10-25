package com.ingresos.back.domain.enums.account;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Moneda utilizada en la aplicación")
public enum Currency {
    @Schema(description = "Peso colombiano")
    COP,
    @Schema(description = "Dólar estadounidense")
    USD
}