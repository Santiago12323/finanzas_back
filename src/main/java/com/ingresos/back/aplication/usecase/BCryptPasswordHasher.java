package com.ingresos.back.aplication.usecase;

import com.ingresos.back.domain.port.PasswordHasher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BCryptPasswordHasher implements PasswordHasher {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public String hash(String password) {
        return passwordEncoder.encode(password);
    }

    @Override
    public boolean matches(String password, String passwordHash) {
        return passwordEncoder.matches(password, passwordHash);
    }
}