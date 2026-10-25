package com.ingresos.back.infrastructure.controller.networth;

import com.ingresos.back.domain.port.NetWorthService;
import com.ingresos.back.infrastructure.controller.dto.NetWorthDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/net-worth")
@Tag(name = "Patrimonio Neto", description = "Operaciones para el cálculo del patrimonio neto del usuario")
public class NetWorthController {

    @Autowired
    private NetWorthService netWorthService;

    @Operation(
            summary = "Calcula el patrimonio neto de un usuario",
            description = "Calcula el patrimonio neto (activos - pasivos) para un usuario, opcionalmente filtrado por rango de fechas.",
            parameters = {
                    @Parameter(
                            name = "userId",
                            description = "ID del usuario para el cual calcular el patrimonio neto",
                            required = true,
                            example = "f47ac10b-58cc-4372-a567-0e02b2c3d479"
                    ),
                    @Parameter(
                            name = "from",
                            description = "Fecha de inicio para el cálculo (DD-MM-YYYY)",
                            example = "01-01-2026",
                            required = false
                    ),
                    @Parameter(
                            name = "to",
                            description = "Fecha de fin para el cálculo (DD-MM-YYYY)",
                            example = "31-12-2026",
                            required = false
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Patrimonio neto calculado exitosamente",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = NetWorthDto.class),
                                    examples = @ExampleObject(value = "{\"userId\": \"f47ac10b-58cc-4372-a567-0e02b2c3d479\", \"totalIncome\": 5000.00, \"totalExpense\": 2000.00, \"netWorth\": 3000.00, \"currency\": \"USD\"}")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Usuario no encontrado",
                            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"Usuario con ID f47ac10b-58cc-4372-a567-0e02b2c3d479 no encontrado\"}"))
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/user/{userId}")
    public ResponseEntity<NetWorthDto> getNetWorth(
            @PathVariable String userId,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate from,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate to
    ) {
        return ResponseEntity.ok(
                netWorthService.calculateNetWorth(userId, from, to)
        );
    }

}
