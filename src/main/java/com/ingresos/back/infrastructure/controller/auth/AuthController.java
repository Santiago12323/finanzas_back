package com.ingresos.back.infrastructure.controller.auth;

import com.ingresos.back.domain.model.User;
import com.ingresos.back.domain.port.AuthService;
import com.ingresos.back.infrastructure.controller.dto.auth.*;
import com.ingresos.back.infrastructure.controller.dto.ForgotPasswordRequest;
import com.ingresos.back.infrastructure.controller.dto.UserDto;
import com.ingresos.back.infrastructure.mapper.UserDtoMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "Operaciones relacionadas con la autenticación y gestión de usuarios.")
public class AuthController {

    private final AuthService authService;
    private final UserDtoMapper userDtoMapper;

    @Operation(
            summary = "Inicia sesión de un usuario",
            description = "Autentica a un usuario con sus credenciales y devuelve tokens de acceso y refresco.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Credenciales del usuario para iniciar sesión",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoginDto.class),
                            examples = @ExampleObject(value = "{\"email\": \"test@example.com\", \"password\": \"password123\"}")
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Autenticación exitosa",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AuthResponse.class),
                                    examples = @ExampleObject(value = "{\"accessToken\": \"eyJhbGciOi...\", \"refreshToken\": \"eyJhbGciOi...\"}")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Credenciales inválidas",
                            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"Credenciales inválidas\"}"))
                    )
            }
    )
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody LoginDto request
    ) {
        AuthResponse response = authService.login(
                request.getEmail(),
                request.getPassword()
        );
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Refresca el token de acceso",
            description = "Utiliza un token de refresco existente para obtener un nuevo token de acceso y un nuevo token de refresco.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Token de refresco",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RefreshTokenDto.class),
                            examples = @ExampleObject(value = "{\"refreshToken\": \"eyJhbGciOi...\"}")
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Tokens refrescados exitosamente",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AuthResponse.class),
                                    examples = @ExampleObject(value = "{\"accessToken\": \"eyJhbGciOi...\", \"refreshToken\": \"eyJhbGciOi...\"}")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Token de refresco inválido o expirado",
                            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"Token de refresco inválido o expirado\"}"))
                    )
            }
    )
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(
            @RequestBody RefreshTokenDto request
    ) {
        AuthResponse response = authService.refreshToken(
                request.getRefreshToken()
        );
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Registra un nuevo usuario",
            description = "Crea una nueva cuenta de usuario con los datos proporcionados.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del nuevo usuario",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RegisterDto.class),
                            examples = @ExampleObject(value = "{\"name\": \"Nuevo Usuario\", \"email\": \"newuser@example.com\", \"password\": \"securepassword123\"}")
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Usuario registrado exitosamente",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserDto.class),
                                    examples = @ExampleObject(value = "{\"id\": 1, \"name\": \"Nuevo Usuario\", \"email\": \"newuser@example.com\"}")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Solicitud inválida (ej. email ya registrado)",
                            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"El email ya está registrado\"}"))
                    )
            }
    )
    @PostMapping("/register")
    public ResponseEntity<UserDto> register(
            @RequestBody RegisterDto request
    ) {
        User user = authService.register(
                request.getName(),
                request.getEmail(),
                request.getPassword()
        );
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userDtoMapper.toDto(user));
    }

    @Operation(
            summary = "Inicia sesión con Google",
            description = "Recibe el ID token de Google, lo valida y devuelve los tokens de acceso y refresco del backend. "
                    + "Si el correo no está registrado, crea la cuenta con el nombre y el correo de Google, sin contraseña. "
                    + "Si el correo ya está registrado con contraseña, responde con error y no vincula las cuentas.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "ID token entregado por Google",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GoogleLoginDto.class),
                            examples = @ExampleObject(value = "{\"idToken\": \"eyJhbGciOiJSUzI1NiIsImtpZCI6...\"}")
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Autenticación exitosa",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AuthResponse.class),
                                    examples = @ExampleObject(value = "{\"accessToken\": \"eyJhbGciOi...\", \"refreshToken\": \"eyJhbGciOi...\"}")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Token de Google inválido, correo no verificado o correo ya registrado con contraseña",
                            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"El email ya está registrado. Inicia sesión con tu contraseña\"}"))
                    )
            }
    )
    @PostMapping("/google")
    public ResponseEntity<AuthResponse> loginWithGoogle(
            @RequestBody GoogleLoginDto request
    ) {
        return ResponseEntity.ok(authService.loginWithGoogle(request.getIdToken()));
    }

    @Operation(
            summary = "Solicita restablecimiento de contraseña",
            description = "Envía un correo electrónico al usuario con instrucciones para restablecer su contraseña.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Email del usuario para restablecer contraseña",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ForgotPasswordRequest.class),
                            examples = @ExampleObject(value = "{\"email\": \"test@example.com\"}")
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Instrucciones enviadas exitosamente",
                            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"message\": \"Instrucciones para restablecer contraseña enviadas a su email.\"}"))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Usuario no encontrado",
                            content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"Usuario no encontrado\"}"))
                    )
            }
    )
    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(
            @RequestBody ForgotPasswordRequest request
    ) {
        authService.forgotPassword(
                request.getEmail()
        );
        return ResponseEntity.ok().build();
    }
}
