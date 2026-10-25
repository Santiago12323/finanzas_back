package com.ingresos.back.infrastructure.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ingresos.back.domain.enums.account.Currency;
import com.ingresos.back.domain.enums.dashboard.Granularity;
import com.ingresos.back.domain.enums.dashboard.MovementType;
import com.ingresos.back.domain.enums.dashboard.PeriodType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@Schema(name = "DashboardDto", description = "Resumen, flujo y distribución por categoría de un periodo")
public class DashboardDto {

    @Schema(description = "ID del usuario", example = "f47ac10b-58cc-4372-a567-0e02b2c3d479")
    private String userId;

    @Schema(description = "Periodo consultado", example = "MONTH")
    private PeriodType period;

    @Schema(description = "Moneda de todos los montos", example = "COP")
    private Currency currency;

    @Schema(description = "Rango de fechas del periodo consultado")
    private DateRangeDto range;

    @Schema(description = "Rango del periodo inmediatamente anterior, usado para las comparaciones")
    private DateRangeDto previousRange;

    @Schema(description = "Totales, ahorro neto y comparación con el periodo anterior")
    private SummaryDto summary;

    @Schema(description = "Ingresos y gastos agrupados por subperiodo")
    private CashflowDto cashflow;

    @Schema(description = "Distribución del gasto por categoría")
    private DistributionDto expenseDistribution;

    @Schema(description = "Distribución del ingreso por categoría")
    private DistributionDto incomeDistribution;

    @Data
    @NoArgsConstructor
    @Schema(name = "DashboardDateRangeDto")
    public static class DateRangeDto {
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        @Schema(description = "Fecha inicial (dd-MM-yyyy)", example = "01-10-2026")
        private LocalDate from;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        @Schema(description = "Fecha final, inclusive (dd-MM-yyyy)", example = "31-10-2026")
        private LocalDate to;
    }

    @Data
    @NoArgsConstructor
    @Schema(name = "DashboardSummaryDto")
    public static class SummaryDto {
        @Schema(description = "Total de ingresos del periodo", example = "54000000.00")
        private BigDecimal totalIncome;

        @Schema(description = "Total de gastos del periodo", example = "25800000.00")
        private BigDecimal totalExpense;

        @Schema(description = "Ahorro neto: ingresos menos gastos", example = "28200000.00")
        private BigDecimal net;

        @Schema(description = "Tasa de ahorro en %: neto sobre ingresos. Nulo si no hubo ingresos", example = "52.22", nullable = true)
        private BigDecimal savingsRate;

        @Schema(description = "Gasto promedio por día transcurrido", example = "1075000.00")
        private BigDecimal avgDailyExpense;

        @Schema(description = "Gasto proyectado al cierre del periodo al ritmo actual. Solo si el periodo está en curso", nullable = true)
        private BigDecimal projectedExpense;

        @Schema(description = "Comparación con el periodo anterior")
        private ComparisonDto comparison;
    }

    @Data
    @NoArgsConstructor
    @Schema(name = "DashboardComparisonDto")
    public static class ComparisonDto {
        @Schema(description = "Ingresos del periodo anterior")
        private BigDecimal previousIncome;

        @Schema(description = "Gastos del periodo anterior")
        private BigDecimal previousExpense;

        @Schema(description = "Ahorro neto del periodo anterior")
        private BigDecimal previousNet;

        @Schema(description = "Variación % de ingresos. Nulo si el periodo anterior fue 0", nullable = true)
        private BigDecimal incomeVariation;

        @Schema(description = "Variación % de gastos. Nulo si el periodo anterior fue 0", nullable = true)
        private BigDecimal expenseVariation;

        @Schema(description = "Variación % del ahorro neto. Nulo si el periodo anterior fue 0", nullable = true)
        private BigDecimal netVariation;

        @Schema(description = "Cambio de la tasa de ahorro en puntos porcentuales", example = "6.00", nullable = true)
        private BigDecimal savingsRatePoints;
    }

    @Data
    @NoArgsConstructor
    @Schema(name = "DashboardCashflowDto")
    public static class CashflowDto {
        @Schema(description = "Granularidad de los puntos", example = "WEEK")
        private Granularity granularity;

        @Schema(description = "Puntos del flujo, en orden cronológico")
        private List<CashflowBucketDto> buckets;
    }

    @Data
    @NoArgsConstructor
    @Schema(name = "DashboardCashflowBucketDto")
    public static class CashflowBucketDto {
        @Schema(description = "Identificador del punto: día/semana 2026-10-24, mes 2026-10, trimestre 2026-Q4", example = "2026-Q4")
        private String key;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        @Schema(description = "Inicio del punto (dd-MM-yyyy)", example = "01-10-2026")
        private LocalDate from;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        @Schema(description = "Fin del punto (dd-MM-yyyy)", example = "31-12-2026")
        private LocalDate to;

        @Schema(description = "Ingresos del punto")
        private BigDecimal income;

        @Schema(description = "Gastos del punto")
        private BigDecimal expense;

        @Schema(description = "Ingresos menos gastos del punto")
        private BigDecimal net;

        @Schema(description = "Ahorro neto acumulado desde el inicio del periodo hasta este punto")
        private BigDecimal cumulativeNet;
    }

    @Data
    @NoArgsConstructor
    @Schema(name = "DashboardDistributionDto")
    public static class DistributionDto {
        @Schema(description = "EXPENSE (gastos) o INCOME (ingresos)", example = "EXPENSE")
        private MovementType type;

        @Schema(description = "Total gastado o ingresado en el periodo", example = "25800000.00")
        private BigDecimal total;

        @Schema(description = "Cantidad de categorías con movimientos, antes de agrupar con topN", example = "6")
        private int categoriesCount;

        @Schema(description = "Categorías ordenadas de mayor a menor monto")
        private List<DistributionItemDto> items;
    }

    @Data
    @NoArgsConstructor
    @Schema(name = "DashboardDistributionItemDto")
    public static class DistributionItemDto {
        @Schema(description = "ID de la categoría. Nulo en 'Sin categoría' y en el grupo 'Otras categorías'", nullable = true)
        private String categoryId;

        @Schema(description = "Nombre de la categoría", example = "Mercado y Hogar")
        private String name;

        @Schema(description = "Ícono de la categoría", example = "home")
        private String icon;

        @Schema(description = "Color de la categoría en hexadecimal", example = "#D9F36B")
        private String color;

        @Schema(description = "Monto de la categoría en el periodo", example = "7740000.00")
        private BigDecimal amount;

        @Schema(description = "% sobre el total de gastos o de ingresos", example = "30.00")
        private BigDecimal percentOfTotal;

        @Schema(description = "% sobre los ingresos del periodo", example = "14.33", nullable = true)
        private BigDecimal percentOfIncome;

        @Schema(description = "Monto de la categoría en el periodo anterior")
        private BigDecimal previousAmount;

        @Schema(description = "Variación % frente al periodo anterior. Nulo si antes fue 0", nullable = true)
        private BigDecimal variationVsPrevious;

        @Schema(description = "true si agrupa varias categorías (cuando se usa topN)")
        private boolean grouped;
    }
}