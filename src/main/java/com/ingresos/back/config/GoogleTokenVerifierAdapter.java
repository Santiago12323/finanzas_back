package com.ingresos.back.config;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.ingresos.back.domain.model.GoogleUserInfo;
import com.ingresos.back.domain.port.GoogleTokenVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Component
public class GoogleTokenVerifierAdapter implements GoogleTokenVerifier {

    private final GoogleIdTokenVerifier verifier;

    public GoogleTokenVerifierAdapter(@Value("${google.client-ids}") String clientIds) {
        List<String> audiences = Arrays.stream(clientIds.split(","))
                .map(String::trim)
                .filter(id -> !id.isBlank())
                .toList();
        if (audiences.isEmpty()) {
            throw new IllegalStateException("Falta configurar google.client-ids");
        }
        // Valida firma, emisor, expiración y que el token haya sido emitido para nuestras apps
        this.verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance())
                .setAudience(audiences)
                .build();
    }

    @Override
    public GoogleUserInfo verify(String idToken) {
        if (idToken == null || idToken.isBlank()) {
            throw new RuntimeException("El token de Google es obligatorio");
        }
        try {
            GoogleIdToken token = verifier.verify(idToken);
            if (token == null) {
                throw new RuntimeException("Token de Google inválido o expirado");
            }
            GoogleIdToken.Payload payload = token.getPayload();

            String email = payload.getEmail();
            if (email == null || email.isBlank()) {
                throw new RuntimeException("El token de Google no incluye un correo");
            }
            email = email.toLowerCase(Locale.ROOT);

            String name = (String) payload.get("name");
            if (name == null || name.isBlank()) {
                name = email.substring(0, email.indexOf('@'));
            }

            return new GoogleUserInfo(email, Boolean.TRUE.equals(payload.getEmailVerified()), name, payload.getSubject());
        } catch (GeneralSecurityException | IOException | IllegalArgumentException e) {
            throw new RuntimeException("Token de Google inválido o expirado", e);
        }
    }
}