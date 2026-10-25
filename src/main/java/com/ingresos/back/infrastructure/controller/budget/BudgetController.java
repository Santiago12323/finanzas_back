package com.ingresos.back.infrastructure.controller.budget;

import com.ingresos.back.domain.model.Budget;
import com.ingresos.back.domain.port.BudgetService;
import com.ingresos.back.infrastructure.controller.dto.BudgetDto;
import com.ingresos.back.infrastructure.mapper.BudgetDtoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/api/budgets")
@Tag(name = "Presupuestos", description = "Operaciones para la gestión de presupuestos mensuales por categoría")
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    @Autowired
    private BudgetDtoMapper budgetDtoMapper;

    @Operation(
            summary = "Crea un nuevo presupuesto",
            description = "Registra un presupuesto mensual para una categoría de gastos. Sin categoría es un presupuesto global. Solo puede haber un presupuesto activo por categoría (o un global) por usuario.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del presupuesto a crear",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BudgetDto.class),
                            examples = @ExampleObject(value = "{\"name\": \"Presupuesto de comida\", \"categoryId\": \"a7b8c9d0-1111-2222-3333-444455556666\", \"monthlyLimit\": {\"amount\": 800000.00, \"currency\": \"COP\"}, \"alertThreshold\": 80, \"userId\": \"f47ac10b-58cc-4372-a567-0e02b2c3d479\"}")
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Presupuesto creado exitosamente",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BudgetDto.class))),
                    @ApiResponse(responseCode = "400", description = "Solicitud inválida o presupuesto duplicado",
                            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"Ya existe un presupuesto activo para esta categoría\"}")))
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping
    public ResponseEntity<BudgetDto> createBudget(@RequestBody BudgetDto budgetDto) {
        Budget created = budgetService.createBudget(budgetDtoMapper.toDomain(budgetDto));
        return new ResponseEntity<>(budgetDtoMapper.toDto(created), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Obtiene un presupuesto por ID",
            description = "Recupera el presupuesto con lo gastado del mes: total gastado, restante, porcentaje y estado. Por defecto es el mes actual.",
            parameters = {
                    @Parameter(name = "id", description = "ID del presupuesto", required = true, example = "e1f2g3h4-i5j6-7890-1234-567890abcdef"),
                    @Parameter(name = "month", description = "Mes a consultar en formato MM/yyyy (opcional, por defecto el mes actual)", example = "10/2026")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Presupuesto encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BudgetDto.class))),
                    @ApiResponse(responseCode = "404", description = "Presupuesto no encontrado",
                            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"Presupuesto no encontrado con ID: e1f2g3h4-i5j6-7890-1234-567890abcdef\"}")))
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/{id}")
    public ResponseEntity<BudgetDto> findById(
            @PathVariable String id,
            @RequestParam(value = "month", required = false) @DateTimeFormat(pattern = "MM/yyyy") YearMonth month) {
        return ResponseEntity.ok(budgetDtoMapper.toDto(budgetService.findById(id, month)));
    }

    @Operation(
            summary = "Obtiene los presupuestos de un usuario",
            description = "Recupera los presupuestos del usuario con lo gastado en el mes consultado. Se puede filtrar por activos o inactivos.",
            parameters = {
                    @Parameter(name = "userId", description = "ID del usuario", required = true, example = "f47ac10b-58cc-4372-a567-0e02b2c3d479"),
                    @Parameter(name = "active", description = "Filtra por presupuestos activos (true) o inactivos (false). Opcional", example = "true"),
                    @Parameter(name = "month", description = "Mes a consultar en formato MM/yyyy (opcional, por defecto el mes actual)", example = "10/2026")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de presupuestos del usuario",
                            content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = BudgetDto.class)))),
                    @ApiResponse(responseCode = "404", description = "Usuario no encontrado",
                            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"Usuario no encontrado\"}")))
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BudgetDto>> findByUserId(
            @PathVariable String userId,
            @RequestParam(value = "active", required = false) Boolean active,
            @RequestParam(value = "month", required = false) @DateTimeFormat(pattern = "MM/yyyy") YearMonth month) {
        return ResponseEntity.ok(budgetService.findByUserId(userId, active, month)
                .stream()
                .map(budgetDtoMapper::toDto)
                .toList());
    }

    @Operation(
            summary = "Actualiza un presupuesto",
            description = "Actualiza nombre, categoría, límite, porcentaje de alerta, cuenta y estado activo. El dueño no se puede cambiar.",
            parameters = @Parameter(name = "id", description = "ID del presupuesto a actualizar", required = true, example = "e1f2g3h4-i5j6-7890-1234-567890abcdef"),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Nuevos datos del presupuesto",
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BudgetDto.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Presupuesto actualizado exitosamente",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BudgetDto.class))),
                    @ApiResponse(responseCode = "400", description = "Solicitud inválida",
                            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"El límite mensual debe ser mayor a cero\"}"))),
                    @ApiResponse(responseCode = "404", description = "Presupuesto no encontrado",
                            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"Presupuesto no encontrado con ID: e1f2g3h4-i5j6-7890-1234-567890abcdef\"}")))
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PutMapping("/{id}")
    public ResponseEntity<BudgetDto> updateBudget(@PathVariable String id, @RequestBody BudgetDto budgetDto) {
        Budget updated = budgetService.updateBudget(id, budgetDtoMapper.toDomain(budgetDto));
        return ResponseEntity.ok(budgetDtoMapper.toDto(updated));
    }

    @Operation(
            summary = "Elimina un presupuesto",
            description = "Elimina el presupuesto. Los gastos no se ven afectados.",
            parameters = @Parameter(name = "id", description = "ID del presupuesto a eliminar", required = true, example = "e1f2g3h4-i5j6-7890-1234-567890abcdef"),
            responses = {
                    @ApiResponse(responseCode = "204", description = "Presupuesto eliminado exitosamente"),
                    @ApiResponse(responseCode = "404", description = "Presupuesto no encontrado",
                            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"Presupuesto no encontrado con ID: e1f2g3h4-i5j6-7890-1234-567890abcdef\"}")))
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBudget(@PathVariable String id) {
        budgetService.deleteBudget(id);
        return ResponseEntity.noContent().build();
    }
}