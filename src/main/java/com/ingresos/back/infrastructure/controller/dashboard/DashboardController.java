package com.ingresos.back.infrastructure.controller.dashboard;

import com.ingresos.back.domain.enums.account.Currency;
import com.ingresos.back.domain.enums.dashboard.Granularity;
import com.ingresos.back.domain.enums.dashboard.MovementType;
import com.ingresos.back.domain.enums.dashboard.PeriodType;
import com.ingresos.back.domain.model.dashboard.DashboardQuery;
import com.ingresos.back.domain.port.DashboardService;
import com.ingresos.back.infrastructure.controller.dto.DashboardDto;
import com.ingresos.back.infrastructure.mapper.DashboardDtoMapper;
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
@RequestMapping("/api/dashboard")
@Tag(name = "Dashboard", description = "Resumen financiero por periodo: ahorro neto, flujo y distribución por categoría")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private DashboardDtoMapper dashboardDtoMapper;

    @Operation(
            summary = "Obtiene el dashboard de un usuario",
            description = "Devuelve en una sola llamada el resumen (ingresos, gastos, ahorro neto, tasa de ahorro y comparación con el periodo anterior), "
                    + "el flujo por subperiodo y la distribución por categoría de gastos e ingresos. "
                    + "El periodo se elige con 'period' (y 'date' para navegar a otros periodos) o con un rango libre usando CUSTOM con 'from' y 'to'. "
                    + "Semana de lunes a domingo; trimestre y año calendario. Todos los montos están en la moneda indicada.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Dashboard del periodo",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = DashboardDto.class),
                                    examples = @ExampleObject(value = """
                                            {
                                              "userId": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
                                              "period": "YEAR",
                                              "currency": "COP",
                                              "range": { "from": "01-01-2026", "to": "31-12-2026" },
                                              "previousRange": { "from": "01-01-2025", "to": "31-12-2025" },
                                              "summary": {
                                                "totalIncome": 54000000.00,
                                                "totalExpense": 25800000.00,
                                                "net": 28200000.00,
                                                "savingsRate": 52.22,
                                                "avgDailyExpense": 87200.00,
                                                "projectedExpense": 31800000.00,
                                                "comparison": {
                                                  "previousIncome": 48000000.00,
                                                  "previousExpense": 26500000.00,
                                                  "previousNet": 21500000.00,
                                                  "incomeVariation": 12.50,
                                                  "expenseVariation": -2.64,
                                                  "netVariation": 31.16,
                                                  "savingsRatePoints": 7.44
                                                }
                                              },
                                              "cashflow": {
                                                "granularity": "QUARTER",
                                                "buckets": [
                                                  { "key": "2026-Q1", "from": "01-01-2026", "to": "31-03-2026", "income": 12000000.00, "expense": 6500000.00, "net": 5500000.00, "cumulativeNet": 5500000.00 },
                                                  { "key": "2026-Q2", "from": "01-04-2026", "to": "30-06-2026", "income": 14000000.00, "expense": 7300000.00, "net": 6700000.00, "cumulativeNet": 12200000.00 },
                                                  { "key": "2026-Q3", "from": "01-07-2026", "to": "30-09-2026", "income": 20000000.00, "expense": 5000000.00, "net": 15000000.00, "cumulativeNet": 27200000.00 },
                                                  { "key": "2026-Q4", "from": "01-10-2026", "to": "31-12-2026", "income": 8000000.00, "expense": 7000000.00, "net": 1000000.00, "cumulativeNet": 28200000.00 }
                                                ]
                                              },
                                              "expenseDistribution": {
                                                "type": "EXPENSE",
                                                "total": 25800000.00,
                                                "categoriesCount": 6,
                                                "items": [
                                                  { "categoryId": "a7b8c9d0-1111-2222-3333-444455556666", "name": "Mercado y Hogar", "icon": "home", "color": "#D9F36B", "amount": 7740000.00, "percentOfTotal": 30.00, "percentOfIncome": 14.33, "previousAmount": 6900000.00, "variationVsPrevious": 12.17, "grouped": false },
                                                  { "categoryId": "b8c9d0e1-2222-3333-4444-555566667777", "name": "Transporte", "icon": "car", "color": "#A5B4FC", "amount": 5160000.00, "percentOfTotal": 20.00, "percentOfIncome": 9.56, "previousAmount": 5300000.00, "variationVsPrevious": -2.64, "grouped": false },
                                                  { "categoryId": null, "name": "Otras categorías", "icon": "ellipsis", "color": "#9CA3AF", "amount": 12900000.00, "percentOfTotal": 50.00, "percentOfIncome": 23.89, "previousAmount": 14300000.00, "variationVsPrevious": -9.79, "grouped": true }
                                                ]
                                              },
                                              "incomeDistribution": {
                                                "type": "INCOME",
                                                "total": 54000000.00,
                                                "categoriesCount": 2,
                                                "items": [
                                                  { "categoryId": "c9d0e1f2-3333-4444-5555-666677778888", "name": "Salario", "icon": "briefcase", "color": "#22C55E", "amount": 48000000.00, "percentOfTotal": 88.89, "percentOfIncome": 88.89, "previousAmount": 44000000.00, "variationVsPrevious": 9.09, "grouped": false },
                                                  { "categoryId": "d0e1f2a3-4444-5555-6666-777788889999", "name": "Freelance", "icon": "laptop", "color": "#10B981", "amount": 6000000.00, "percentOfTotal": 11.11, "percentOfIncome": 11.11, "previousAmount": 4000000.00, "variationVsPrevious": 50.00, "grouped": false }
                                                ]
                                              }
                                            }
                                            """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Parámetros inválidos: moneda ausente, rango personalizado incompleto o invertido, topN menor a 1, demasiados puntos de flujo",
                            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"Para un periodo personalizado, 'from' y 'to' son obligatorios\"}"))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Usuario no encontrado",
                            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"Usuario no encontrado\"}"))
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/user/{userId}")
    public ResponseEntity<DashboardDto> getDashboard(
            @Parameter(description = "ID del usuario", example = "f47ac10b-58cc-4372-a567-0e02b2c3d479")
            @PathVariable String userId,
            @Parameter(description = "Periodo: WEEK, MONTH, QUARTER, YEAR o CUSTOM (por defecto MONTH)", example = "MONTH")
            @RequestParam(value = "period", defaultValue = "MONTH") PeriodType period,
            @Parameter(description = "Cualquier día dentro del periodo, dd/MM/yyyy (por defecto hoy). Sirve para navegar a periodos anteriores", example = "24/10/2026")
            @RequestParam(value = "date", required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate date,
            @Parameter(description = "Fecha mínima del rango, dd/MM/yyyy. Solo con period=CUSTOM", example = "01/09/2026")
            @RequestParam(value = "from", required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate from,
            @Parameter(description = "Fecha máxima del rango, dd/MM/yyyy. Solo con period=CUSTOM", example = "15/10/2026")
            @RequestParam(value = "to", required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate to,
            @Parameter(description = "Moneda de la consulta", example = "COP")
            @RequestParam("currency") Currency currency,
            @Parameter(description = "Granularidad del flujo: DAY, WEEK, MONTH o QUARTER. Si no se envía, se elige según el periodo", example = "MONTH")
            @RequestParam(value = "granularity", required = false) Granularity granularity,
            @Parameter(description = "Cantidad de categorías a mostrar; el resto se agrupa en 'Otras categorías'", example = "5")
            @RequestParam(value = "topN", required = false) Integer topN) {

        DashboardQuery query = buildQuery(userId, period, date, from, to, currency, granularity, topN);
        return ResponseEntity.ok(dashboardDtoMapper.toDto(dashboardService.getDashboard(query)));
    }

    @Operation(
            summary = "Obtiene la distribución por categoría de gastos o ingresos",
            description = "Devuelve únicamente la distribución por categoría del tipo indicado, para el mismo filtro de periodo del dashboard. "
                    + "Úsalo para refrescar el donut o para cambiar entre gastos e ingresos sin recargar todo el dashboard.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Distribución por categoría",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DashboardDto.DistributionDto.class))),
                    @ApiResponse(responseCode = "400", description = "Parámetros inválidos",
                            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"La moneda es obligatoria\"}"))),
                    @ApiResponse(responseCode = "404", description = "Usuario no encontrado",
                            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"Usuario no encontrado\"}")))
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/user/{userId}/distribution")
    public ResponseEntity<DashboardDto.DistributionDto> getDistribution(
            @Parameter(description = "ID del usuario", example = "f47ac10b-58cc-4372-a567-0e02b2c3d479")
            @PathVariable String userId,
            @Parameter(description = "EXPENSE (gastos) o INCOME (ingresos) (por defecto EXPENSE)", example = "EXPENSE")
            @RequestParam(value = "type", defaultValue = "EXPENSE") MovementType type,
            @Parameter(description = "Periodo: WEEK, MONTH, QUARTER, YEAR o CUSTOM (por defecto MONTH)", example = "MONTH")
            @RequestParam(value = "period", defaultValue = "MONTH") PeriodType period,
            @Parameter(description = "Cualquier día dentro del periodo, dd/MM/yyyy (por defecto hoy)", example = "24/10/2026")
            @RequestParam(value = "date", required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate date,
            @Parameter(description = "Fecha mínima del rango, dd/MM/yyyy. Solo con period=CUSTOM", example = "01/09/2026")
            @RequestParam(value = "from", required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate from,
            @Parameter(description = "Fecha máxima del rango, dd/MM/yyyy. Solo con period=CUSTOM", example = "15/10/2026")
            @RequestParam(value = "to", required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate to,
            @Parameter(description = "Moneda de la consulta", example = "COP")
            @RequestParam("currency") Currency currency,
            @Parameter(description = "Cantidad de categorías a mostrar; el resto se agrupa en 'Otras categorías'", example = "5")
            @RequestParam(value = "topN", required = false) Integer topN) {

        DashboardQuery query = buildQuery(userId, period, date, from, to, currency, null, topN);
        return ResponseEntity.ok(dashboardDtoMapper.toDistributionDto(dashboardService.getDistribution(query, type)));
    }

    private DashboardQuery buildQuery(String userId, PeriodType period, LocalDate date, LocalDate from, LocalDate to,
                                      Currency currency, Granularity granularity, Integer topN) {
        return DashboardQuery.builder()
                .userId(userId)
                .period(period)
                .date(date)
                .from(from)
                .to(to)
                .currency(currency)
                .granularity(granularity)
                .topN(topN)
                .build();
    }
}