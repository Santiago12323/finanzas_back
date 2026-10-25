package com.ingresos.back.infrastructure.controller.suscription;

import com.ingresos.back.domain.model.Subscription;
import com.ingresos.back.domain.port.SubscriptionService;
import com.ingresos.back.infrastructure.controller.dto.suscription.SubscriptionDto;
import com.ingresos.back.infrastructure.mapper.SubscriptionDtoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@RequestMapping("/subscriptions")
@Tag(name = "Suscripciones", description = "Operaciones para la gestión de suscripciones periódicas")
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private SubscriptionDtoMapper subscriptionDtoMapper;

    @Operation(
            summary = "Crea una nueva suscripción",
            description = "Registra una nueva suscripción periódica para un usuario.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos de la suscripción a crear",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SubscriptionDto.class),
                            examples = @ExampleObject(value = "{\"userId\": \"f47ac10b-58cc-4372-a567-0e02b2c3d479\", \"name\": \"Netflix\", \"amount\": 15.99, \"currency\": \"USD\", \"startDate\": \"23-10-2026\", \"endDate\": \"23-10-2027\", \"frequency\": \"MONTHLY\"}")
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Suscripción creada exitosamente",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = SubscriptionDto.class),
                                    examples = @ExampleObject(value = "{\"id\": \"d1e2f3g4-h5i6-7890-1234-567890abcdef\", \"userId\": \"f47ac10b-58cc-4372-a567-0e02b2c3d479\", \"name\": \"Netflix\", \"amount\": 15.99, \"currency\": \"USD\", \"startDate\": \"23-10-2026\", \"endDate\": \"23-10-2027\", \"frequency\": \"MONTHLY\"}")
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
    public ResponseEntity<SubscriptionDto> createSubscription(@RequestBody SubscriptionDto dto) {
        Subscription subscription = subscriptionDtoMapper.toDomain(dto);
        Subscription created = subscriptionService.createSubscription(subscription, dto.getUserId());
        return new ResponseEntity<>(
                subscriptionDtoMapper.toDto(created),
                HttpStatus.CREATED
        );
    }

    @Operation(
            summary = "Obtiene una suscripción por ID",
            description = "Recupera los detalles de una suscripción específica utilizando su ID.",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "ID de la suscripción a buscar",
                            required = true,
                            example = "d1e2f3g4-h5i6-7890-1234-567890abcdef"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Suscripción encontrada",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = SubscriptionDto.class),
                                    examples = @ExampleObject(value = "{\"id\": \"d1e2f3g4-h5i6-7890-1234-567890abcdef\", \"userId\": \"f47ac10b-58cc-4372-a567-0e02b2c3d479\", \"name\": \"Netflix\", \"amount\": 15.99, \"currency\": \"USD\", \"startDate\": \"23-10-2026\", \"endDate\": \"23-10-2027\", \"frequency\": \"MONTHLY\"}")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Suscripción no encontrada",
                            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"Suscripción con ID d1e2f3g4-h5i6-7890-1234-567890abcdef no encontrada\"}"))
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionDto> findById(@PathVariable String id) {
        return ResponseEntity.ok(subscriptionDtoMapper.toDto(subscriptionService.findById(id)));
    }

    @Operation(
            summary = "Obtiene suscripciones por ID de usuario",
            description = "Recupera una lista de todas las suscripciones asociadas a un ID de usuario específico.",
            parameters = {
                    @Parameter(
                            name = "userId",
                            description = "ID del usuario para buscar sus suscripciones",
                            required = true,
                            example = "f47ac10b-58cc-4372-a567-0e02b2c3d479"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de suscripciones encontradas",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = SubscriptionDto.class),
                                    examples = @ExampleObject(value = "[{\"id\": \"d1e2f3g4-h5i6-7890-1234-567890abcdef\", \"userId\": \"f47ac10b-58cc-4372-a567-0e02b2c3d479\", \"name\": \"Netflix\", \"amount\": 15.99, \"currency\": \"USD\", \"startDate\": \"23-10-2026\", \"endDate\": \"23-10-2027\", \"frequency\": \"MONTHLY\"}]")
                            )
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SubscriptionDto>> findByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(
                subscriptionService.findByUserId(userId)
                        .stream()
                        .map(subscriptionDtoMapper::toDto)
                        .toList()
        );
    }

    @Operation(
            summary = "Obtiene todas las suscripciones",
            description = "Recupera una lista de todas las suscripciones registradas en el sistema.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de todas las suscripciones",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = SubscriptionDto.class),
                                    examples = @ExampleObject(value = "[{\"id\": \"d1e2f3g4-h5i6-7890-1234-567890abcdef\", \"userId\": \"f47ac10b-58cc-4372-a567-0e02b2c3d479\", \"name\": \"Netflix\", \"amount\": 15.99, \"currency\": \"USD\", \"startDate\": \"23-10-2026\", \"endDate\": \"23-10-2027\", \"frequency\": \"MONTHLY\"}]")
                            )
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping
    public ResponseEntity<List<SubscriptionDto>> findAllSubscriptions() {
        return ResponseEntity.ok(
                subscriptionService.findAllSubscriptions()
                        .stream()
                        .map(subscriptionDtoMapper::toDto)
                        .toList()
        );
    }

    @Operation(
            summary = "Actualiza una suscripción existente",
            description = "Actualiza los detalles de una suscripción específica por su ID.",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "ID de la suscripción a actualizar",
                            required = true,
                            example = "d1e2f3g4-h5i6-7890-1234-567890abcdef"
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Nuevos datos de la suscripción",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SubscriptionDto.class),
                            examples = @ExampleObject(value = "{\"name\": \"Disney+\", \"amount\": 10.99, \"currency\": \"EUR\", \"startDate\": \"23-10-2026\", \"endDate\": \"23-10-2027\", \"frequency\": \"ANNUAL\"}")
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Suscripción actualizada exitosamente",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = SubscriptionDto.class),
                                    examples = @ExampleObject(value = "{\"id\": \"d1e2f3g4-h5i6-7890-1234-567890abcdef\", \"userId\": \"f47ac10b-58cc-4372-a567-0e02b2c3d479\", \"name\": \"Disney+\", \"amount\": 10.99, \"currency\": \"EUR\", \"startDate\": \"23-10-2026\", \"endDate\": \"23-10-2027\", \"frequency\": \"ANNUAL\"}")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Suscripción no encontrada",
                            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"Suscripción con ID d1e2f3g4-h5i6-7890-1234-567890abcdef no encontrada\"}"))
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PutMapping("/{id}")
    public ResponseEntity<SubscriptionDto> updateSubscription(@PathVariable String id, @RequestBody SubscriptionDto dto) {
        Subscription subscription = subscriptionDtoMapper.toDomain(dto);
        Subscription updated = subscriptionService.updateSubscription(id, subscription);
        return ResponseEntity.ok(subscriptionDtoMapper.toDto(updated));
    }

    @Operation(
            summary = "Elimina una suscripción por ID",
            description = "Elimina una suscripción específica del sistema utilizando su ID.",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "ID de la suscripción a eliminar",
                            required = true,
                            example = "d1e2f3g4-h5i6-7890-1234-567890abcdef"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Suscripción eliminada exitosamente"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Suscripción no encontrada",
                            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"Suscripción con ID d1e2f3g4-h5i6-7890-1234-567890abcdef no encontrada\"}"))
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubscription(@PathVariable String id) {
        subscriptionService.deleteSubscription(id);
        return ResponseEntity.noContent().build();
    }
}
