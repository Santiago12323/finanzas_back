package com.ingresos.back.infrastructure.controller.savings;

import com.ingresos.back.domain.enums.savings.GoalStatus;
import com.ingresos.back.domain.model.SavingsContribution;
import com.ingresos.back.domain.model.SavingsGoal;
import com.ingresos.back.domain.port.SavingsGoalService;
import com.ingresos.back.infrastructure.controller.dto.SavingsContributionDto;
import com.ingresos.back.infrastructure.controller.dto.SavingsGoalDto;
import com.ingresos.back.infrastructure.mapper.SavingsContributionDtoMapper;
import com.ingresos.back.infrastructure.mapper.SavingsGoalDtoMapper;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/savings-goals")
@Tag(name = "Metas de ahorro", description = "Operaciones para la gestión de metas de ahorro y sus aportes")
public class SavingsGoalController {

    @Autowired
    private SavingsGoalService savingsGoalService;

    @Autowired
    private SavingsGoalDtoMapper goalDtoMapper;

    @Autowired
    private SavingsContributionDtoMapper contributionDtoMapper;

    @Operation(
            summary = "Crea una nueva meta de ahorro",
            description = "Registra una meta de ahorro con su monto objetivo, ahorro mensual planeado y fecha objetivo. Nace en estado ACTIVE.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos de la meta a crear",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SavingsGoalDto.class),
                            examples = @ExampleObject(value = "{\"name\": \"Viaje a Japón\", \"targetAmount\": 12000000.00, \"currency\": \"COP\", \"plannedMonthlyAmount\": 800000.00, \"targetDate\": \"2027-12-31\", \"userId\": \"f47ac10b-58cc-4372-a567-0e02b2c3d479\"}")
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Meta creada exitosamente",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = SavingsGoalDto.class),
                                    examples = @ExampleObject(value = "{\"id\": \"d1e2f3a4-b5c6-7890-1234-567890abcdef\", \"name\": \"Viaje a Japón\", \"targetAmount\": 12000000.00, \"currency\": \"COP\", \"plannedMonthlyAmount\": 800000.00, \"startDate\": \"2026-10-24\", \"targetDate\": \"2027-12-31\", \"status\": \"ACTIVE\", \"userId\": \"f47ac10b-58cc-4372-a567-0e02b2c3d479\", \"savedAmount\": 0, \"remainingAmount\": 12000000.00, \"progressPercent\": 0.00, \"monthsLeft\": 15, \"requiredMonthlyAmount\": 800000.00, \"onTrack\": true}")
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Solicitud inválida",
                            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"El monto objetivo debe ser mayor a cero\"}")))
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping
    public ResponseEntity<SavingsGoalDto> createGoal(@RequestBody SavingsGoalDto goalDto) {
        SavingsGoal created = savingsGoalService.createGoal(goalDtoMapper.toDomain(goalDto));
        return new ResponseEntity<>(goalDtoMapper.toDto(created), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Obtiene una meta de ahorro por ID",
            description = "Recupera la meta con su progreso: total ahorrado, lo que falta, porcentaje, meses restantes y ahorro mensual requerido.",
            parameters = @Parameter(name = "id", description = "ID de la meta", required = true, example = "d1e2f3a4-b5c6-7890-1234-567890abcdef"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Meta encontrada",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SavingsGoalDto.class))),
                    @ApiResponse(responseCode = "404", description = "Meta no encontrada",
                            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"Meta de ahorro no encontrada con ID: d1e2f3a4-b5c6-7890-1234-567890abcdef\"}")))
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/{id}")
    public ResponseEntity<SavingsGoalDto> findById(@PathVariable String id) {
        return ResponseEntity.ok(goalDtoMapper.toDto(savingsGoalService.findById(id)));
    }

    @Operation(
            summary = "Obtiene las metas de ahorro de un usuario",
            description = "Recupera todas las metas del usuario con su progreso. Se puede filtrar por estado.",
            parameters = {
                    @Parameter(name = "userId", description = "ID del usuario", required = true, example = "f47ac10b-58cc-4372-a567-0e02b2c3d479"),
                    @Parameter(name = "status", description = "Filtra por estado: ACTIVE, PAUSED, COMPLETED o CANCELLED (opcional)", example = "ACTIVE")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de metas del usuario",
                            content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SavingsGoalDto.class)))),
                    @ApiResponse(responseCode = "404", description = "Usuario no encontrado",
                            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"Usuario no encontrado\"}")))
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SavingsGoalDto>> findByUserId(
            @PathVariable String userId,
            @RequestParam(value = "status", required = false) GoalStatus status) {
        return ResponseEntity.ok(savingsGoalService.findByUserId(userId, status)
                .stream()
                .map(goalDtoMapper::toDto)
                .toList());
    }

    @Operation(
            summary = "Actualiza una meta de ahorro",
            description = "Actualiza nombre, monto objetivo, ahorro mensual, fechas y cuenta destino. El dueño y el estado no se modifican por aquí. Si el nuevo monto objetivo ya se alcanzó, la meta pasa a COMPLETED.",
            parameters = @Parameter(name = "id", description = "ID de la meta a actualizar", required = true, example = "d1e2f3a4-b5c6-7890-1234-567890abcdef"),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Nuevos datos de la meta",
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SavingsGoalDto.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Meta actualizada exitosamente",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SavingsGoalDto.class))),
                    @ApiResponse(responseCode = "400", description = "Solicitud inválida",
                            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"La fecha objetivo no puede ser anterior a la fecha de inicio\"}"))),
                    @ApiResponse(responseCode = "404", description = "Meta no encontrada",
                            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"Meta de ahorro no encontrada con ID: d1e2f3a4-b5c6-7890-1234-567890abcdef\"}")))
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PutMapping("/{id}")
    public ResponseEntity<SavingsGoalDto> updateGoal(@PathVariable String id, @RequestBody SavingsGoalDto goalDto) {
        SavingsGoal updated = savingsGoalService.updateGoal(id, goalDtoMapper.toDomain(goalDto));
        return ResponseEntity.ok(goalDtoMapper.toDto(updated));
    }

    @Operation(
            summary = "Cambia el estado de una meta",
            description = "Permite pausar (PAUSED), reactivar (ACTIVE) o cancelar (CANCELLED) una meta. COMPLETED no se puede asignar manualmente: se establece solo al alcanzar el monto objetivo. Las metas completadas o canceladas ya no cambian de estado.",
            parameters = {
                    @Parameter(name = "id", description = "ID de la meta", required = true, example = "d1e2f3a4-b5c6-7890-1234-567890abcdef"),
                    @Parameter(name = "status", description = "Nuevo estado: ACTIVE, PAUSED o CANCELLED", required = true, example = "PAUSED")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Estado actualizado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SavingsGoalDto.class))),
                    @ApiResponse(responseCode = "400", description = "Cambio de estado no permitido",
                            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"No se puede cambiar el estado de una meta completada o cancelada\"}"))),
                    @ApiResponse(responseCode = "404", description = "Meta no encontrada",
                            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"Meta de ahorro no encontrada con ID: d1e2f3a4-b5c6-7890-1234-567890abcdef\"}")))
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PatchMapping("/{id}/status")
    public ResponseEntity<SavingsGoalDto> changeStatus(@PathVariable String id, @RequestParam("status") GoalStatus status) {
        return ResponseEntity.ok(goalDtoMapper.toDto(savingsGoalService.changeStatus(id, status)));
    }

    @Operation(
            summary = "Elimina una meta de ahorro",
            description = "Elimina la meta junto con todos sus aportes.",
            parameters = @Parameter(name = "id", description = "ID de la meta a eliminar", required = true, example = "d1e2f3a4-b5c6-7890-1234-567890abcdef"),
            responses = {
                    @ApiResponse(responseCode = "204", description = "Meta eliminada exitosamente"),
                    @ApiResponse(responseCode = "404", description = "Meta no encontrada",
                            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"Meta de ahorro no encontrada con ID: d1e2f3a4-b5c6-7890-1234-567890abcdef\"}")))
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGoal(@PathVariable String id) {
        savingsGoalService.deleteGoal(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Registra un aporte a una meta",
            description = "Suma un aporte a la meta. Solo se aceptan aportes en metas ACTIVE. Si con este aporte se alcanza el monto objetivo, la meta pasa a COMPLETED.",
            parameters = @Parameter(name = "id", description = "ID de la meta", required = true, example = "d1e2f3a4-b5c6-7890-1234-567890abcdef"),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del aporte",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SavingsContributionDto.class),
                            examples = @ExampleObject(value = "{\"amount\": 800000.00, \"contributionDate\": \"2026-10-24\", \"accountId\": \"a1b2c3d4-e5f6-7890-1234-567890abcdef\"}")
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Aporte registrado exitosamente",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SavingsContributionDto.class))),
                    @ApiResponse(responseCode = "400", description = "Aporte inválido o meta no activa",
                            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"Solo se pueden registrar aportes en metas activas\"}"))),
                    @ApiResponse(responseCode = "404", description = "Meta no encontrada",
                            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"Meta de ahorro no encontrada con ID: d1e2f3a4-b5c6-7890-1234-567890abcdef\"}")))
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("/{id}/contributions")
    public ResponseEntity<SavingsContributionDto> addContribution(
            @PathVariable String id,
            @RequestBody SavingsContributionDto contributionDto) {
        SavingsContribution created = savingsGoalService.addContribution(id, contributionDtoMapper.toDomain(contributionDto));
        return new ResponseEntity<>(contributionDtoMapper.toDto(created), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Obtiene los aportes de una meta",
            description = "Recupera el historial de aportes de la meta, del más reciente al más antiguo.",
            parameters = @Parameter(name = "id", description = "ID de la meta", required = true, example = "d1e2f3a4-b5c6-7890-1234-567890abcdef"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de aportes",
                            content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = SavingsContributionDto.class)))),
                    @ApiResponse(responseCode = "404", description = "Meta no encontrada",
                            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"Meta de ahorro no encontrada con ID: d1e2f3a4-b5c6-7890-1234-567890abcdef\"}")))
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/{id}/contributions")
    public ResponseEntity<List<SavingsContributionDto>> findContributions(@PathVariable String id) {
        return ResponseEntity.ok(savingsGoalService.findContributionsByGoalId(id)
                .stream()
                .map(contributionDtoMapper::toDto)
                .toList());
    }

    @Operation(
            summary = "Elimina un aporte de una meta",
            description = "Elimina un aporte. Si la meta estaba COMPLETED y con esto deja de alcanzar el monto objetivo, vuelve a ACTIVE.",
            parameters = {
                    @Parameter(name = "id", description = "ID de la meta", required = true, example = "d1e2f3a4-b5c6-7890-1234-567890abcdef"),
                    @Parameter(name = "contributionId", description = "ID del aporte a eliminar", required = true, example = "9a8b7c6d-5e4f-3210-abcd-ef0123456789")
            },
            responses = {
                    @ApiResponse(responseCode = "204", description = "Aporte eliminado exitosamente"),
                    @ApiResponse(responseCode = "404", description = "Meta o aporte no encontrado",
                            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"Aporte no encontrado con ID: 9a8b7c6d-5e4f-3210-abcd-ef0123456789\"}")))
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @DeleteMapping("/{id}/contributions/{contributionId}")
    public ResponseEntity<Void> deleteContribution(@PathVariable String id, @PathVariable String contributionId) {
        savingsGoalService.deleteContribution(id, contributionId);
        return ResponseEntity.noContent().build();
    }
}