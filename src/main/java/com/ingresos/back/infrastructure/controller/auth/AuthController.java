package com.ingresos.back.infrastructure.controller.auth;

import com.ingresos.back.domain.model.User;
import com.ingresos.back.domain.port.AuthService;
import com.ingresos.back.infrastructure.controller.dto.auth.AuthResponse;
import com.ingresos.back.infrastructure.controller.dto.ForgotPasswordRequest;
import com.ingresos.back.infrastructure.controller.dto.auth.LoginDto;
import com.ingresos.back.infrastructure.controller.dto.auth.RefreshTokenDto;
import com.ingresos.back.infrastructure.controller.dto.auth.RegisterDto;
import com.ingresos.back.infrastructure.controller.dto.UserDto;
import com.ingresos.back.infrastructure.mapper.UserDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserDtoMapper userDtoMapper;

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

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(
            @RequestBody RefreshTokenDto request
    ) {

        AuthResponse response = authService.refreshToken(
                request.getRefreshToken()
        );

        return ResponseEntity.ok(response);
    }

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
