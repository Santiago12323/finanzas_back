package com.ingresos.back.domain.port;

public interface PasswordHasher {
    String hash(String password);

    boolean matches(String password, String hash);
}
