package com.ingresos.back.infrastructure.controller.income;

import com.ingresos.back.domain.model.Income;
import com.ingresos.back.domain.port.IncomeService;
import com.ingresos.back.infrastructure.controller.dto.IncomeDto;
import com.ingresos.back.infrastructure.mapper.IncomeDtoMapper;
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
@RequestMapping("/api/incomes")
@Tag(name = "Ingresos", description = "Operaciones para la gestión de ingresos")
public class IncomeController {

    @Autowired
    private IncomeService incomeService;

    @Autowired
    private IncomeDtoMapper incomeDtoMapper;

    @Operation(
            summary = "Crea un nuevo ingreso",
            description = "Registra un nuevo ingreso en el sistema.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del ingreso a crear",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = IncomeDto.class),
                            examples = @ExampleObject(value = "{\"description\": \"Salario mensual\", \"amount\": 2500.00, \"categoryId\": \"salary-uuid\", \"accountId\": \"a1b2c3d4-e5f6-7890-1234-567890abcdef\", \"userId\": \"f47ac10b-58cc-4372-a567-0e02b2c3d479\", \"date\": \"2026-10-23T09:00:00Z\"}")
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Ingreso creado exitosamente",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = IncomeDto.class),
                                    examples = @ExampleObject(value = "{\"id\": \"c1d2e3f4-g5h6-7890-1234-567890abcdef\", \"description\": \"Salario mensual\", \"amount\": 2500.00, \"categoryId\": \"salary-uuid\", \"accountId\": \"a1b2c3d4-e5f6-7890-1234-567890abcdef\", \"userId\": \"f47ac10b-58cc-4372-a567-0e02b2c3d479\", \"date\": \"2026-10-23T09:00:00Z\"}")
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
    public ResponseEntity<IncomeDto> createIncome(@RequestBody IncomeDto incomeDto) {
        System.out.println("categoryId recibido: " + incomeDto.getCategoryId());
        Income income = incomeDtoMapper.toDomain(incomeDto);
        Income created = incomeService.createIncome(income);
        return new ResponseEntity<>(incomeDtoMapper.toDto(created), HttpStatus.CREATED);
    }

    @Operation(summary = "Obtiene un ingreso por ID", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/{id}")
    public ResponseEntity<IncomeDto> findById(@PathVariable String id) {
        return ResponseEntity.ok(incomeDtoMapper.toDto(incomeService.findById(id)));
    }

    @Operation(summary = "Obtiene todos los ingresos", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping
    public ResponseEntity<List<IncomeDto>> findAllIncomes() {
        List<IncomeDto> incomes = incomeService.findAllIncomes()
                .stream()
                .map(incomeDtoMapper::toDto)
                .toList();
        return ResponseEntity.ok(incomes);
    }

    @Operation(summary = "Obtiene ingresos por ID de usuario", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<IncomeDto>> findByUserId(@PathVariable String userId) {
        List<IncomeDto> incomes = incomeService.findByUserId(userId)
                .stream()
                .map(incomeDtoMapper::toDto)
                .toList();
        return ResponseEntity.ok(incomes);
    }

    @Operation(summary = "Obtiene ingresos por ID de cuenta", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<IncomeDto>> findByAccountId(@PathVariable String accountId) {
        List<IncomeDto> incomes = incomeService.findByAccountId(accountId)
                .stream()
                .map(incomeDtoMapper::toDto)
                .toList();
        return ResponseEntity.ok(incomes);
    }

    @Operation(summary = "Actualiza un ingreso existente", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping("/{id}")
    public ResponseEntity<IncomeDto> updateIncome(
            @PathVariable String id,
            @RequestBody IncomeDto incomeDto
    ) {
        Income income = incomeDtoMapper.toDomain(incomeDto);
        Income updated = incomeService.updateIncome(id, income);
        return ResponseEntity.ok(incomeDtoMapper.toDto(updated));
    }

    @Operation(summary = "Elimina un ingreso por ID", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIncome(@PathVariable String id) {
        incomeService.deleteIncome(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Obtiene ingresos de un usuario por rango de fechas", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/user/{userId}/range")
    public ResponseEntity<List<IncomeDto>> findByUserIdAndDateRange(
            @PathVariable String userId,
            @RequestParam("start") @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate start,
            @RequestParam("end") @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate end) {
        return ResponseEntity.ok(incomeService.findByUserIdAndDateRange(userId, start, end)
                .stream()
                .map(incomeDtoMapper::toDto)
                .toList());
    }
}