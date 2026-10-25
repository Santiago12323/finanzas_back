package com.ingresos.back.domain.enums.account;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Tipo de entidad bancaria o financiera")
public enum BankType {
    @Schema(description = "Nequi")
    NEQUI,
    @Schema(description = "Daviplata")
    DAVIPLATA,
    @Schema(description = "Bancolombia")
    BANCOLOMBIA,
    @Schema(description = "Banco de Bogotá")
    BANCO_DE_BOGOTA,
    @Schema(description = "BBVA Colombia")
    BBVA_COLOMBIA,
    @Schema(description = "Davivienda")
    DAVIVIENDA,
    @Schema(description = "Nubank Colombia")
    NUBANK_COLOMBIA,
    @Schema(description = "RappiPay")
    RAPPIPAY,
    @Schema(description = "Scotiabank Colpatria")
    SCOTIABANK_COLPATRIA,
    @Schema(description = "Banco de Occidente")
    BANCO_DE_OCCIDENTE,
    @Schema(description = "Banco Popular")
    BANCO_POPULAR,
    @Schema(description = "Banco Lulo")
    BANCO_LULO,
    @Schema(description = "Falabella")
    FALABELLA,
    @Schema(description = "Banco PiBank")
    BANCO_PIBANK,
    @Schema(description = "Caja Social")
    CAJA_SOCIAL,
    @Schema(description = "Efectivo en cartera física")
    EFECTIVO_CARTERA_FISICA
}