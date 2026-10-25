package com.ingresos.back.infrastructure.controller.account;

import com.ingresos.back.domain.model.Account;
import com.ingresos.back.domain.port.AccountService;
import com.ingresos.back.infrastructure.controller.dto.AccountDto;
import com.ingresos.back.infrastructure.mapper.AccountDtoMapper;
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
@RequestMapping("/accounts")
@Tag(name = "Cuentas", description = "Operaciones relacionadas con la gestión de cuentas de usuario")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountDtoMapper accountDtoMapper;

    @Operation(
            summary = "Crea una nueva cuenta",
            description = "Crea una nueva cuenta asociada a un usuario específico.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos de la cuenta a crear",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AccountDto.class),
                            examples = @ExampleObject(value = "{\"userId\": \"f47ac10b-58cc-4372-a567-0e02b2c3d479\", \"name\": \"Cuenta de Ahorros\", \"balance\": 1500.75, \"currency\": \"USD\"}")
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Cuenta creada exitosamente",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AccountDto.class),
                                    examples = @ExampleObject(value = "{\"id\": \"a1b2c3d4-e5f6-7890-1234-567890abcdef\", \"userId\": \"f47ac10b-58cc-4372-a567-0e02b2c3d479\", \"name\": \"Cuenta de Ahorros\", \"balance\": 1500.75, \"currency\": \"USD\"}")
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
    public ResponseEntity<AccountDto> createAccount(@RequestBody AccountDto dto) {
        Account account = accountDtoMapper.toDomain(dto);
        Account accountResposne = accountService.createAccount(account, dto.getUserId());
        return new ResponseEntity<>(
                accountDtoMapper.toDto(accountResposne),
                HttpStatus.CREATED
        );
    }

    @Operation(
            summary = "Obtiene una cuenta por ID",
            description = "Recupera los detalles de una cuenta específica utilizando su ID.",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "ID de la cuenta a buscar",
                            required = true,
                            example = "a1b2c3d4-e5f6-7890-1234-567890abcdef"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Cuenta encontrada",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AccountDto.class),
                                    examples = @ExampleObject(value = "{\"id\": \"a1b2c3d4-e5f6-7890-1234-567890abcdef\", \"userId\": \"f47ac10b-58cc-4372-a567-0e02b2c3d479\", \"name\": \"Cuenta de Ahorros\", \"balance\": 1500.75, \"currency\": \"USD\"}")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Cuenta no encontrada",
                            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"Cuenta con ID a1b2c3d4-e5f6-7890-1234-567890abcdef no encontrada\"}"))
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/{id}")
    public ResponseEntity<AccountDto> findById(@PathVariable String id) {
        return ResponseEntity.ok(accountDtoMapper.toDto(accountService.findById(id)));
    }

    @Operation(
            summary = "Obtiene cuentas por ID de usuario",
            description = "Recupera una lista de todas las cuentas asociadas a un ID de usuario específico.",
            parameters = {
                    @Parameter(
                            name = "userId",
                            description = "ID del usuario para buscar sus cuentas",
                            required = true,
                            example = "f47ac10b-58cc-4372-a567-0e02b2c3d479"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de cuentas encontradas",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AccountDto.class),
                                    examples = @ExampleObject(value = "[{\"id\": \"a1b2c3d4-e5f6-7890-1234-567890abcdef\", \"userId\": \"f47ac10b-58cc-4372-a567-0e02b2c3d479\", \"name\": \"Cuenta de Ahorros\", \"balance\": 1500.75, \"currency\": \"USD\"}]")
                            )
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AccountDto>> findByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(accountService.
                findByUserId(userId)
                .stream()
                .map(accountDtoMapper::toDto)
                .toList());
    }

    @Operation(
            summary = "Obtiene todas las cuentas",
            description = "Recupera una lista de todas las cuentas registradas en el sistema.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de todas las cuentas",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AccountDto.class),
                                    examples = @ExampleObject(value = "[{\"id\": \"a1b2c3d4-e5f6-7890-1234-567890abcdef\", \"userId\": \"f47ac10b-58cc-4372-a567-0e02b2c3d479\", \"name\": \"Cuenta de Ahorros\", \"balance\": 1500.75, \"currency\": \"USD\"}]\n")
                            )
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping
    public ResponseEntity<List<AccountDto>> findAllAccounts() {
        return ResponseEntity.ok(accountService.findAllAccounts().stream().map(accountDtoMapper::toDto).toList());
    }

    @Operation(
            summary = "Actualiza una cuenta existente",
            description = "Actualiza los detalles de una cuenta específica utilizando su ID.",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "ID de la cuenta a actualizar",
                            required = true,
                            example = "a1b2c3d4-e5f6-7890-1234-567890abcdef"
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Nuevos datos de la cuenta",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AccountDto.class),
                            examples = @ExampleObject(value = "{\"name\": \"Cuenta de Cheques Actualizada\", \"balance\": 2000.00, \"currency\": \"EUR\"}")
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Cuenta actualizada exitosamente",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AccountDto.class),
                                    examples = @ExampleObject(value = "{\"id\": \"a1b2c3d4-e5f6-7890-1234-567890abcdef\", \"userId\": \"f47ac10b-58cc-4372-a567-0e02b2c3d479\", \"name\": \"Cuenta de Cheques Actualizada\", \"balance\": 2000.00, \"currency\": \"EUR\"}")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Cuenta no encontrada",
                            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"Cuenta con ID a1b2c3d4-e5f6-7890-1234-567890abcdef no encontrada\"}"))
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PutMapping("/{id}")
    public ResponseEntity<AccountDto> updateAccount(@PathVariable String id, @RequestBody AccountDto account) {
        return ResponseEntity
                .ok(accountDtoMapper.toDto(accountService.updateAccount(id, accountDtoMapper.toDomain(account))));
    }

    @Operation(
            summary = "Elimina una cuenta por ID",
            description = "Elimina una cuenta específica del sistema utilizando su ID.",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "ID de la cuenta a eliminar",
                            required = true,
                            example = "a1b2c3d4-e5f6-7890-1234-567890abcdef"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Cuenta eliminada exitosamente"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Cuenta no encontrada",
                            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"Cuenta con ID a1b2c3d4-e5f6-7890-1234-567890abcdef no encontrada\"}"))
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable String id) {
        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }
}
