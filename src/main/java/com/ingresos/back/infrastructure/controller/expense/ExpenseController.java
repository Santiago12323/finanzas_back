package com.ingresos.back.infrastructure.controller.expense;

import com.ingresos.back.domain.model.Expense;
import com.ingresos.back.domain.port.ExpenseService;
import com.ingresos.back.infrastructure.controller.dto.ExpenseDto;
import com.ingresos.back.infrastructure.mapper.ExpenseDtoMapper;
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

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@Tag(name = "Gastos", description = "Operaciones para la gestión de gastos")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private ExpenseDtoMapper expenseDtoMapper;

    @Operation(
            summary = "Crea un nuevo gasto",
            description = "Registra un nuevo gasto en el sistema.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del gasto a crear",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExpenseDto.class),
                            examples = @ExampleObject(value = "{\"description\": \"Alquiler mensual\", \"amount\": 750.00, \"categoryId\": \"housing\", \"accountId\": \"a1b2c3d4-e5f6-7890-1234-567890abcdef\", \"date\": \"2026-10-23T10:00:00Z\"}")
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Gasto creado exitosamente",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExpenseDto.class),
                                    examples = @ExampleObject(value = "{\"id\": \"b1c2d3e4-f5a6-7890-1234-567890abcdef\", \"description\": \"Alquiler mensual\", \"amount\": 750.00, \"categoryId\": \"housing\", \"accountId\": \"a1b2c3d4-e5f6-7890-1234-567890abcdef\", \"date\": \"2026-10-23T10:00:00Z\"}")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Solicitud inválida",
                            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"Campos inválidos\"}"))
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping
    public ResponseEntity<ExpenseDto> createExpense(@RequestBody ExpenseDto expenseDto) {
        Expense expense = expenseDtoMapper.toDomain(expenseDto);
        Expense created = expenseService.createExpense(expense);
        return new ResponseEntity<>(expenseDtoMapper.toDto(created), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Obtiene un gasto por ID",
            description = "Recupera los detalles de un gasto específico utilizando su ID.",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "ID del gasto a buscar",
                            required = true,
                            example = "b1c2d3e4-f5a6-7890-1234-567890abcdef"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Gasto encontrado",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExpenseDto.class),
                                    examples = @ExampleObject(value = "{\"id\": \"b1c2d3e4-f5a6-7890-1234-567890abcdef\", \"description\": \"Alquiler mensual\", \"amount\": 750.00, \"categoryId\": \"housing\", \"accountId\": \"a1b2c3d4-e5f6-7890-1234-567890abcdef\", \"date\": \"2026-10-23T10:00:00Z\"}")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Gasto no encontrado",
                            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"Gasto con ID b1c2d3e4-f5a6-7890-1234-567890abcdef no encontrado\"}"))
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/{id}")
    public ResponseEntity<ExpenseDto> findById(@PathVariable String id) {
        return ResponseEntity.ok(expenseDtoMapper.toDto(expenseService.findById(id)));
    }

    @Operation(
            summary = "Obtiene todos los gastos",
            description = "Recupera una lista de todos los gastos registrados en el sistema.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de gastos",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExpenseDto.class),
                                    examples = @ExampleObject(value = "[{\"id\": \"b1c2d3e4-f5a6-7890-1234-567890abcdef\", \"description\": \"Alquiler mensual\", \"amount\": 750.00, \"categoryId\": \"housing\", \"accountId\": \"a1b2c3d4-e5f6-7890-1234-567890abcdef\", \"date\": \"2026-10-23T10:00:00Z\"}]\n")
                            )
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping
    public ResponseEntity<List<ExpenseDto>> findAllExpenses() {
        List<ExpenseDto> expenses = expenseService.findAllExpenses()
                .stream()
                .map(expenseDtoMapper::toDto)
                .toList();
        return ResponseEntity.ok(expenses);
    }

    @Operation(
            summary = "Obtiene gastos por ID de usuario",
            description = "Recupera una lista de todos los gastos asociados a un ID de usuario específico.",
            parameters = {
                    @Parameter(
                            name = "userId",
                            description = "ID del usuario para buscar sus gastos",
                            required = true,
                            example = "f47ac10b-58cc-4372-a567-0e02b2c3d479"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de gastos encontrados",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExpenseDto.class),
                                    examples = @ExampleObject(value = "[{\"id\": \"b1c2d3e4-f5a6-7890-1234-567890abcdef\", \"description\": \"Alquiler mensual\", \"amount\": 750.00, \"categoryId\": \"housing\", \"accountId\": \"a1b2c3d4-e5f6-7890-1234-567890abcdef\", \"date\": \"2026-10-23T10:00:00Z\"}]\n")
                            )
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ExpenseDto>> findByUserId(@PathVariable String userId) {
        List<ExpenseDto> expenses = expenseService.findByUserId(userId)
                .stream()
                .map(expenseDtoMapper::toDto)
                .toList();
        return ResponseEntity.ok(expenses);
    }

    @Operation(
            summary = "Obtiene gastos por ID de cuenta",
            description = "Recupera una lista de todos los gastos asociados a un ID de cuenta específico.",
            parameters = {
                    @Parameter(
                            name = "accountId",
                            description = "ID de la cuenta para buscar sus gastos",
                            required = true,
                            example = "a1b2c3d4-e5f6-7890-1234-567890abcdef"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de gastos encontrados",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExpenseDto.class),
                                    examples = @ExampleObject(value = "[{\"id\": \"b1c2d3e4-f5a6-7890-1234-567890abcdef\", \"description\": \"Alquiler mensual\", \"amount\": 750.00, \"categoryId\": \"housing\", \"accountId\": \"a1b2c3d4-e5f6-7890-1234-567890abcdef\", \"date\": \"2026-10-23T10:00:00Z\"}]\n")
                            )
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<ExpenseDto>> findByAccountId(@PathVariable String accountId) {
        List<ExpenseDto> expenses = expenseService.findByAccountId(accountId)
                .stream()
                .map(expenseDtoMapper::toDto)
                .toList();
        return ResponseEntity.ok(expenses);
    }

    @Operation(
            summary = "Actualiza un gasto existente",
            description = "Actualiza los detalles de un gasto específico por su ID.",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "ID del gasto a actualizar",
                            required = true,
                            example = "b1c2d3e4-f5a6-7890-1234-567890abcdef"
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Nuevos datos del gasto",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExpenseDto.class),
                            examples = @ExampleObject(value = "{\"description\": \"Alquiler mensual actualizado\", \"amount\": 800.00, \"categoryId\": \"housing\", \"accountId\": \"a1b2c3d4-e5f6-7890-1234-567890abcdef\", \"date\": \"2026-10-23T10:00:00Z\"}")
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Gasto actualizado exitosamente",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExpenseDto.class),
                                    examples = @ExampleObject(value = "{\"id\": \"b1c2d3e4-f5a6-7890-1234-567890abcdef\", \"description\": \"Alquiler mensual actualizado\", \"amount\": 800.00, \"categoryId\": \"housing\", \"accountId\": \"a1b2c3d4-e5f6-7890-1234-567890abcdef\", \"date\": \"2026-10-23T10:00:00Z\"}")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Gasto no encontrado",
                            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"Gasto con ID b1c2d3e4-f5a6-7890-1234-567890abcdef no encontrado\"}"))
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PutMapping("/{id}")
    public ResponseEntity<ExpenseDto> updateExpense(
            @PathVariable String id,
            @RequestBody ExpenseDto expenseDto
    ) {
        Expense expense = expenseDtoMapper.toDomain(expenseDto);
        Expense updated = expenseService.updateExpense(id, expense);
        return ResponseEntity.ok(expenseDtoMapper.toDto(updated));
    }

    @Operation(
            summary = "Elimina un gasto por ID",
            description = "Elimina un gasto específico del sistema utilizando su ID.",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "ID del gasto a eliminar",
                            required = true,
                            example = "b1c2d3e4-f5a6-7890-1234-567890abcdef"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Gasto eliminado exitosamente"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Gasto no encontrado",
                            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"Gasto con ID b1c2d3e4-f5a6-7890-1234-567890abcdef no encontrado\"}"))
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable String id) {
        expenseService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Obtiene gastos de un usuario por rango de fechas",
            description = "Recupera los gastos de un usuario cuya fecha esté entre una fecha mínima y una máxima, ambas inclusive. Formato dd/MM/yyyy. El día final se incluye completo.",
            parameters = {
                    @Parameter(name = "userId", description = "ID del usuario", required = true,
                            example = "f47ac10b-58cc-4372-a567-0e02b2c3d479"),
                    @Parameter(name = "start", description = "Fecha mínima (dd/MM/yyyy)", required = true, example = "01/10/2026"),
                    @Parameter(name = "end", description = "Fecha máxima (dd/MM/yyyy)", required = true, example = "31/10/2026")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de gastos del usuario dentro del rango",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = ExpenseDto.class)),
                                    examples = @ExampleObject(value = "[{\"id\": \"b1c2d3e4-f5a6-7890-1234-567890abcdef\", \"description\": \"Alquiler mensual\", \"amount\": 750.00, \"categoryId\": \"housing\", \"accountId\": \"a1b2c3d4-e5f6-7890-1234-567890abcdef\", \"date\": \"2026-10-23T10:00:00Z\"}]")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Fechas inválidas o fecha mínima posterior a la máxima",
                            content = @Content(mediaType = "application/json",
                                    examples = @ExampleObject(value = "{\"error\": \"La fecha mínima no puede ser posterior a la fecha máxima\"}"))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Usuario no encontrado",
                            content = @Content(mediaType = "application/json",
                                    examples = @ExampleObject(value = "{\"error\": \"Usuario no encontrado\"}"))
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/user/{userId}/range")
    public ResponseEntity<List<ExpenseDto>> findByUserIdAndDateRange(
            @PathVariable String userId,
            @RequestParam("start") @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate start,
            @RequestParam("end") @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate end) {
        return ResponseEntity.ok(expenseService.findByUserIdAndDateRange(userId, start, end)
                .stream()
                .map(expenseDtoMapper::toDto)
                .toList());
    }
}
