package com.ingresos.back.infrastructure.controller.user;

import com.ingresos.back.domain.model.User;
import com.ingresos.back.domain.port.UseService;
import com.ingresos.back.infrastructure.controller.dto.UserDto;
import com.ingresos.back.infrastructure.mapper.UserDtoMapper;
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
@RequestMapping("/api/users")
@Tag(name = "Usuarios", description = "Operaciones para la gestión de usuarios")
public class UserController {

    @Autowired
    private UseService useService;

    @Autowired
    private UserDtoMapper userDtoMapper;

    @Operation(
            summary = "Crea un nuevo usuario",
            description = "Registra un nuevo usuario en el sistema.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del usuario a crear",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class),
                            examples = @ExampleObject(value = "{\"name\": \"Jane Doe\", \"email\": \"jane.doe@example.com\", \"password\": \"strongpassword\", \"confirmPassword\": \"strongpassword\"}")
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Usuario creado exitosamente",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserDto.class),
                                    examples = @ExampleObject(value = "{\"id\": \"f47ac10b-58cc-4372-a567-0e02b2c3d479\", \"name\": \"Jane Doe\", \"email\": \"jane.doe@example.com\"}")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Solicitud inválida (ej. email ya registrado)",
                            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"El email ya está registrado\"}"))
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping
    public ResponseEntity<UserDto> createUser(
            @RequestBody UserDto userDto
    ) {
        User user = userDtoMapper.toDomain(userDto);
        User createdUser = useService.createUser(user);
        return new ResponseEntity<>(
                userDtoMapper.toDto(createdUser),
                HttpStatus.CREATED
        );
    }

    @Operation(
            summary = "Obtiene un usuario por ID",
            description = "Recupera los detalles de un usuario específico utilizando su ID.",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "ID del usuario a buscar",
                            required = true,
                            example = "f47ac10b-58cc-4372-a567-0e02b2c3d479"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Usuario encontrado",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserDto.class),
                                    examples = @ExampleObject(value = "{\"id\": \"f47ac10b-58cc-4372-a567-0e02b2c3d479\", \"name\": \"Jane Doe\", \"email\": \"jane.doe@example.com\"}")
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
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> findById(
            @PathVariable String id
    ) {
        User user = useService.findById(id);
        return ResponseEntity.ok(
                userDtoMapper.toDto(user)
        );
    }

    @Operation(
            summary = "Obtiene todos los usuarios",
            description = "Recupera una lista de todos los usuarios registrados en el sistema.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de usuarios",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserDto.class),
                                    examples = @ExampleObject(value = "[{\"id\": \"f47ac10b-58cc-4372-a567-0e02b2c3d479\", \"name\": \"Jane Doe\", \"email\": \"jane.doe@example.com\"}, {\"id\": \"a1b2c3d4-e5f6-7890-1234-567890abcdef\", \"name\": \"John Smith\", \"email\": \"john.smith@example.com\"}]")
                            )
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping
    public ResponseEntity<List<UserDto>> findAllUsers() {
        List<UserDto> users = useService.findAllUsers()
                .stream()
                .map(userDtoMapper::toDto)
                .toList();
        return ResponseEntity.ok(users);
    }

    @Operation(
            summary = "Actualiza un usuario existente",
            description = "Actualiza la información de un usuario específico por su ID.",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "ID del usuario a actualizar",
                            required = true,
                            example = "f47ac10b-58cc-4372-a567-0e02b2c3d479"
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Nuevos datos del usuario",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class),
                            examples = @ExampleObject(value = "{\"name\": \"Jane Updated\", \"email\": \"jane.updated@example.com\", \"password\": \"newstrongpassword\", \"confirmPassword\": \"newstrongpassword\"}")
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Usuario actualizado exitosamente",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserDto.class),
                                    examples = @ExampleObject(value = "{\"id\": \"f47ac10b-58cc-4372-a567-0e02b2c3d479\", \"name\": \"Jane Updated\", \"email\": \"jane.updated@example.com\"}")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Usuario no encontrado",
                            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"Usuario con ID f47ac10b-58cc-4372-a567-0e02b2c3d479 no encontrado\"}"))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Solicitud inválida",
                            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"Email ya en uso\"}"))
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable String id,
            @RequestBody UserDto userDto
    ) {
        User user = userDtoMapper.toDomain(userDto);
        User updatedUser = useService.updateUser(id, user);
        return ResponseEntity.ok(
                userDtoMapper.toDto(updatedUser)
        );
    }

    @Operation(
            summary = "Elimina un usuario por ID",
            description = "Elimina un usuario específico del sistema utilizando su ID.",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "ID del usuario a eliminar",
                            required = true,
                            example = "f47ac10b-58cc-4372-a567-0e02b2c3d479"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Usuario eliminado exitosamente"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Usuario no encontrado",
                            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"Usuario con ID f47ac10b-58cc-4372-a567-0e02b2c3d479 no encontrado\"}"))
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable String id
    ) {
        useService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
